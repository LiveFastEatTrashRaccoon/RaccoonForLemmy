package com.livefast.eattrash.raccoonforlemmy.unit.myaccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.ThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.ProfileLoggedSection
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import com.livefast.eattrash.raccoonforlemmy.core.utils.share.ShareHelper
import com.livefast.eattrash.raccoonforlemmy.core.utils.vibrate.HapticFeedback
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.ApiConfigurationRepository
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommentModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SortType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.CommentPaginationManager
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.CommentPaginationSpecification
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.PostNavigationManager
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.PostPaginationManager
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.PostPaginationSpecification
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.CommentRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.LemmyValueCache
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.PostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.yield

class ProfileLoggedViewModel(
    private val identityRepository: IdentityRepository,
    private val apiConfigurationRepository: ApiConfigurationRepository,
    private val postPaginationManager: PostPaginationManager,
    private val commentPaginationManager: CommentPaginationManager,
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
    private val themeRepository: ThemeRepository,
    private val settingsRepository: SettingsRepository,
    private val shareHelper: ShareHelper,
    private val notificationCenter: NotificationCenter,
    private val hapticFeedback: HapticFeedback,
    private val postNavigationManager: PostNavigationManager,
    private val lemmyValueCache: LemmyValueCache,
) : ViewModel(),
    MviModelDelegate<ProfileLoggedMviModel.Intent, ProfileLoggedMviModel.UiState, ProfileLoggedMviModel.Effect>
    by DefaultMviModelDelegate(initialState = ProfileLoggedMviModel.UiState()),
    ProfileLoggedMviModel {
    init {
        viewModelScope.launch {
            apiConfigurationRepository.instance
                .onEach { instance ->
                    updateState {
                        it.copy(instance = instance)
                    }
                }.launchIn(this)

            themeRepository.postLayout
                .onEach { layout ->
                    updateState { it.copy(postLayout = layout) }
                }.launchIn(this)

            @OptIn(FlowPreview::class)
            identityRepository.isLogged
                .debounce(500)
                .onEach { logged ->
                    if (logged == true) {
                        updateState {
                            it.copy(
                                posts = emptyList(),
                                comments = emptyList(),
                            )
                        }
                        refreshUser()
                        refresh()
                    }
                }.launchIn(this)
            settingsRepository.currentSettings
                .onEach { settings ->
                    updateState {
                        it.copy(
                            voteFormat = settings.voteFormat,
                            autoLoadImages = settings.autoLoadImages,
                            preferNicknames = settings.preferUserNicknames,
                            fullHeightImages = settings.fullHeightImages,
                            fullWidthImages = settings.fullWidthImages,
                            showScores = settings.showScores,
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
                .subscribe(NotificationCenterEvent.Share::class)
                .onEach { evt ->
                    shareHelper.share(evt.url)
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.Logout::class)
                .onEach {
                    delay(250)
                    refreshUser()
                }.launchIn(this)

            lemmyValueCache.isDownVoteEnabled
                .onEach { value ->
                    updateState { it.copy(downVoteEnabled = value) }
                }.launchIn(this)
            lemmyValueCache.isCurrentUserModerator
                .onEach { value ->
                    updateState { it.copy(isModerator = value) }
                }.launchIn(this)

            if (uiState.value.initial) {
                val userFromCache = identityRepository.cachedUser
                if (userFromCache != null) {
                    updateState {
                        it.copy(
                            user = userFromCache,
                            initial = false,
                        )
                    }
                    refresh(initial = false)
                } else {
                    refreshUser()
                    refresh(initial = true)
                }
            }
        }
    }

    override fun reduce(intent: ProfileLoggedMviModel.Intent) {
        when (intent) {
            is ProfileLoggedMviModel.Intent.ChangeSection -> changeSection(intent.section)
            is ProfileLoggedMviModel.Intent.DeleteComment -> deleteComment(intent.id)
            is ProfileLoggedMviModel.Intent.DeletePost -> deletePost(intent.id)
            ProfileLoggedMviModel.Intent.LoadNextPage ->
                viewModelScope.launch {
                    loadNextPage()
                }

            ProfileLoggedMviModel.Intent.Refresh ->
                viewModelScope.launch {
                    refreshUser()
                    refresh()
                }

            is ProfileLoggedMviModel.Intent.Share -> {
                shareHelper.share(intent.url)
            }

            is ProfileLoggedMviModel.Intent.DownVoteComment -> {
                if (intent.feedback) {
                    hapticFeedback.vibrate()
                }
                uiState.value.comments.firstOrNull { it.id == intent.id }?.also { comment ->
                    toggleDownVoteComment(comment)
                }
            }

            is ProfileLoggedMviModel.Intent.DownVotePost -> {
                if (intent.feedback) {
                    hapticFeedback.vibrate()
                }
                uiState.value.posts.firstOrNull { it.id == intent.id }?.also { post ->
                    toggleDownVotePost(post)
                }
            }

            is ProfileLoggedMviModel.Intent.SaveComment -> {
                if (intent.feedback) {
                    hapticFeedback.vibrate()
                }
                uiState.value.comments.firstOrNull { it.id == intent.id }?.also { comment ->
                    toggleSaveComment(comment)
                }
            }

            is ProfileLoggedMviModel.Intent.SavePost -> {
                if (intent.feedback) {
                    hapticFeedback.vibrate()
                }
                uiState.value.posts.firstOrNull { it.id == intent.id }?.also { post ->
                    toggleSavePost(post)
                }
            }

            is ProfileLoggedMviModel.Intent.UpVoteComment -> {
                if (intent.feedback) {
                    hapticFeedback.vibrate()
                }
                uiState.value.comments.firstOrNull { it.id == intent.id }?.also { comment ->
                    toggleUpVoteComment(comment)
                }
            }

            is ProfileLoggedMviModel.Intent.UpVotePost -> {
                if (intent.feedback) {
                    hapticFeedback.vibrate()
                }
                uiState.value.posts.firstOrNull { it.id == intent.id }?.also { post ->
                    toggleUpVotePost(post)
                }
            }

            is ProfileLoggedMviModel.Intent.WillOpenDetail ->
                viewModelScope.launch {
                    if (intent.commentId == null) {
                        val state = postPaginationManager.extractState()
                        postNavigationManager.push(state)
                    }
                    emitEffect(
                        ProfileLoggedMviModel.Effect.OpenDetail(
                            postId = intent.postId,
                            commentId = intent.commentId,
                        ),
                    )
                }

            is ProfileLoggedMviModel.Intent.RestorePost -> {
                restorePost(intent.id)
            }

            is ProfileLoggedMviModel.Intent.RestoreComment -> {
                restoreComment(intent.id)
            }
        }
    }

    private suspend fun refreshUser() {
        val auth = identityRepository.authToken.value.orEmpty()
        if (auth.isEmpty()) {
            updateState { it.copy(user = null) }
        } else {
            var user = identityRepository.cachedUser
            withContext(Dispatchers.IO) {
                runCatching {
                    withTimeout(2000) {
                        while (user == null) {
                            // retry getting user if non-empty auth
                            delay(500)
                            identityRepository.refreshLoggedState()
                            user = identityRepository.cachedUser
                            yield()
                        }
                    }
                }

                lemmyValueCache.refresh(auth)
            }
            updateState {
                it.copy(user = user)
            }
        }
    }

    private suspend fun refresh(initial: Boolean = false) {
        val userId = uiState.value.user?.id ?: return
        postPaginationManager.reset(
            PostPaginationSpecification.User(
                id = userId,
                sortType = SortType.New,
                includeDeleted = true,
            ),
        )
        commentPaginationManager.reset(
            CommentPaginationSpecification.User(
                id = userId,
                sortType = SortType.New,
                includeDeleted = true,
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
        loadNextPage()
        if (identityRepository.isLogged.value == null) {
            identityRepository.refreshLoggedState()
        }
    }

    private fun changeSection(section: ProfileLoggedSection) {
        viewModelScope.launch {
            updateState {
                it.copy(section = section)
            }
        }
    }

    private suspend fun loadNextPage() {
        val currentState = uiState.value
        if (!currentState.canFetchMore || currentState.loading || currentState.user == null) {
            updateState { it.copy(refreshing = false) }
            return
        }

        updateState { it.copy(loading = true) }
        val refreshing = currentState.refreshing
        val section = currentState.section
        if (section == ProfileLoggedSection.Posts) {
            coroutineScope {
                val posts =
                    async {
                        postPaginationManager.loadNextPage()
                    }.await()
                val comments =
                    async {
                        if (currentState.comments.isEmpty() || refreshing) {
                            // this is needed because otherwise on first selector change
                            // the lazy column scrolls back to top (it must have an empty data set)
                            commentPaginationManager.loadNextPage()
                        } else {
                            currentState.comments
                        }
                    }.await()
                updateState {
                    it.copy(
                        posts = posts,
                        comments = comments,
                        loading = false,
                        canFetchMore = postPaginationManager.canFetchMore,
                        refreshing = false,
                        initial = false,
                    )
                }
            }
        } else {
            val comments = commentPaginationManager.loadNextPage()
            updateState {
                it.copy(
                    comments = comments,
                    loading = false,
                    canFetchMore = commentPaginationManager.canFetchMore,
                    refreshing = false,
                    initial = false,
                )
            }
        }
    }

    private fun toggleUpVotePost(post: PostModel) {
        val newVote = post.myVote <= 0
        val newPost =
            postRepository.asUpVoted(
                post = post,
                voted = newVote,
            )
        handlePostUpdate(newPost)
        viewModelScope.launch {
            try {
                val auth = identityRepository.authToken.value.orEmpty()
                postRepository.upVote(
                    post = post,
                    auth = auth,
                    voted = newVote,
                )
            } catch (e: Throwable) {
                e.printStackTrace()
                handlePostUpdate(post)
            }
        }
    }

    private fun toggleDownVotePost(post: PostModel) {
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

    private fun toggleSavePost(post: PostModel) {
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
                    post = post,
                    auth = auth,
                    saved = newValue,
                )
            } catch (e: Throwable) {
                e.printStackTrace()
                handlePostUpdate(post)
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

    private fun deletePost(id: Long) {
        viewModelScope.launch {
            val auth = identityRepository.authToken.value.orEmpty()
            val newPost = postRepository.delete(id = id, auth = auth)
            if (newPost != null) {
                handlePostUpdate(newPost)
            }
        }
    }

    private fun deleteComment(id: Long) {
        viewModelScope.launch {
            val auth = identityRepository.authToken.value.orEmpty()
            val newComment = commentRepository.delete(id, auth)
            if (newComment != null) {
                handleCommentUpdate(newComment)
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

    private fun restoreComment(id: Long) {
        viewModelScope.launch {
            val auth = identityRepository.authToken.value.orEmpty()
            val newComment =
                commentRepository.restore(
                    commentId = id,
                    auth = auth,
                )
            if (newComment != null) {
                handleCommentUpdate(newComment)
            }
        }
    }
}
