package com.livefast.eattrash.raccoonforlemmy.unit.communitydetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.ThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.FavoriteCommunityModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.UserTagType
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.AccountRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.CommunityPreferredLanguageRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.CommunitySortRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.FavoriteCommunityRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.UserTagRepository
import com.livefast.eattrash.raccoonforlemmy.core.utils.imageload.ImagePreloadManager
import com.livefast.eattrash.raccoonforlemmy.core.utils.share.ShareHelper
import com.livefast.eattrash.raccoonforlemmy.core.utils.vibrate.HapticFeedback
import com.livefast.eattrash.raccoonforlemmy.core.utils.zombiemode.ZombieModeHelper
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.ApiConfigurationRepository
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommunityVisibilityType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SortType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.containsId
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.imageUrl
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.readableHandle
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toInt
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toSortType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.PostNavigationManager
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.PostPaginationManager
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.PostPaginationSpecification
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.CommunityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.LemmyItemCache
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.LemmyValueCache
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.PostRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.SiteRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.usecase.GetSortTypesUseCase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class CommunityDetailViewModel(
    private val communityId: Long,
    private val otherInstance: String,
    private val identityRepository: IdentityRepository,
    private val apiConfigurationRepository: ApiConfigurationRepository,
    private val postPaginationManager: PostPaginationManager,
    private val communityRepository: CommunityRepository,
    private val postRepository: PostRepository,
    private val siteRepository: SiteRepository,
    private val themeRepository: ThemeRepository,
    private val settingsRepository: SettingsRepository,
    private val accountRepository: AccountRepository,
    private val favoriteCommunityRepository: FavoriteCommunityRepository,
    private val shareHelper: ShareHelper,
    private val hapticFeedback: HapticFeedback,
    private val zombieModeHelper: ZombieModeHelper,
    private val imagePreloadManager: ImagePreloadManager,
    private val getSortTypesUseCase: GetSortTypesUseCase,
    private val notificationCenter: NotificationCenter,
    private val itemCache: LemmyItemCache,
    private val communitySortRepository: CommunitySortRepository,
    private val postNavigationManager: PostNavigationManager,
    private val communityPreferredLanguageRepository: CommunityPreferredLanguageRepository,
    private val userTagRepository: UserTagRepository,
    private val lemmyValueCache: LemmyValueCache,
) : ViewModel(),
    MviModelDelegate<CommunityDetailMviModel.Intent, CommunityDetailMviModel.UiState, CommunityDetailMviModel.Effect>
    by DefaultMviModelDelegate(initialState = CommunityDetailMviModel.UiState()),
    CommunityDetailMviModel {
    private var hideReadPosts = false

    init {
        viewModelScope.launch {
            if (uiState.value.community.id == 0L) {
                val community = itemCache.getCommunity(communityId) ?: CommunityModel()
                updateState {
                    it.copy(
                        community = community,
                        instance =
                        otherInstance.takeIf { n -> n.isNotEmpty() }
                            ?: apiConfigurationRepository.instance.value,
                    )
                }
            }

            themeRepository.postLayout
                .onEach { layout ->
                    updateState { it.copy(postLayout = layout) }
                }.launchIn(this)
            identityRepository.isLogged
                .onEach { logged ->
                    updateState { it.copy(isLogged = logged ?: false) }
                    updateAvailableSortTypes()
                }.launchIn(this)
            combine(
                identityRepository.isLogged.map { it == true },
                settingsRepository.currentSettings.map { it.enableSwipeActions },
            ) { logged, swipeActionsEnabled ->
                logged && swipeActionsEnabled && otherInstance.isEmpty()
            }.onEach { value ->
                updateState { it.copy(swipeActionsEnabled = value) }
            }.launchIn(this)

            settingsRepository.currentSettings
                .onEach { settings ->
                    updateState {
                        it.copy(
                            blurNsfw = settings.blurNsfw,
                            doubleTapActionEnabled = settings.enableDoubleTapAction,
                            fullHeightImages = settings.fullHeightImages,
                            fullWidthImages = settings.fullWidthImages,
                            voteFormat = settings.voteFormat,
                            autoLoadImages = settings.autoLoadImages,
                            preferNicknames = settings.preferUserNicknames,
                            actionsOnSwipeToStartPosts = settings.actionsOnSwipeToStartPosts,
                            actionsOnSwipeToEndPosts = settings.actionsOnSwipeToEndPosts,
                            showScores = settings.showScores,
                            fadeReadPosts = settings.fadeReadPosts,
                            showUnreadComments = settings.showUnreadComments,
                        )
                    }
                }.launchIn(this)

            zombieModeHelper.index
                .onEach { index ->
                    if (uiState.value.zombieModeActive) {
                        emitEffect(CommunityDetailMviModel.Effect.ZombieModeTick(index))
                    }
                }.launchIn(this)

            notificationCenter
                .subscribe(NotificationCenterEvent.PostUpdated::class)
                .onEach { evt ->
                    handlePostUpdate(evt.model)
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.PostRemoved::class)
                .onEach { evt ->
                    handlePostDelete(evt.model.id)
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.UserBannedPost::class)
                .onEach { evt ->
                    val postId = evt.postId
                    val newUser = evt.user
                    updateState {
                        it.copy(
                            posts =
                            it.posts.map { p ->
                                if (p.id == postId) {
                                    p.copy(
                                        creator = newUser,
                                        updateDate = newUser.updateDate,
                                    )
                                } else {
                                    p
                                }
                            },
                        )
                    }
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.ChangeSortType::class)
                .onEach { evt ->
                    val communityHandle = uiState.value.community.readableHandle
                    if (evt.screenKey == communityHandle) {
                        if (evt.saveAsDefault) {
                            communitySortRepository.save(
                                handle = communityHandle,
                                value = evt.value.toInt(),
                            )
                        }
                        applySortType(evt.value)
                    }
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.Share::class)
                .onEach { evt ->
                    shareHelper.share(evt.url)
                }.launchIn(this)

            uiState
                .map { it.searchText }
                .distinctUntilChanged()
                .drop(1)
                .debounce(1_000)
                .onEach {
                    if (!uiState.value.initial) {
                        updateState { it.copy(loading = false) }
                        emitEffect(CommunityDetailMviModel.Effect.BackToTop)
                        delay(50)
                        refresh()
                    }
                }.launchIn(this)

            lemmyValueCache.isCurrentUserAdmin
                .onEach { value ->
                    updateState {
                        it.copy(
                            isAdmin = value,
                        )
                    }
                }.launchIn(this)
            lemmyValueCache.isDownVoteEnabled
                .onEach { value ->
                    updateState {
                        it.copy(
                            downVoteEnabled = value,
                        )
                    }
                }.launchIn(this)

            if (uiState.value.initial) {
                val communityHandle = uiState.value.community.readableHandle
                val defaultPostSortType =
                    settingsRepository.currentSettings.value.defaultPostSortType
                        .toSortType()
                val customPostSortType =
                    communitySortRepository.get(communityHandle)?.toSortType()
                val preferredLanguageId = communityPreferredLanguageRepository.get(communityHandle)
                val auth = identityRepository.authToken.value.orEmpty()
                val languages = siteRepository.getLanguages(auth)
                updateState {
                    it.copy(
                        sortType = customPostSortType ?: defaultPostSortType,
                        currentPreferredLanguageId = preferredLanguageId,
                        availableLanguages = languages,
                        currentUserId = identityRepository.cachedUser?.id ?: 0,
                    )
                }
                refresh(initial = true)
            }
        }
    }

    private suspend fun updateAvailableSortTypes() {
        val sortTypes = getSortTypesUseCase.getTypesForPosts(otherInstance = otherInstance)
        updateState { it.copy(availableSortTypes = sortTypes) }
    }

    override fun reduce(intent: CommunityDetailMviModel.Intent) {
        when (intent) {
            CommunityDetailMviModel.Intent.LoadNextPage ->
                viewModelScope.launch {
                    loadNextPage()
                }

            CommunityDetailMviModel.Intent.Refresh ->
                viewModelScope.launch {
                    refresh()
                }

            is CommunityDetailMviModel.Intent.DownVotePost ->
                uiState.value.posts.firstOrNull { it.id == intent.id }?.also { post ->
                    if (intent.feedback) {
                        hapticFeedback.vibrate()
                    }
                    toggleDownVotePost(post)
                }

            is CommunityDetailMviModel.Intent.Share -> shareHelper.share(intent.url)
            is CommunityDetailMviModel.Intent.SavePost ->
                uiState.value.posts.firstOrNull { it.id == intent.id }?.also { post ->
                    if (intent.feedback) {
                        hapticFeedback.vibrate()
                    }
                    toggleSavePost(post)
                }

            is CommunityDetailMviModel.Intent.UpVotePost ->
                uiState.value.posts.firstOrNull { it.id == intent.id }?.also { post ->
                    if (intent.feedback) {
                        hapticFeedback.vibrate()
                    }
                    toggleUpVotePost(post)
                }

            CommunityDetailMviModel.Intent.HapticIndication -> hapticFeedback.vibrate()
            CommunityDetailMviModel.Intent.Subscribe -> subscribe()
            CommunityDetailMviModel.Intent.Unsubscribe -> unsubscribe()
            is CommunityDetailMviModel.Intent.DeletePost -> handlePostDelete(intent.id)
            CommunityDetailMviModel.Intent.Block -> blockCommunity()
            CommunityDetailMviModel.Intent.BlockInstance -> blockInstance()
            is CommunityDetailMviModel.Intent.MarkAsRead ->
                viewModelScope.launch {
                    markAsRead(uiState.value.posts.first { it.id == intent.id })
                }

            CommunityDetailMviModel.Intent.ClearRead -> clearRead()
            is CommunityDetailMviModel.Intent.Hide ->
                uiState.value.posts.firstOrNull { it.id == intent.id }?.also { post ->
                    hide(post)
                }

            CommunityDetailMviModel.Intent.PauseZombieMode ->
                viewModelScope.launch {
                    updateState { it.copy(zombieModeActive = false) }
                    zombieModeHelper.pause()
                }

            is CommunityDetailMviModel.Intent.StartZombieMode ->
                viewModelScope.launch {
                    updateState { it.copy(zombieModeActive = true) }
                    zombieModeHelper.start(
                        initialValue = intent.index,
                        interval = settingsRepository.currentSettings.value.zombieModeInterval,
                    )
                }

            is CommunityDetailMviModel.Intent.ModFeaturePost ->
                uiState.value.posts
                    .firstOrNull { it.id == intent.id }
                    ?.also { post ->
                        feature(post)
                    }

            is CommunityDetailMviModel.Intent.AdminFeaturePost ->
                uiState.value.posts
                    .firstOrNull { it.id == intent.id }
                    ?.also { post ->
                        featureLocal(post)
                    }

            is CommunityDetailMviModel.Intent.ModLockPost ->
                uiState.value.posts
                    .firstOrNull { it.id == intent.id }
                    ?.also { post ->
                        lock(post)
                    }

            is CommunityDetailMviModel.Intent.ModToggleModUser -> toggleModeratorStatus(intent.id)
            CommunityDetailMviModel.Intent.ToggleFavorite -> toggleFavorite()
            is CommunityDetailMviModel.Intent.ChangeSearching ->
                viewModelScope.launch {
                    updateState { it.copy(searching = intent.value) }
                    if (!intent.value) {
                        updateSearchText("")
                    }
                }

            is CommunityDetailMviModel.Intent.SetSearch -> updateSearchText(intent.value)
            is CommunityDetailMviModel.Intent.WillOpenDetail ->
                viewModelScope.launch {
                    uiState.value.posts
                        .firstOrNull { it.id == intent.id }
                        ?.also { post ->
                            markAsRead(post)
                            val state = postPaginationManager.extractState()
                            postNavigationManager.push(state)
                        }
                }

            CommunityDetailMviModel.Intent.UnhideCommunity -> unhideCommunity()
            is CommunityDetailMviModel.Intent.SelectPreferredLanguage ->
                updatePreferredLanguage(intent.languageId)

            CommunityDetailMviModel.Intent.DeleteCommunity -> deleteCommunity()
            is CommunityDetailMviModel.Intent.RestorePost -> restorePost(intent.id)
            is CommunityDetailMviModel.Intent.ToggleRead ->
                viewModelScope.launch {
                    uiState.value.posts.firstOrNull { it.id == intent.id }?.also { post ->
                        setRead(post = post, read = !post.read)
                    }
                }
        }
    }

    private suspend fun refresh(initial: Boolean = false) {
        hideReadPosts = false
        val currentState = uiState.value
        zombieModeHelper.pause()
        postPaginationManager.reset(
            PostPaginationSpecification.Community(
                id = currentState.community.id,
                sortType = currentState.sortType,
                name = currentState.community.name,
                otherInstance = otherInstance,
                query = currentState.searchText.takeIf { currentState.searching },
                includeNsfw = settingsRepository.currentSettings.value.includeNsfw,
            ),
        )
        updateState {
            it.copy(
                canFetchMore = true,
                refreshing = !initial,
                initial = initial,
            )
        }
        val auth = identityRepository.authToken.value
        val accountId = accountRepository.getActive()?.id
        val isFavorite =
            favoriteCommunityRepository.getBy(
                accountId = accountId,
                communityId = currentState.community.id,
            ) != null
        val refreshedCommunity =
            communityRepository
                .get(
                    auth = auth,
                    name = currentState.community.name,
                    id = currentState.community.id,
                    instance = otherInstance,
                )?.copy(favorite = isFavorite)
        val moderators =
            communityRepository.getModerators(
                auth = auth,
                id = currentState.community.id,
            )
        if (refreshedCommunity != null) {
            val newNotices = currentState.notices.toMutableList()
            if (refreshedCommunity.visibilityType == CommunityVisibilityType.LocalOnly &&
                newNotices.none { it == CommunityNotices.LocalOnlyVisibility }
            ) {
                newNotices += CommunityNotices.LocalOnlyVisibility
            }
            if (refreshedCommunity.currentlyBanned && newNotices.none { it == CommunityNotices.BannedUser }) {
                newNotices += CommunityNotices.BannedUser
            }
            updateState {
                it.copy(
                    community = refreshedCommunity,
                    moderators = moderators,
                    loading = false,
                    zombieModeActive = false,
                    notices = newNotices,
                )
            }
        }
        val botTagColor =
            userTagRepository.getSpecialTagColor(
                accountId = accountId ?: 0,
                type = UserTagType.Bot,
            )
        val meTagColor =
            userTagRepository.getSpecialTagColor(
                accountId = accountId ?: 0,
                type = UserTagType.Me,
            )
        updateState {
            it.copy(botTagColor = botTagColor, meTagColor = meTagColor)
        }

        loadNextPage()
    }

    private fun applySortType(value: SortType) {
        if (uiState.value.sortType == value) {
            return
        }
        viewModelScope.launch {
            updateState { it.copy(sortType = value) }
            emitEffect(CommunityDetailMviModel.Effect.BackToTop)
            delay(50)
            refresh()
        }
    }

    private suspend fun loadNextPage() {
        val currentState = uiState.value
        if (!currentState.canFetchMore || currentState.loading) {
            updateState { it.copy(refreshing = false) }
            return
        }
        updateState { it.copy(loading = true) }
        val posts =
            postPaginationManager
                .loadNextPage()
                .let {
                    if (!hideReadPosts) {
                        it
                    } else {
                        it.filter { post -> !post.read }
                    }
                }.let {
                    if (currentState.searching) {
                        it.filter { post ->
                            listOf(post.title, post.text).any { s ->
                                s.contains(
                                    other = currentState.searchText,
                                    ignoreCase = true,
                                )
                            }
                        }
                    } else {
                        it
                    }
                }
        if (uiState.value.autoLoadImages) {
            posts.forEach { post ->
                post.imageUrl.takeIf { i -> i.isNotEmpty() }?.also { url ->
                    imagePreloadManager.preload(url)
                }
            }
        }
        updateState {
            it.copy(
                posts = posts,
                loading = false,
                canFetchMore = postPaginationManager.canFetchMore,
                refreshing = false,
                initial = false,
            )
        }
    }

    private fun toggleUpVotePost(post: PostModel) {
        val newValue = post.myVote <= 0
        val newPost =
            postRepository.asUpVoted(
                post = post,
                voted = newValue,
            )
        val shouldBeMarkedAsRead = settingsRepository.currentSettings.value.markAsReadOnInteraction
        handlePostUpdate(newPost)
        viewModelScope.launch {
            try {
                val auth = identityRepository.authToken.value.orEmpty()
                postRepository.upVote(
                    auth = auth,
                    post = post,
                    voted = newValue,
                )
                if (shouldBeMarkedAsRead) {
                    markAsRead(newPost)
                } else {
                    handlePostUpdate(newPost)
                }
                notificationCenter.send(
                    event = NotificationCenterEvent.PostUpdated(newPost),
                )
            } catch (e: Throwable) {
                e.printStackTrace()
                handlePostUpdate(post)
            }
        }
    }

    private suspend fun markAsRead(post: PostModel) {
        if (post.read) {
            return
        }
        setRead(post = post, read = true)
    }

    private suspend fun setRead(post: PostModel, read: Boolean) {
        val newPost = post.copy(read = read)
        try {
            val auth = identityRepository.authToken.value.orEmpty()
            postRepository.setRead(
                read = read,
                postId = post.id,
                auth = auth,
            )
            handlePostUpdate(newPost)
        } catch (e: Throwable) {
            e.printStackTrace()
            handlePostUpdate(post)
        }
    }

    private fun toggleDownVotePost(post: PostModel) {
        val newValue = post.myVote >= 0
        val newPost =
            postRepository.asDownVoted(
                post = post,
                downVoted = newValue,
            )
        val shouldBeMarkedAsRead = settingsRepository.currentSettings.value.markAsReadOnInteraction
        handlePostUpdate(newPost)
        viewModelScope.launch {
            try {
                val auth = identityRepository.authToken.value.orEmpty()
                postRepository.downVote(
                    auth = auth,
                    post = post,
                    downVoted = newValue,
                )
                if (shouldBeMarkedAsRead) {
                    markAsRead(newPost)
                } else {
                    handlePostUpdate(newPost)
                }
                notificationCenter.send(
                    event = NotificationCenterEvent.PostUpdated(newPost),
                )
            } catch (e: Throwable) {
                e.printStackTrace()
                handlePostUpdate(post)
            }
        }
    }

    private fun toggleSavePost(post: PostModel) {
        val newValue = !post.saved
        val newPost =
            postRepository.asSaved(
                post = post,
                saved = newValue,
            )
        val shouldBeMarkedAsRead = settingsRepository.currentSettings.value.markAsReadOnInteraction
        handlePostUpdate(newPost)
        viewModelScope.launch {
            try {
                val auth = identityRepository.authToken.value.orEmpty()
                postRepository.save(
                    auth = auth,
                    post = post,
                    saved = newValue,
                )
                if (shouldBeMarkedAsRead) {
                    markAsRead(newPost)
                } else {
                    handlePostUpdate(newPost)
                }
                notificationCenter.send(
                    event = NotificationCenterEvent.PostUpdated(newPost),
                )
            } catch (e: Throwable) {
                e.printStackTrace()
                handlePostUpdate(post)
            }
        }
    }

    private fun subscribe() {
        hapticFeedback.vibrate()
        viewModelScope.launch {
            val community =
                communityRepository.subscribe(
                    auth = identityRepository.authToken.value,
                    id = communityId,
                )
            if (community != null) {
                updateState { it.copy(community = community) }
                notificationCenter.send(
                    NotificationCenterEvent.CommunitySubscriptionChanged(
                        community,
                    ),
                )
            }
        }
    }

    private fun unsubscribe() {
        hapticFeedback.vibrate()
        viewModelScope.launch {
            val community =
                communityRepository.unsubscribe(
                    auth = identityRepository.authToken.value,
                    id = communityId,
                )
            if (community != null) {
                updateState { it.copy(community = community) }
                notificationCenter.send(
                    NotificationCenterEvent.CommunitySubscriptionChanged(
                        community,
                    ),
                )
            }
        }
    }

    private fun handlePostUpdate(post: PostModel) {
        viewModelScope.launch {
            updateState {
                it.copy(
                    posts =
                    it.posts.map { p ->
                        if (p.id == post.id) {
                            post
                        } else {
                            p
                        }
                    },
                )
            }
        }
    }

    private fun handlePostDelete(id: Long) {
        viewModelScope.launch {
            updateState { it.copy(posts = it.posts.filter { post -> post.id != id }) }
        }
    }

    private fun blockCommunity() {
        viewModelScope.launch {
            updateState { it.copy(asyncInProgress = true) }
            try {
                val auth = identityRepository.authToken.value
                communityRepository.block(
                    id = communityId,
                    blocked = true,
                    auth = auth,
                )
                emitEffect(CommunityDetailMviModel.Effect.Success)
            } catch (e: Throwable) {
                emitEffect(CommunityDetailMviModel.Effect.Error(e.message))
            } finally {
                updateState { it.copy(asyncInProgress = false) }
            }
        }
    }

    private fun blockInstance() {
        viewModelScope.launch {
            updateState { it.copy(asyncInProgress = true) }
            try {
                val community = uiState.value.community
                val instanceId = community.instanceId
                val auth = identityRepository.authToken.value
                siteRepository.block(instanceId, true, auth)
                emitEffect(CommunityDetailMviModel.Effect.Success)
            } catch (e: Throwable) {
                emitEffect(CommunityDetailMviModel.Effect.Error(e.message))
            } finally {
                updateState { it.copy(asyncInProgress = false) }
            }
        }
    }

    private fun clearRead() {
        viewModelScope.launch {
            hideReadPosts = true
            updateState {
                val newPosts = it.posts.filter { e -> !e.read }
                it.copy(
                    posts = newPosts,
                )
            }
        }
    }

    private fun hide(post: PostModel) {
        viewModelScope.launch {
            try {
                val auth = identityRepository.authToken.value.orEmpty()
                postRepository.hide(
                    hidden = true,
                    postId = post.id,
                    auth = auth,
                )
                updateState {
                    val newPosts = it.posts.filter { e -> e.id != post.id }
                    it.copy(
                        posts = newPosts,
                    )
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

    private fun feature(post: PostModel) {
        viewModelScope.launch {
            val auth = identityRepository.authToken.value.orEmpty()
            val newPost =
                postRepository.featureInCommunity(
                    postId = post.id,
                    auth = auth,
                    featured = !post.featuredCommunity,
                )
            if (newPost != null) {
                handlePostUpdate(newPost)
            }
        }
    }

    private fun featureLocal(post: PostModel) {
        viewModelScope.launch {
            val auth = identityRepository.authToken.value.orEmpty()
            val newPost =
                postRepository.featureInInstance(
                    postId = post.id,
                    auth = auth,
                    featured = !post.featuredLocal,
                )
            if (newPost != null) {
                handlePostUpdate(newPost)
            }
        }
    }

    private fun lock(post: PostModel) {
        viewModelScope.launch {
            val auth = identityRepository.authToken.value.orEmpty()
            val newPost =
                postRepository.lock(
                    postId = post.id,
                    auth = auth,
                    locked = !post.locked,
                )
            if (newPost != null) {
                handlePostUpdate(newPost)
            }
        }
    }

    private fun toggleModeratorStatus(userId: Long) {
        viewModelScope.launch {
            val isModerator = uiState.value.moderators.containsId(userId)
            val auth = identityRepository.authToken.value.orEmpty()
            val newModerators =
                communityRepository.addModerator(
                    auth = auth,
                    communityId = communityId,
                    added = !isModerator,
                    userId = userId,
                )
            updateState {
                it.copy(moderators = newModerators)
            }
        }
    }

    private fun toggleFavorite() {
        viewModelScope.launch {
            val accountId = accountRepository.getActive()?.id ?: 0L
            val newValue = !uiState.value.community.favorite
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
            val newCommunity = uiState.value.community.copy(favorite = newValue)
            updateState { it.copy(community = newCommunity) }
            emitEffect(CommunityDetailMviModel.Effect.Success)
        }
    }

    private fun updateSearchText(value: String) {
        viewModelScope.launch {
            updateState { it.copy(searchText = value) }
        }
    }

    private fun unhideCommunity() {
        viewModelScope.launch {
            val auth = identityRepository.authToken.value
            try {
                communityRepository.hide(
                    auth = auth,
                    communityId = communityId,
                    hidden = false,
                )
                emitEffect(CommunityDetailMviModel.Effect.Success)
            } catch (e: Throwable) {
                emitEffect(CommunityDetailMviModel.Effect.Failure(e.message))
            }
        }
    }

    private fun updatePreferredLanguage(languageId: Long?) {
        viewModelScope.launch {
            val communityHandle = uiState.value.community.readableHandle
            communityPreferredLanguageRepository.save(handle = communityHandle, value = languageId)
            updateState {
                it.copy(currentPreferredLanguageId = languageId)
            }
        }
    }

    private fun deleteCommunity() {
        viewModelScope.launch {
            val auth = identityRepository.authToken.value.orEmpty()
            try {
                communityRepository.delete(
                    auth = auth,
                    communityId = communityId,
                )
                emitEffect(CommunityDetailMviModel.Effect.Back)
            } catch (e: Exception) {
                emitEffect(CommunityDetailMviModel.Effect.Failure(e.message))
            }
        }
    }

    private fun restorePost(id: Long) {
        viewModelScope.launch {
            val auth = identityRepository.authToken.value.orEmpty()
            val newPost =
                postRepository.restore(
                    id = id,
                    auth = auth,
                )
            if (newPost != null) {
                handlePostUpdate(newPost)
            }
        }
    }
}
