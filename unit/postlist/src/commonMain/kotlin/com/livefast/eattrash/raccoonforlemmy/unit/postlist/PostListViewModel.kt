package com.livefast.eattrash.raccoonforlemmy.unit.postlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.ThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.UserTagType
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.AccountRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.UserTagRepository
import com.livefast.eattrash.raccoonforlemmy.core.utils.imageload.ImagePreloadManager
import com.livefast.eattrash.raccoonforlemmy.core.utils.share.ShareHelper
import com.livefast.eattrash.raccoonforlemmy.core.utils.vibrate.HapticFeedback
import com.livefast.eattrash.raccoonforlemmy.core.utils.zombiemode.ZombieModeHelper
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.ApiConfigurationRepository
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.ListingType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SortType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.imageUrl
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toListingType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toSortType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.PostNavigationManager
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.PostPaginationManager
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.PostPaginationSpecification
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.CommunityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.LemmyValueCache
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.PostRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.SiteRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.UserRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.usecase.GetSortTypesUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class PostListViewModel(
    private val postPaginationManager: PostPaginationManager,
    private val postRepository: PostRepository,
    private val apiConfigurationRepository: ApiConfigurationRepository,
    private val identityRepository: IdentityRepository,
    private val siteRepository: SiteRepository,
    private val themeRepository: ThemeRepository,
    private val shareHelper: ShareHelper,
    private val settingsRepository: SettingsRepository,
    private val userRepository: UserRepository,
    private val communityRepository: CommunityRepository,
    private val accountRepository: AccountRepository,
    private val userTagRepository: UserTagRepository,
    private val notificationCenter: NotificationCenter,
    private val hapticFeedback: HapticFeedback,
    private val zombieModeHelper: ZombieModeHelper,
    private val imagePreloadManager: ImagePreloadManager,
    private val getSortTypesUseCase: GetSortTypesUseCase,
    private val postNavigationManager: PostNavigationManager,
    private val lemmyValueCache: LemmyValueCache,
) : ViewModel(),
    MviModelDelegate<PostListMviModel.Intent, PostListMviModel.UiState, PostListMviModel.Effect>
    by DefaultMviModelDelegate(initialState = PostListMviModel.UiState()),
    PostListMviModel {
    private var hideReadPosts = false

    init {
        viewModelScope.launch {
            apiConfigurationRepository.instance
                .onEach { instance ->
                    updateState {
                        it.copy(instance = instance)
                    }
                }.launchIn(this)

            identityRepository.isLogged
                .onEach { logged ->
                    refreshUser()
                    updateState { it.copy(isLogged = logged ?: false) }
                    updateAvailableSortTypes()
                    onFirstLoad()
                }.launchIn(this)
            combine(
                identityRepository.isLogged.map { it == true },
                settingsRepository.currentSettings.map { it.enableSwipeActions },
            ) { logged, swipeActionsEnabled ->
                logged && swipeActionsEnabled
            }.onEach { value ->
                updateState { it.copy(swipeActionsEnabled = value) }
            }.launchIn(this)
            themeRepository.postLayout
                .onEach { layout ->
                    updateState { it.copy(postLayout = layout) }
                }.launchIn(this)

            settingsRepository.currentSettings
                .onEach { settings ->
                    updateState {
                        it.copy(
                            blurNsfw = settings.blurNsfw,
                            doubleTapActionEnabled = settings.enableDoubleTapAction,
                            voteFormat = settings.voteFormat,
                            autoLoadImages = settings.autoLoadImages,
                            preferNicknames = settings.preferUserNicknames,
                            fullHeightImages = settings.fullHeightImages,
                            fullWidthImages = settings.fullWidthImages,
                            actionsOnSwipeToStartPosts = settings.actionsOnSwipeToStartPosts,
                            actionsOnSwipeToEndPosts = settings.actionsOnSwipeToEndPosts,
                            showScores = settings.showScores,
                            fadeReadPosts = settings.fadeReadPosts,
                            showUnreadComments = settings.showUnreadComments,
                        )
                    }
                }.launchIn(this)

            notificationCenter
                .subscribe(NotificationCenterEvent.PostUpdated::class)
                .onEach { evt ->
                    if (evt.model.deleted) {
                        handlePostDelete(evt.model)
                    } else {
                        handlePostUpdate(evt.model)
                    }
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.ChangeFeedType::class)
                .onEach { evt ->
                    if (evt.screenKey == "postList") {
                        applyListingType(evt.value)
                    }
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.ChangeSortType::class)
                .onEach { evt ->
                    if (evt.screenKey == "postList") {
                        applySortType(evt.value)
                    }
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.Logout::class)
                .onEach {
                    handleLogout()
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.InstanceSelected::class)
                .onEach {
                    refresh(initial = true)
                    delay(100)
                    emitEffect(PostListMviModel.Effect.BackToTop)
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.BlockActionSelected::class)
                .onEach { evt ->
                    val userId = evt.userId
                    val communityId = evt.communityId
                    val instanceId = evt.instanceId
                    when {
                        userId != null -> blockUser(userId)
                        communityId != null -> blockCommunity(communityId)
                        instanceId != null -> blockInstance(instanceId)
                    }
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.Share::class)
                .onEach { evt ->
                    shareHelper.share(evt.url)
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.ResetHome::class)
                .onEach {
                    onFirstLoad()
                }.launchIn(this)

            zombieModeHelper.index
                .onEach { index ->
                    if (uiState.value.zombieModeActive) {
                        emitEffect(PostListMviModel.Effect.ZombieModeTick(index))
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
        }

        if (uiState.value.initial) {
            onFirstLoad()
        }
    }

    private fun onFirstLoad() {
        viewModelScope.launch {
            val settings = settingsRepository.currentSettings.value
            updateState {
                it.copy(
                    listingType = settings.defaultListingType.toListingType(),
                    sortType = settings.defaultPostSortType.toSortType(),
                )
            }
            refreshUser()
            refresh(initial = true)
            emitEffect(PostListMviModel.Effect.BackToTop)
        }
    }

    private suspend fun refreshUser() {
        val auth = identityRepository.authToken.value.orEmpty()
        val user = siteRepository.getCurrentUser(auth)
        updateState {
            it.copy(currentUserId = user?.id ?: 0)
        }
    }

    private suspend fun updateAvailableSortTypes() {
        val sortTypes = getSortTypesUseCase.getTypesForPosts()
        updateState { it.copy(availableSortTypes = sortTypes) }
    }

    override fun reduce(intent: PostListMviModel.Intent) {
        when (intent) {
            PostListMviModel.Intent.LoadNextPage ->
                viewModelScope.launch {
                    loadNextPage()
                }

            is PostListMviModel.Intent.Refresh ->
                viewModelScope.launch {
                    if (intent.hardReset) {
                        refreshUser()
                    }
                    refresh()
                }

            is PostListMviModel.Intent.ChangeListing -> applyListingType(intent.value)
            is PostListMviModel.Intent.DownVotePost ->
                uiState.value.posts.firstOrNull { it.id == intent.id }?.also { post ->
                    if (intent.feedback) {
                        hapticFeedback.vibrate()
                    }
                    toggleDownVote(post)
                }

            is PostListMviModel.Intent.SavePost ->
                uiState.value.posts.firstOrNull { it.id == intent.id }?.also { post ->
                    if (intent.feedback) {
                        hapticFeedback.vibrate()
                    }
                    toggleSave(post)
                }

            is PostListMviModel.Intent.UpVotePost ->
                uiState.value.posts.firstOrNull { it.id == intent.id }?.also { post ->
                    if (intent.feedback) {
                        hapticFeedback.vibrate()
                    }
                    toggleUpVote(post)
                }

            PostListMviModel.Intent.HapticIndication -> hapticFeedback.vibrate()
            is PostListMviModel.Intent.DeletePost -> deletePost(intent.id)
            is PostListMviModel.Intent.Share -> shareHelper.share(intent.url)

            is PostListMviModel.Intent.MarkAsRead ->
                viewModelScope.launch {
                    uiState.value.posts.firstOrNull { it.id == intent.id }?.also { post ->
                        markAsRead(post)
                    }
                }

            PostListMviModel.Intent.ClearRead -> clearRead()
            is PostListMviModel.Intent.Hide ->
                uiState.value.posts.firstOrNull { it.id == intent.id }?.also { post ->
                    hide(post)
                }

            PostListMviModel.Intent.PauseZombieMode ->
                viewModelScope.launch {
                    updateState { it.copy(zombieModeActive = false) }
                    zombieModeHelper.pause()
                }

            is PostListMviModel.Intent.StartZombieMode ->
                viewModelScope.launch {
                    updateState { it.copy(zombieModeActive = true) }
                    zombieModeHelper.start(
                        initialValue = intent.index,
                        interval = settingsRepository.currentSettings.value.zombieModeInterval,
                    )
                }

            is PostListMviModel.Intent.WillOpenDetail ->
                viewModelScope.launch {
                    uiState.value.posts.firstOrNull { it.id == intent.id }?.also { post ->
                        markAsRead(post)
                        val state = postPaginationManager.extractState()
                        postNavigationManager.push(state)
                        emitEffect(PostListMviModel.Effect.OpenDetail(post))
                    }
                }

            is PostListMviModel.Intent.ToggleRead ->
                viewModelScope.launch {
                    uiState.value.posts.firstOrNull { it.id == intent.id }?.also { post ->
                        setRead(post = post, read = !post.read)
                    }
                }
        }
    }

    private suspend fun refresh(initial: Boolean = false) {
        hideReadPosts = false
        val listingType = uiState.value.listingType ?: return
        val sortType = uiState.value.sortType ?: return
        zombieModeHelper.pause()
        postPaginationManager.reset(
            PostPaginationSpecification.Listing(
                listingType = listingType,
                sortType = sortType,
                includeNsfw = settingsRepository.currentSettings.value.includeNsfw,
            ),
        )
        updateState {
            it.copy(
                initial = initial,
                canFetchMore = true,
                refreshing = !initial,
                loading = false,
                zombieModeActive = false,
            )
        }
        val accountId = accountRepository.getActive()?.id
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

    private suspend fun loadNextPage() {
        val currentState = uiState.value
        if (!currentState.canFetchMore || currentState.loading) {
            updateState { it.copy(refreshing = false) }
            return
        }
        updateState { it.copy(loading = true) }
        val posts =
            postPaginationManager.loadNextPage().let {
                if (!hideReadPosts) {
                    it
                } else {
                    it.filter { post -> !post.read }
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
                loading = if (it.initial) posts.isEmpty() else false,
                canFetchMore = postPaginationManager.canFetchMore,
                refreshing = false,
                initial = if (it.initial) posts.isEmpty() else false,
            )
        }
    }

    private fun applySortType(value: SortType) {
        if (uiState.value.sortType == value) {
            return
        }
        viewModelScope.launch {
            updateState { it.copy(sortType = value) }
            emitEffect(PostListMviModel.Effect.BackToTop)
            delay(50)
            refresh()
        }
    }

    private fun applyListingType(value: ListingType) {
        if (uiState.value.listingType == value) {
            return
        }
        viewModelScope.launch {
            updateState { it.copy(listingType = value) }
            emitEffect(PostListMviModel.Effect.BackToTop)
            delay(50)
            refresh()
        }
    }

    private fun toggleUpVote(post: PostModel) {
        val newVote = post.myVote <= 0
        val newPost =
            postRepository.asUpVoted(
                post = post,
                voted = newVote,
            )
        handlePostUpdate(newPost)
        val shouldBeMarkedAsRead = settingsRepository.currentSettings.value.markAsReadOnInteraction
        viewModelScope.launch {
            try {
                val auth = identityRepository.authToken.value.orEmpty()
                postRepository.upVote(
                    post = post,
                    auth = auth,
                    voted = newVote,
                )
                if (shouldBeMarkedAsRead) {
                    markAsRead(newPost)
                } else {
                    handlePostUpdate(newPost)
                }
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

    private fun toggleDownVote(post: PostModel) {
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
                    post = post,
                    auth = auth,
                    downVoted = newValue,
                )
                if (shouldBeMarkedAsRead) {
                    markAsRead(newPost)
                } else {
                    handlePostUpdate(newPost)
                }
            } catch (e: Throwable) {
                e.printStackTrace()
                handlePostUpdate(post)
            }
        }
    }

    private fun toggleSave(post: PostModel) {
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
                    post = post,
                    auth = auth,
                    saved = newValue,
                )
                if (shouldBeMarkedAsRead) {
                    markAsRead(newPost)
                } else {
                    handlePostUpdate(newPost)
                }
            } catch (e: Throwable) {
                e.printStackTrace()
                handlePostUpdate(post)
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

    private fun handleLogout() {
        viewModelScope.launch {
            updateState {
                it.copy(
                    posts = emptyList(),
                    isLogged = false,
                )
            }
            onFirstLoad()
        }
    }

    private fun deletePost(id: Long) {
        viewModelScope.launch {
            val auth = identityRepository.authToken.value.orEmpty()
            val newPost = postRepository.delete(id = id, auth = auth)
            if (newPost != null) {
                handlePostDelete(newPost)
            }
        }
    }

    private fun handlePostDelete(post: PostModel) {
        viewModelScope.launch {
            updateState {
                it.copy(posts = it.posts.filter { p -> p.id != post.id })
            }
        }
    }

    private fun clearRead() {
        viewModelScope.launch {
            hideReadPosts = true
            updateState {
                val newPosts = it.posts.filter { e -> !e.read }
                it.copy(posts = newPosts)
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

    private fun blockUser(userId: Long) {
        viewModelScope.launch {
            runCatching {
                val auth = identityRepository.authToken.value
                userRepository.block(
                    id = userId,
                    blocked = true,
                    auth = auth,
                )
            }
        }
    }

    private fun blockCommunity(communityId: Long) {
        viewModelScope.launch {
            runCatching {
                val auth = identityRepository.authToken.value
                communityRepository.block(
                    id = communityId,
                    blocked = true,
                    auth = auth,
                )
            }
        }
    }

    private fun blockInstance(instanceId: Long) {
        viewModelScope.launch {
            runCatching {
                val auth = identityRepository.authToken.value
                siteRepository.block(
                    id = instanceId,
                    blocked = true,
                    auth = auth,
                )
            }
        }
    }
}
