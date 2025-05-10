package com.livefast.eattrash.raccoonforlemmy.unit.multicommunity.detail

import cafe.adriel.voyager.core.model.screenModelScope
import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.ThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModel
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.MultiCommunityModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.UserTagType
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.AccountRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.MultiCommunityRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.UserTagRepository
import com.livefast.eattrash.raccoonforlemmy.core.utils.imageload.ImagePreloadManager
import com.livefast.eattrash.raccoonforlemmy.core.utils.share.ShareHelper
import com.livefast.eattrash.raccoonforlemmy.core.utils.vibrate.HapticFeedback
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SortType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.imageUrl
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toSortType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.PostNavigationManager
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.PostPaginationManager
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.PostPaginationSpecification
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.usecase.GetSortTypesUseCase
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.LemmyValueCache
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.PostRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.SiteRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MultiCommunityViewModel(
    private val communityId: Long,
    private val postPaginationManager: PostPaginationManager,
    private val postRepository: PostRepository,
    private val identityRepository: IdentityRepository,
    private val multiCommunityRepository: MultiCommunityRepository,
    private val siteRepository: SiteRepository,
    private val themeRepository: ThemeRepository,
    private val shareHelper: ShareHelper,
    private val settingsRepository: SettingsRepository,
    private val accountRepository: AccountRepository,
    private val userTagRepository: UserTagRepository,
    private val notificationCenter: NotificationCenter,
    private val hapticFeedback: HapticFeedback,
    private val imagePreloadManager: ImagePreloadManager,
    private val getSortTypesUseCase: GetSortTypesUseCase,
    private val postNavigationManager: PostNavigationManager,
    private val lemmyValueCache: LemmyValueCache,
) : DefaultMviModel<MultiCommunityMviModel.Intent, MultiCommunityMviModel.UiState, MultiCommunityMviModel.Effect>(
        initialState = MultiCommunityMviModel.UiState(),
    ),
    MultiCommunityMviModel {
    private var hideReadPosts = false

    init {
        screenModelScope.launch {
            if ((uiState.value.community.id ?: 0) == 0L) {
                val community =
                    multiCommunityRepository.getById(communityId) ?: MultiCommunityModel()
                updateState { it.copy(community = community) }
            }
            themeRepository.postLayout
                .onEach { layout ->
                    updateState { it.copy(postLayout = layout) }
                }.launchIn(this)
            combine(
                identityRepository.isLogged.map { it == true },
                settingsRepository.currentSettings.map { it.enableSwipeActions },
            ) { logged, swipeActionsEnabled ->
                logged && swipeActionsEnabled
            }.onEach { value ->
                updateState { it.copy(swipeActionsEnabled = value) }
            }.launchIn(this)
            settingsRepository.currentSettings
                .onEach { settings ->
                    updateState {
                        it.copy(
                            blurNsfw = settings.blurNsfw,
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
                    handlePostUpdate(evt.model)
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.ChangeSortType::class)
                .onEach { evt ->
                    if (evt.screenKey == "multiCommunity") {
                        applySortType(evt.value)
                    }
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.Share::class)
                .onEach { evt ->
                    shareHelper.share(evt.url)
                }.launchIn(this)
            identityRepository.isLogged
                .onEach { logged ->
                    updateState { it.copy(isLogged = logged ?: false) }
                }.launchIn(this)
            lemmyValueCache.isDownVoteEnabled
                .onEach { value ->
                    updateState {
                        it.copy(
                            downVoteEnabled = value,
                        )
                    }
                }.launchIn(this)

            if (uiState.value.currentUserId == null) {
                val auth = identityRepository.authToken.value.orEmpty()
                val user = siteRepository.getCurrentUser(auth)
                updateState {
                    it.copy(
                        currentUserId = user?.id ?: 0,
                    )
                }
            }
            if (uiState.value.initial) {
                val settings = settingsRepository.currentSettings.value
                val sortTypes = getSortTypesUseCase.getTypesForPosts()
                updateState {
                    it.copy(
                        sortType = settings.defaultPostSortType.toSortType(),
                        availableSortTypes = sortTypes,
                    )
                }
                refresh(initial = true)
            }
        }
    }

    override fun reduce(intent: MultiCommunityMviModel.Intent) {
        when (intent) {
            is MultiCommunityMviModel.Intent.DownVotePost ->
                uiState.value.posts.firstOrNull { it.id == intent.id }?.also { post ->
                    if (intent.feedback) {
                        hapticFeedback.vibrate()
                    }
                    toggleDownVote(post)
                }

            MultiCommunityMviModel.Intent.HapticIndication -> hapticFeedback.vibrate()
            MultiCommunityMviModel.Intent.LoadNextPage ->
                screenModelScope.launch {
                    loadNextPage()
                }

            MultiCommunityMviModel.Intent.Refresh ->
                screenModelScope.launch {
                    refresh()
                }

            is MultiCommunityMviModel.Intent.SavePost ->
                uiState.value.posts.firstOrNull { it.id == intent.id }?.also { post ->
                    if (intent.feedback) {
                        hapticFeedback.vibrate()
                    }
                    toggleSave(post)
                }

            is MultiCommunityMviModel.Intent.Share -> shareHelper.share(intent.url)

            is MultiCommunityMviModel.Intent.UpVotePost ->
                uiState.value.posts.firstOrNull { it.id == intent.id }?.also { post ->
                    if (intent.feedback) {
                        hapticFeedback.vibrate()
                    }
                    toggleUpVote(post)
                }

            MultiCommunityMviModel.Intent.ClearRead -> clearRead()
            is MultiCommunityMviModel.Intent.MarkAsRead ->
                screenModelScope.launch {
                    markAsRead(
                        post = uiState.value.posts.first { it.id == intent.id },
                    )
                }

            is MultiCommunityMviModel.Intent.Hide ->
                hide(
                    post = uiState.value.posts.first { it.id == intent.id },
                )

            is MultiCommunityMviModel.Intent.WillOpenDetail ->
                screenModelScope.launch {
                    uiState.value.posts.firstOrNull { it.id == intent.id }?.also { post ->
                        markAsRead(post)
                        val state = postPaginationManager.extractState()
                        postNavigationManager.push(state)
                        emitEffect(MultiCommunityMviModel.Effect.OpenDetail(post))
                    }
                }

            is MultiCommunityMviModel.Intent.ToggleRead ->
                screenModelScope.launch {
                    uiState.value.posts.firstOrNull { it.id == intent.id }?.also { post ->
                        setRead(post = post, read = !post.read)
                    }
                }
        }
    }

    private suspend fun refresh(initial: Boolean = false) {
        hideReadPosts = false
        val sortType = uiState.value.sortType ?: return
        postPaginationManager.reset(
            PostPaginationSpecification.MultiCommunity(
                communityIds = uiState.value.community.communityIds,
                sortType = sortType,
                includeNsfw = settingsRepository.currentSettings.value.includeNsfw,
            ),
        )
        updateState {
            it.copy(
                canFetchMore = true,
                refreshing = !initial,
                initial = initial,
                loading = false,
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
        val canFetchMore = postPaginationManager.canFetchMore
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
                canFetchMore = canFetchMore,
                refreshing = false,
                initial = posts.isEmpty(),
            )
        }
    }

    private fun applySortType(value: SortType) {
        if (uiState.value.sortType == value) {
            return
        }
        screenModelScope.launch {
            updateState { it.copy(sortType = value) }
            emitEffect(MultiCommunityMviModel.Effect.BackToTop)
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
        val shouldBeMarkedAsRead = settingsRepository.currentSettings.value.markAsReadOnInteraction
        handlePostUpdate(newPost)
        screenModelScope.launch {
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

    private suspend fun setRead(
        post: PostModel,
        read: Boolean,
    ) {
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
        screenModelScope.launch {
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
        screenModelScope.launch {
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
        screenModelScope.launch {
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

    private fun clearRead() {
        screenModelScope.launch {
            hideReadPosts = true
            updateState {
                val newPosts = it.posts.filter { e -> !e.read }
                it.copy(posts = newPosts)
            }
        }
    }

    private fun hide(post: PostModel) {
        screenModelScope.launch {
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
}
