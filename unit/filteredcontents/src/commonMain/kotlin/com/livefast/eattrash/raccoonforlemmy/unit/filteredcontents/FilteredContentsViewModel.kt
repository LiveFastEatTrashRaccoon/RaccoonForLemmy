package com.livefast.eattrash.raccoonforlemmy.unit.filteredcontents

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
import com.livefast.eattrash.raccoonforlemmy.core.utils.vibrate.HapticFeedback
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommentModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.ListingType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SortType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.imageUrl
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.CommentPaginationManager
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.CommentPaginationSpecification
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.PostNavigationManager
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.PostPaginationManager
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.PostPaginationSpecification
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.CommentRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.LemmyValueCache
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.PostRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class FilteredContentsViewModel(
    private val contentsType: Int,
    private val postPaginationManager: PostPaginationManager,
    private val commentPaginationManager: CommentPaginationManager,
    private val themeRepository: ThemeRepository,
    private val settingsRepository: SettingsRepository,
    private val identityRepository: IdentityRepository,
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
    private val accountRepository: AccountRepository,
    private val userTagRepository: UserTagRepository,
    private val imagePreloadManager: ImagePreloadManager,
    private val hapticFeedback: HapticFeedback,
    private val notificationCenter: NotificationCenter,
    private val postNavigationManager: PostNavigationManager,
    private val lemmyValueCache: LemmyValueCache,
) : ViewModel(),
    MviModelDelegate<FilteredContentsMviModel.Intent, FilteredContentsMviModel.State, FilteredContentsMviModel.Effect>
    by DefaultMviModelDelegate(initialState = FilteredContentsMviModel.State()),
    FilteredContentsMviModel {
    init {
        viewModelScope.launch {
            updateState {
                val type = contentsType.toFilteredContentsType()
                it.copy(
                    contentsType = type,
                    isPostOnly = type == FilteredContentsType.Hidden,
                )
            }

            identityRepository.isLogged
                .onEach {
                    updateState { it.copy(currentUserId = identityRepository.cachedUser?.id) }
                    // when this is a top-level screen, listen for auth changes
                    refresh(initial = true)
                }.launchIn(this)
            themeRepository.postLayout
                .onEach { layout ->
                    updateState { it.copy(postLayout = layout) }
                }.launchIn(this)

            lemmyValueCache.isCurrentUserAdmin
                .onEach { value ->
                    updateState {
                        it.copy(isAdmin = value)
                    }
                }.launchIn(this)
            lemmyValueCache.isDownVoteEnabled
                .onEach { value ->
                    updateState {
                        it.copy(downVoteEnabled = value)
                    }
                }.launchIn(this)

            settingsRepository.currentSettings
                .onEach { settings ->
                    updateState {
                        it.copy(
                            autoLoadImages = settings.autoLoadImages,
                            preferNicknames = settings.preferUserNicknames,
                            swipeActionsEnabled = settings.enableSwipeActions,
                            voteFormat = settings.voteFormat,
                            fullHeightImages = settings.fullHeightImages,
                            fullWidthImages = settings.fullWidthImages,
                            fadeReadPosts = settings.fadeReadPosts,
                            showUnreadComments = settings.showUnreadComments,
                            actionsOnSwipeToStartPosts = settings.actionsOnSwipeToStartPosts,
                            actionsOnSwipeToEndPosts = settings.actionsOnSwipeToEndPosts,
                            actionsOnSwipeToStartComments = settings.actionsOnSwipeToStartComments,
                            actionsOnSwipeToEndComments = settings.actionsOnSwipeToEndComments,
                        )
                    }
                }.launchIn(this)

            notificationCenter
                .subscribe(NotificationCenterEvent.ChangedLikedType::class)
                .onEach { evt ->
                    changeLiked(evt.value)
                }.launchIn(this)

            if (uiState.value.initial) {
                refresh(initial = true)
            }
        }
    }

    override fun reduce(intent: FilteredContentsMviModel.Intent) {
        when (intent) {
            is FilteredContentsMviModel.Intent.ChangeSection -> changeSection(intent.value)
            FilteredContentsMviModel.Intent.Refresh ->
                viewModelScope.launch {
                    refresh()
                }

            FilteredContentsMviModel.Intent.HapticIndication -> hapticFeedback.vibrate()
            FilteredContentsMviModel.Intent.LoadNextPage ->
                viewModelScope.launch {
                    loadNextPage()
                }

            is FilteredContentsMviModel.Intent.UpVotePost -> {
                if (intent.feedback) {
                    hapticFeedback.vibrate()
                }
                uiState.value.posts.firstOrNull { it.id == intent.id }?.also { post ->
                    toggleUpVote(post)
                }
            }

            is FilteredContentsMviModel.Intent.DownVotePost -> {
                if (intent.feedback) {
                    hapticFeedback.vibrate()
                }
                uiState.value.posts.firstOrNull { it.id == intent.id }?.also { post ->
                    toggleDownVote(post)
                }
            }

            is FilteredContentsMviModel.Intent.SavePost -> {
                if (intent.feedback) {
                    hapticFeedback.vibrate()
                }
                uiState.value.posts.firstOrNull { it.id == intent.id }?.also { post ->
                    toggleSave(post)
                }
            }

            is FilteredContentsMviModel.Intent.ModFeaturePost ->
                uiState.value.posts
                    .firstOrNull { it.id == intent.id }
                    ?.also { post ->
                        feature(post)
                    }

            is FilteredContentsMviModel.Intent.AdminFeaturePost ->
                uiState.value.posts
                    .firstOrNull { it.id == intent.id }
                    ?.also { post ->
                        featureLocal(post)
                    }

            is FilteredContentsMviModel.Intent.ModLockPost ->
                uiState.value.posts
                    .firstOrNull { it.id == intent.id }
                    ?.also { post ->
                        lock(post)
                    }

            is FilteredContentsMviModel.Intent.UpVoteComment -> {
                if (intent.feedback) {
                    hapticFeedback.vibrate()
                }
                uiState.value.comments.firstOrNull { it.id == intent.commentId }?.also { comment ->
                    toggleUpVoteComment(comment)
                }
            }

            is FilteredContentsMviModel.Intent.DownVoteComment -> {
                if (intent.feedback) {
                    hapticFeedback.vibrate()
                }
                uiState.value.comments.firstOrNull { it.id == intent.commentId }?.also { comment ->
                    toggleDownVoteComment(comment)
                }
            }

            is FilteredContentsMviModel.Intent.SaveComment -> {
                if (intent.feedback) {
                    hapticFeedback.vibrate()
                }
                uiState.value.comments.firstOrNull { it.id == intent.commentId }?.also { comment ->
                    toggleSaveComment(comment)
                }
            }

            is FilteredContentsMviModel.Intent.ModDistinguishComment ->
                uiState.value.comments
                    .firstOrNull {
                        it.id == intent.commentId
                    }?.also { comment ->
                        distinguish(comment)
                    }

            is FilteredContentsMviModel.Intent.WillOpenDetail ->
                viewModelScope.launch {
                    if (intent.commentId == null) {
                        val state = postPaginationManager.extractState()
                        postNavigationManager.push(state)
                    }
                    emitEffect(
                        FilteredContentsMviModel.Effect.OpenDetail(
                            postId = intent.postId,
                            commentId = intent.commentId,
                        ),
                    )
                }
        }
    }

    private suspend fun refresh(initial: Boolean = false) {
        val currentState = uiState.value
        val postSpecification =
            when (currentState.contentsType) {
                FilteredContentsType.Moderated ->
                    PostPaginationSpecification.Listing(
                        listingType = ListingType.ModeratorView,
                        sortType = SortType.New,
                    )

                FilteredContentsType.Votes ->
                    PostPaginationSpecification.Votes(
                        liked = currentState.liked,
                        sortType = SortType.New,
                    )

                FilteredContentsType.Bookmarks ->
                    PostPaginationSpecification.Saved(
                        sortType = SortType.New,
                    )

                FilteredContentsType.Hidden ->
                    PostPaginationSpecification.Hidden(
                        sortType = SortType.New,
                    )
            }
        postPaginationManager.reset(postSpecification)
        val commentSpecification =
            when (currentState.contentsType) {
                FilteredContentsType.Moderated ->
                    CommentPaginationSpecification.Replies(
                        listingType = ListingType.ModeratorView,
                        sortType = SortType.New,
                    )

                FilteredContentsType.Votes ->
                    CommentPaginationSpecification.Votes(
                        liked = currentState.liked,
                        sortType = SortType.New,
                    )

                FilteredContentsType.Bookmarks ->
                    CommentPaginationSpecification.Saved(
                        sortType = SortType.New,
                    )

                FilteredContentsType.Hidden -> null
            }
        if (commentSpecification != null) {
            commentPaginationManager.reset(commentSpecification)
        }
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
        viewModelScope.launch {
            loadNextPage()
        }
    }

    private fun changeSection(section: FilteredContentsSection) {
        viewModelScope.launch {
            updateState {
                it.copy(section = section)
            }
        }
    }

    private suspend fun loadNextPage() {
        val currentState = uiState.value
        if (!currentState.canFetchMore || currentState.loading) {
            updateState { it.copy(refreshing = false) }
            return
        }
        updateState { it.copy(loading = true) }
        val refreshing = currentState.refreshing

        if (currentState.section == FilteredContentsSection.Posts) {
            coroutineScope {
                val posts =
                    async {
                        postPaginationManager.loadNextPage()
                    }.await()
                val comments =
                    if (currentState.isPostOnly) {
                        emptyList()
                    } else {
                        async {
                            if (currentState.comments.isEmpty() || refreshing) {
                                // this is needed because otherwise on first selector change
                                // the lazy column scrolls back to top (it must have an empty data set)
                                commentPaginationManager.loadNextPage()
                            } else {
                                currentState.comments
                            }
                        }.await()
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
                        comments = comments,
                        loading = if (it.initial) posts.isEmpty() else false,
                        canFetchMore = postPaginationManager.canFetchMore,
                        refreshing = false,
                        initial = if (it.initial) posts.isEmpty() else false,
                    )
                }
            }
        } else {
            val comments = commentPaginationManager.loadNextPage()
            updateState {
                it.copy(
                    comments = comments,
                    loading = if (it.initial) comments.isEmpty() else false,
                    canFetchMore = commentPaginationManager.canFetchMore,
                    refreshing = false,
                    initial = if (it.initial) comments.isEmpty() else false,
                )
            }
        }
    }

    private fun toggleUpVote(post: PostModel) {
        val newValue = post.myVote <= 0
        val newPost =
            postRepository.asUpVoted(
                post = post,
                voted = newValue,
            )
        handlePostUpdate(newPost)
        viewModelScope.launch {
            try {
                val auth = identityRepository.authToken.value.orEmpty()
                postRepository.upVote(
                    auth = auth,
                    post = post,
                    voted = newValue,
                )
                notificationCenter.send(
                    event = NotificationCenterEvent.PostUpdated(newPost),
                )
            } catch (e: Throwable) {
                e.printStackTrace()
                handlePostUpdate(post)
            }
        }
    }

    private fun toggleDownVote(post: PostModel) {
        val newValue = post.myVote >= 0
        val newPost =
            postRepository.asDownVoted(
                post = post,
                downVoted = newValue,
            )
        handlePostUpdate(newPost)
        viewModelScope.launch {
            try {
                val auth = identityRepository.authToken.value.orEmpty()
                postRepository.downVote(
                    post = post,
                    auth = auth,
                    downVoted = newValue,
                )
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
        handlePostUpdate(newPost)
        viewModelScope.launch {
            try {
                val auth = identityRepository.authToken.value.orEmpty()
                postRepository.save(
                    auth = auth,
                    post = post,
                    saved = newValue,
                )
                notificationCenter.send(
                    event = NotificationCenterEvent.PostUpdated(newPost),
                )
            } catch (e: Throwable) {
                e.printStackTrace()
                handlePostUpdate(post)
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

    private fun toggleUpVoteComment(comment: CommentModel) {
        val newValue = comment.myVote <= 0
        val newComment =
            commentRepository.asUpVoted(
                comment = comment,
                voted = newValue,
            )
        handleCommentUpdate(newComment)
        viewModelScope.launch {
            try {
                val auth = identityRepository.authToken.value.orEmpty()
                commentRepository.upVote(
                    auth = auth,
                    comment = comment,
                    voted = newValue,
                )
            } catch (e: Throwable) {
                e.printStackTrace()
                handleCommentUpdate(comment)
            }
        }
    }

    private fun toggleDownVoteComment(comment: CommentModel) {
        val newValue = comment.myVote >= 0
        val newComment = commentRepository.asDownVoted(comment, newValue)
        handleCommentUpdate(newComment)
        viewModelScope.launch {
            try {
                val auth = identityRepository.authToken.value.orEmpty()
                commentRepository.downVote(
                    auth = auth,
                    comment = comment,
                    downVoted = newValue,
                )
            } catch (e: Throwable) {
                e.printStackTrace()
                handleCommentUpdate(comment)
            }
        }
    }

    private fun toggleSaveComment(comment: CommentModel) {
        val newValue = !comment.saved
        val newComment =
            commentRepository.asSaved(
                comment = comment,
                saved = newValue,
            )
        handleCommentUpdate(newComment)
        viewModelScope.launch {
            try {
                val auth = identityRepository.authToken.value.orEmpty()
                commentRepository.save(
                    auth = auth,
                    comment = comment,
                    saved = newValue,
                )
            } catch (e: Throwable) {
                e.printStackTrace()
                handleCommentUpdate(comment)
            }
        }
    }

    private fun distinguish(comment: CommentModel) {
        viewModelScope.launch {
            val auth = identityRepository.authToken.value.orEmpty()
            val newComment =
                commentRepository.distinguish(
                    commentId = comment.id,
                    auth = auth,
                    distinguished = !comment.distinguished,
                )
            if (newComment != null) {
                handleCommentUpdate(newComment)
            }
        }
    }

    private fun handleCommentUpdate(comment: CommentModel) {
        viewModelScope.launch {
            updateState {
                it.copy(
                    comments =
                    it.comments.map { c ->
                        if (c.id == comment.id) {
                            comment
                        } else {
                            c
                        }
                    },
                )
            }
        }
    }

    private fun changeLiked(value: Boolean) {
        viewModelScope.launch {
            updateState { it.copy(liked = value) }
            refresh(initial = true)
            emitEffect(FilteredContentsMviModel.Effect.BackToTop)
        }
    }
}
