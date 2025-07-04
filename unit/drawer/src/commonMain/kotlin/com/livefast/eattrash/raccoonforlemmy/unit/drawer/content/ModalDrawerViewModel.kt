package com.livefast.eattrash.raccoonforlemmy.unit.drawer.content

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.navigation.TabNavigationSection
import com.livefast.eattrash.raccoonforlemmy.core.navigation.toTabNavigationSections
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.FavoriteCommunityModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.AccountRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.FavoriteCommunityRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.MultiCommunityRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.ApiConfigurationRepository
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.CommunityPaginationManager
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.CommunityPaginationSpecification
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.CommunityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.SiteRepository
import com.livefast.eattrash.raccoonforlemmy.unit.drawer.cache.SubscriptionsCache
import com.livefast.eattrash.raccoonforlemmy.unit.drawer.cache.SubscriptionsCacheState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.yield

@OptIn(FlowPreview::class)
class ModalDrawerViewModel(
    private val identityRepository: IdentityRepository,
    private val communityRepository: CommunityRepository,
    private val accountRepository: AccountRepository,
    private val multiCommunityRepository: MultiCommunityRepository,
    private val siteRepository: SiteRepository,
    private val apiConfigurationRepository: ApiConfigurationRepository,
    private val settingsRepository: SettingsRepository,
    private val favoriteCommunityRepository: FavoriteCommunityRepository,
    private val communityPaginationManager: CommunityPaginationManager,
    private val notificationCenter: NotificationCenter,
    private val subscriptionsCache: SubscriptionsCache,
) : ViewModel(),
    MviModelDelegate<ModalDrawerMviModel.Intent, ModalDrawerMviModel.UiState, ModalDrawerMviModel.Effect>
    by DefaultMviModelDelegate(initialState = ModalDrawerMviModel.UiState()),
    ModalDrawerMviModel {
    init {
        viewModelScope.launch {
            apiConfigurationRepository.instance
                .onEach { instance ->
                    updateState {
                        it.copy(instance = instance)
                    }
                }.launchIn(this)

            identityRepository.isLogged
                .onEach { _ ->
                    refreshUser()
                }.launchIn(this)

            subscriptionsCache.state
                .onEach {
                    if (it is SubscriptionsCacheState.Loaded) {
                        loadCachedSubscriptions()
                    }
                }.launchIn(this)

            notificationCenter
                .subscribe(NotificationCenterEvent.Logout::class)
                .onEach {
                    delay(250)
                    refreshUser()
                    refresh()
                }.launchIn(this)

            notificationCenter
                .subscribe(NotificationCenterEvent.FavoritesUpdated::class)
                .onEach {
                    refresh()
                }.launchIn(this)

            settingsRepository.currentSettings
                .onEach { settings ->
                    updateState {
                        it.copy(
                            autoLoadImages = settings.autoLoadImages,
                            preferNicknames = settings.preferUserNicknames,
                            enableToggleFavorite = settings.enableToggleFavoriteInNavDrawer,
                        )
                    }
                }.launchIn(this)
            settingsRepository.currentBottomBarSections
                .onEach { sectionIds ->
                    val isSettingsInBottomBar =
                        sectionIds.toTabNavigationSections().contains(TabNavigationSection.Settings)
                    updateState {
                        it.copy(isSettingsVisible = !isSettingsInBottomBar)
                    }
                }.launchIn(this)

            uiState
                .map { it.searchText }
                .distinctUntilChanged()
                .drop(1)
                .debounce(1_000)
                .onEach {
                    refresh()
                }.launchIn(this)

            delay(250)
            refreshUser()
            refresh(initial = true)
        }
    }

    override fun reduce(intent: ModalDrawerMviModel.Intent) {
        when (intent) {
            ModalDrawerMviModel.Intent.Refresh ->
                viewModelScope.launch {
                    refresh()
                }

            is ModalDrawerMviModel.Intent.SetSearch ->
                viewModelScope.launch {
                    updateState { it.copy(searchText = intent.value) }
                }

            is ModalDrawerMviModel.Intent.ToggleFavorite -> toggleFavorite(intent.id)
        }
    }

    private suspend fun refreshUser() {
        val auth = identityRepository.authToken.value.orEmpty()
        if (auth.isEmpty()) {
            updateState { it.copy(user = null) }
        } else {
            var user = siteRepository.getCurrentUser(auth)
            withContext(Dispatchers.IO) {
                runCatching {
                    withTimeout(2000) {
                        while (user == null) {
                            // retry getting user if non-empty auth
                            delay(500)
                            user = siteRepository.getCurrentUser(auth)
                            yield()
                        }
                        updateState { it.copy(user = user) }
                    }
                }
            }
        }
    }

    private suspend fun refresh(initial: Boolean = false) {
        if (uiState.value.refreshing) {
            return
        }
        val searchText = uiState.value.searchText
        communityPaginationManager.reset(
            CommunityPaginationSpecification.Subscribed(
                searchText = searchText,
            ),
        )
        updateState {
            it.copy(
                isFiltering = searchText.isNotEmpty(),
                refreshing = true,
                loading = false,
            )
        }

        val accountId = accountRepository.getActive()?.id
        val multiCommunities =
            accountId
                ?.let {
                    multiCommunityRepository
                        .getAll(it)
                        .let { communities ->
                            if (searchText.isEmpty()) {
                                communities
                            } else {
                                communities.filter { c ->
                                    c.name.contains(
                                        other = searchText,
                                        ignoreCase = true,
                                    )
                                }
                            }
                        }.sortedBy { e -> e.name }
                }.orEmpty()

        val favorites =
            coroutineScope {
                val auth = identityRepository.authToken.value
                favoriteCommunityRepository.getAll(accountId).mapNotNull { favorite ->
                    val communityId = favorite.communityId
                    async {
                        communityRepository.get(
                            auth = auth,
                            id = communityId,
                        )
                    }.await()
                }
            }.let { communities ->
                if (searchText.isEmpty()) {
                    communities
                } else {
                    communities.filter { c ->
                        c.name.contains(
                            other = searchText,
                            ignoreCase = true,
                        )
                    }
                }
            }

        updateState {
            it.copy(
                multiCommunities = multiCommunities,
                favorites = favorites,
            )
        }

        if (!initial) {
            loadAllSubscriptions()
        } else {
            loadCachedSubscriptions()
        }
    }

    private suspend fun loadCachedSubscriptions() {
        val cachedValues =
            subscriptionsCache.state
                .filterIsInstance<SubscriptionsCacheState.Loaded>()
                .first()
                .communities
        updateState {
            it.copy(
                communities = cachedValues.sortedBy { c -> c.name },
                refreshing = false,
                loading = false,
            )
        }
    }

    private suspend fun loadAllSubscriptions() {
        val currentState = uiState.value
        if (currentState.loading) {
            updateState { it.copy(refreshing = false) }
            return
        }
        val itemsToAdd = communityPaginationManager.fetchAll().sortedBy { c -> c.name }
        updateState {
            it.copy(
                refreshing = false,
                communities = itemsToAdd,
                loading = false,
            )
        }
    }

    private fun toggleFavorite(communityId: Long) {
        viewModelScope.launch {
            val currentState = uiState.value
            val accountId = accountRepository.getActive()?.id ?: 0L
            val isCurrentlyFavorite = currentState.favorites.any { it.id == communityId }
            if (isCurrentlyFavorite) {
                val community = currentState.favorites.first { it.id == communityId }
                favoriteCommunityRepository.getBy(accountId, communityId)?.also { toDelete ->
                    favoriteCommunityRepository.delete(accountId, toDelete)
                }
                val newFavorites = currentState.favorites.filter { it.id != communityId }
                val newCommunities = currentState.communities + community
                updateState {
                    it.copy(
                        favorites = newFavorites,
                        communities = newCommunities.sortedBy { c -> c.name },
                    )
                }
            } else {
                val model = FavoriteCommunityModel(communityId = communityId)
                favoriteCommunityRepository.create(model, accountId)
                val community = currentState.communities.first { it.id == communityId }
                val newFavorites = currentState.favorites + community
                val newCommunities = currentState.communities.filter { it.id != communityId }
                updateState {
                    it.copy(
                        favorites = newFavorites.sortedBy { c -> c.name },
                        communities = newCommunities,
                    )
                }
            }
        }
    }
}
