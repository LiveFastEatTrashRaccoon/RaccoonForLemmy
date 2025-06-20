package com.livefast.eattrash.raccoonforlemmy.unit.managesubscriptions

import cafe.adriel.voyager.core.model.screenModelScope
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModel
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.FavoriteCommunityModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.MultiCommunityModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.AccountRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.FavoriteCommunityRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.MultiCommunityRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import com.livefast.eattrash.raccoonforlemmy.core.utils.vibrate.HapticFeedback
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.CommunityPaginationManager
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.CommunityPaginationSpecification
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.CommunityRepository
import com.livefast.eattrash.raccoonforlemmy.unit.managesubscriptions.ManageSubscriptionsMviModel.Effect
import com.livefast.eattrash.raccoonforlemmy.unit.managesubscriptions.ManageSubscriptionsMviModel.Intent
import com.livefast.eattrash.raccoonforlemmy.unit.managesubscriptions.ManageSubscriptionsMviModel.UiState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class ManageSubscriptionsViewModel(
    private val identityRepository: IdentityRepository,
    private val communityRepository: CommunityRepository,
    private val accountRepository: AccountRepository,
    private val multiCommunityRepository: MultiCommunityRepository,
    private val settingsRepository: SettingsRepository,
    private val favoriteCommunityRepository: FavoriteCommunityRepository,
    private val communityPaginationManager: CommunityPaginationManager,
    private val hapticFeedback: HapticFeedback,
    private val notificationCenter: NotificationCenter,
) : DefaultMviModel<Intent, UiState, Effect>(
    initialState = UiState(),
),
    ManageSubscriptionsMviModel {
    init {
        screenModelScope.launch {
            settingsRepository.currentSettings
                .onEach { settings ->
                    updateState {
                        it.copy(
                            autoLoadImages = settings.autoLoadImages,
                            preferNicknames = settings.preferUserNicknames,
                        )
                    }
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.MultiCommunityCreated::class)
                .onEach { evt ->
                    handleMultiCommunityCreated(evt.model)
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.CommunitySubscriptionChanged::class)
                .onEach { evt ->
                    handleCommunityUpdate(evt.value)
                }.launchIn(this)

            uiState
                .map { it.searchText }
                .distinctUntilChanged()
                .drop(1)
                .debounce(1_000)
                .onEach {
                    if (!uiState.value.initial) {
                        emitEffect(Effect.BackToTop)
                        refresh()
                    }
                }.launchIn(this)
            if (uiState.value.communities.isEmpty()) {
                refresh(initial = true)
            }
        }
    }

    override fun reduce(intent: Intent) {
        when (intent) {
            Intent.HapticIndication -> hapticFeedback.vibrate()
            Intent.Refresh -> screenModelScope.launch { refresh() }
            is Intent.Unsubscribe -> {
                uiState.value.communities.firstOrNull { it.id == intent.id }?.also { community ->
                    unsubscribe(community)
                }
            }

            is Intent.DeleteMultiCommunity -> {
                uiState.value.multiCommunities
                    .firstOrNull {
                        (it.id ?: 0L) == intent.id
                    }?.also { community ->
                        deleteMultiCommunity(community)
                    }
            }

            is Intent.ToggleFavorite -> {
                uiState.value.communities.firstOrNull { it.id == intent.id }?.also { community ->
                    toggleFavorite(community)
                }
            }

            is Intent.SetSearch -> updateSearchText(intent.value)

            Intent.LoadNextPage ->
                screenModelScope.launch {
                    loadNextPage()
                }
        }
    }

    private suspend fun refresh(initial: Boolean = false) {
        val searchText = uiState.value.searchText
        communityPaginationManager.reset(
            CommunityPaginationSpecification.Subscribed(
                searchText = searchText,
            ),
        )
        val accountId = accountRepository.getActive()?.id ?: 0L
        val multiCommunitites =
            multiCommunityRepository
                .getAll(accountId)
                .let {
                    if (searchText.isNotEmpty()) {
                        it.filter { c ->
                            c.name.contains(searchText, ignoreCase = true)
                        }
                    } else {
                        it
                    }
                }.sortedBy { it.name }

        updateState {
            it.copy(
                refreshing = true,
                initial = initial,
                canFetchMore = true,
                multiCommunities = multiCommunitites,
            )
        }
        loadNextPage()
    }

    private fun unsubscribe(community: CommunityModel) {
        screenModelScope.launch {
            val auth = identityRepository.authToken.value
            communityRepository.unsubscribe(
                auth = auth,
                id = community.id,
            )
            updateState {
                it.copy(communities = it.communities.filter { c -> c.id != community.id })
            }
        }
    }

    private fun deleteMultiCommunity(community: MultiCommunityModel) {
        screenModelScope.launch {
            multiCommunityRepository.delete(community)
            updateState {
                val newCommunities = it.multiCommunities.filter { c -> c.id != community.id }
                it.copy(multiCommunities = newCommunities)
            }
        }
    }

    private fun handleMultiCommunityCreated(community: MultiCommunityModel) {
        screenModelScope.launch {
            val oldCommunities = uiState.value.multiCommunities
            val newCommunities =
                if (oldCommunities.any { it.id == community.id }) {
                    oldCommunities.map {
                        if (it.id == community.id) {
                            community
                        } else {
                            it
                        }
                    }
                } else {
                    oldCommunities + community
                }.sortedBy { it.name }
            updateState { it.copy(multiCommunities = newCommunities) }
        }
    }

    private fun toggleFavorite(community: CommunityModel) {
        val communityId = community.id
        screenModelScope.launch {
            val accountId = accountRepository.getActive()?.id ?: 0L
            val newValue = !community.favorite
            if (newValue) {
                val model = FavoriteCommunityModel(communityId = communityId)
                favoriteCommunityRepository.create(model, accountId)
                notificationCenter.send(NotificationCenterEvent.FavoritesUpdated)
            } else {
                favoriteCommunityRepository.getBy(accountId, communityId)?.also { toDelete ->
                    favoriteCommunityRepository.delete(accountId, toDelete)
                    notificationCenter.send(NotificationCenterEvent.FavoritesUpdated)
                }
            }
            val newCommunity = community.copy(favorite = newValue)
            handleCommunityUpdate(newCommunity)
            emitEffect(Effect.Success)
        }
    }

    private fun handleCommunityUpdate(community: CommunityModel) {
        screenModelScope.launch {
            updateState {
                it.copy(
                    communities =
                    it.communities.map { c ->
                        if (c.id == community.id) {
                            community
                        } else {
                            c
                        }
                    },
                )
            }
        }
    }

    private fun updateSearchText(value: String) {
        screenModelScope.launch {
            updateState { it.copy(searchText = value) }
        }
    }

    private suspend fun loadNextPage() {
        val currentState = uiState.value
        if (!currentState.canFetchMore || currentState.loading) {
            updateState { it.copy(refreshing = false) }
            return
        }
        val accountId = accountRepository.getActive()?.id
        val favoriteCommunityIds =
            favoriteCommunityRepository.getAll(accountId).map { it.communityId }
        val itemsToAdd =
            communityPaginationManager
                .loadNextPage()
                .map {
                    val favorite = favoriteCommunityIds.contains(it.id)
                    it.copy(favorite = favorite)
                }
        updateState {
            it.copy(
                refreshing = false,
                communities = itemsToAdd,
                canFetchMore = communityPaginationManager.canFetchMore,
                loading = false,
                initial = false,
            )
        }
    }
}
