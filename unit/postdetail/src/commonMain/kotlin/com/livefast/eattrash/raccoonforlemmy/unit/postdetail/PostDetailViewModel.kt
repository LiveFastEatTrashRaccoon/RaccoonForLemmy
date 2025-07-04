package com.livefast.eattrash.raccoonforlemmy.unit.postdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.ThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.UserTagType
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.AccountRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.PostLastSeenDateRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.UserTagRepository
import com.livefast.eattrash.raccoonforlemmy.core.utils.datetime.epochMillis
import com.livefast.eattrash.raccoonforlemmy.core.utils.share.ShareHelper
import com.livefast.eattrash.raccoonforlemmy.core.utils.vibrate.HapticFeedback
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.ApiConfigurationRepository
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommentModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SortType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.containsId
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toSortType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.CommentPaginationManager
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.CommentPaginationSpecification
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.PostNavigationManager
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.CommentRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.CommunityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.LemmyItemCache
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.LemmyValueCache
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.PostRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.SiteRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.UserTagHelper
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.usecase.GetSortTypesUseCase
import com.livefast.eattrash.raccoonforlemmy.unit.postdetail.utils.populateLoadMoreComments
import com.livefast.eattrash.raccoonforlemmy.unit.postdetail.utils.sortToNestedOrder
import kotlinx.coroutines.Dispatchers
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
class PostDetailViewModel(
    postId: Long,
    private val otherInstance: String,
    private val highlightCommentId: Long,
    private val isModerator: Boolean,
    private val identityRepository: IdentityRepository,
    private val apiConfigurationRepository: ApiConfigurationRepository,
    private val postRepository: PostRepository,
    private val commentPaginationManager: CommentPaginationManager,
    private val commentRepository: CommentRepository,
    private val communityRepository: CommunityRepository,
    private val siteRepository: SiteRepository,
    private val themeRepository: ThemeRepository,
    private val settingsRepository: SettingsRepository,
    private val accountRepository: AccountRepository,
    private val userTagRepository: UserTagRepository,
    private val postLastSeenDateRepository: PostLastSeenDateRepository,
    private val userTagHelper: UserTagHelper,
    private val shareHelper: ShareHelper,
    private val notificationCenter: NotificationCenter,
    private val hapticFeedback: HapticFeedback,
    private val getSortTypesUseCase: GetSortTypesUseCase,
    private val itemCache: LemmyItemCache,
    private val postNavigationManager: PostNavigationManager,
    private val lemmyValueCache: LemmyValueCache,
) : ViewModel(),
    MviModelDelegate<PostDetailMviModel.Intent, PostDetailMviModel.UiState, PostDetailMviModel.Effect>
    by DefaultMviModelDelegate(initialState = PostDetailMviModel.UiState()),
    PostDetailMviModel {
    private var highlightCommentPath: String? = null
    private var commentWasHighlighted = false
    private val initialNavigationEnabled = postNavigationManager.canNavigate.value
    private var lastCommentNavigateIndex: Int? = null

    override fun onCleared() {
        super.onCleared()
        if (initialNavigationEnabled) {
            postNavigationManager.pop()
        }
    }

    init {
        viewModelScope.launch {
            updateState {
                it.copy(
                    instance =
                    otherInstance.takeIf { n -> n.isNotEmpty() }
                        ?: apiConfigurationRepository.instance.value,
                )
            }
            if (uiState.value.post.id == 0L) {
                val post = itemCache.getPost(postId) ?: PostModel()
                val lastSeenTimestamp = postLastSeenDateRepository.get(postId)
                updateState {
                    it.copy(
                        post = post,
                        isModerator = isModerator,
                        currentUserId = identityRepository.cachedUser?.id,
                        canFetchMore = it.comments.size < post.comments,
                        lastSeenTimestamp = lastSeenTimestamp,
                    )
                }
                if (identityRepository.isLogged.value == true) {
                    val now = epochMillis()
                    postLastSeenDateRepository.save(postId = postId, timestamp = now)
                }
            }

            themeRepository.postLayout
                .onEach { layout ->
                    updateState { it.copy(postLayout = layout) }
                }.launchIn(this)

            settingsRepository.currentSettings
                .onEach { settings ->
                    updateState {
                        it.copy(
                            doubleTapActionEnabled = settings.enableDoubleTapAction,
                            voteFormat = settings.voteFormat,
                            autoLoadImages = settings.autoLoadImages,
                            preferNicknames = settings.preferUserNicknames,
                            fullHeightImages = settings.fullHeightImages,
                            fullWidthImages = settings.fullWidthImages,
                            actionsOnSwipeToStartComments = settings.actionsOnSwipeToStartComments,
                            actionsOnSwipeToEndComments = settings.actionsOnSwipeToEndComments,
                            showScores = settings.showScores,
                            enableButtonsToScrollBetweenComments = settings.enableButtonsToScrollBetweenComments,
                            commentBarThickness = settings.commentBarThickness,
                            commentIndentAmount = settings.commentIndentAmount,
                        )
                    }
                }.launchIn(this)

            notificationCenter
                .subscribe(NotificationCenterEvent.PostRemoved::class)
                .onEach { _ ->
                    emitEffect(PostDetailMviModel.Effect.Close)
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.ChangeCommentSortType::class)
                .onEach { evt ->
                    applySortType(evt.value)
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.CommentCreated::class)
                .onEach {
                    refresh()
                }.launchIn(this)

            notificationCenter
                .subscribe(NotificationCenterEvent.PostUpdated::class)
                .onEach { evt ->
                    if (evt.model.id == uiState.value.post.id) {
                        refreshPost()
                    }
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.Share::class)
                .onEach { evt ->
                    shareHelper.share(evt.url)
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.UserBannedComment::class)
                .onEach { evt ->
                    val commentId = evt.commentId
                    val newUser = evt.user
                    updateState {
                        it.copy(
                            comments =
                            it.comments.map { c ->
                                if (c.id == commentId) {
                                    c.copy(
                                        creator = newUser,
                                        updateDate = newUser.updateDate,
                                    )
                                } else {
                                    c
                                }
                            },
                        )
                    }
                }.launchIn(this)

            uiState
                .map { it.searchText }
                .distinctUntilChanged()
                .drop(1)
                .debounce(1_000)
                .onEach {
                    if (!uiState.value.initial) {
                        updateState { it.copy(loading = false) }
                        emitEffect(PostDetailMviModel.Effect.BackToTop)
                        refresh()
                    }
                }.launchIn(this)

            identityRepository.isLogged
                .onEach { logged ->
                    updateState { it.copy(isLogged = logged ?: false) }
                }.launchIn(this)
            combine(
                identityRepository.isLogged.map { it == true },
                settingsRepository.currentSettings.map { it.enableSwipeActions },
            ) { logged, swipeActionsEnabled ->
                logged && swipeActionsEnabled && otherInstance.isEmpty()
            }.onEach { value ->
                updateState { it.copy(swipeActionsEnabled = value) }
            }.launchIn(this)

            postNavigationManager.canNavigate
                .onEach { canNavigate ->
                    updateState { it.copy(isNavigationSupported = canNavigate) }
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

            val auth = identityRepository.authToken.value
            val updatedPost =
                postRepository
                    .get(
                        id = postId,
                        auth = auth,
                        instance = otherInstance,
                    )?.let {
                        with(userTagHelper) {
                            it.copy(creator = it.creator.withTags())
                        }
                    }
            if (updatedPost != null) {
                updateState {
                    it.copy(post = updatedPost)
                }
                // reset unread comments
                notificationCenter.send(
                    event =
                    NotificationCenterEvent.PostUpdated(
                        updatedPost.copy(unreadComments = 0),
                    ),
                )
            }

            if (highlightCommentId != 0L) {
                val comment =
                    commentRepository.getBy(
                        id = highlightCommentId,
                        auth = auth,
                        instance = otherInstance,
                    )
                highlightCommentPath = comment?.path
            }
            if (uiState.value.initial) {
                val moderators =
                    uiState.value.post.community
                        ?.id
                        ?.let { communityId ->
                            communityRepository.getModerators(
                                auth = auth,
                                id = communityId,
                            )
                        }.orEmpty()
                val admins =
                    uiState.value.post.community
                        .let { community ->
                            siteRepository.getAdmins(otherInstance = community?.host)
                        }

                val sortTypes =
                    getSortTypesUseCase.getTypesForComments(otherInstance = otherInstance)
                val defaultCommentSortType =
                    settingsRepository.currentSettings.value.defaultCommentSortType
                        .toSortType()
                updateState {
                    it.copy(
                        sortType = defaultCommentSortType,
                        availableSortTypes = sortTypes,
                        moderators = moderators,
                        admins = admins,
                    )
                }
                if (uiState.value.post.comments == 0) {
                    updateState { it.copy(loading = false, initial = false) }
                } else {
                    refresh(initial = true)
                }
            }
        }
    }

    private suspend fun downloadUntilHighlight() {
        val highlightPath = highlightCommentPath ?: return
        val indexOfHighlight =
            uiState.value.comments.indexOfFirst {
                it.path == highlightPath
            }
        if (indexOfHighlight == -1) {
            val lastCommentOfThread =
                uiState.value.comments
                    .filter {
                        highlightPath.startsWith(it.path)
                    }.takeIf { it.isNotEmpty() }
                    ?.maxBy { it.depth }
            if (lastCommentOfThread != null) {
                // comment has an ancestor in the list, go down that path
                loadMoreComments(
                    parentId = lastCommentOfThread.id,
                    loadUntilHighlight = true,
                )
            } else {
                // no ancestor of the comment on this pages, check the next one
                loadNextPage()
            }
        } else {
            // comment to highlight found
            commentWasHighlighted = true
            viewModelScope.launch(Dispatchers.Main) {
                // skip the first item which is the post
                emitEffect(PostDetailMviModel.Effect.ScrollToComment(indexOfHighlight + 1))
            }
        }
    }

    override fun reduce(intent: PostDetailMviModel.Intent) {
        when (intent) {
            PostDetailMviModel.Intent.LoadNextPage ->
                viewModelScope.launch {
                    if (!uiState.value.initial) {
                        loadNextPage()
                    }
                }

            PostDetailMviModel.Intent.Refresh ->
                viewModelScope.launch {
                    refresh()
                }

            PostDetailMviModel.Intent.RefreshPost -> refreshPost()
            PostDetailMviModel.Intent.HapticIndication -> hapticFeedback.vibrate()

            is PostDetailMviModel.Intent.DownVoteComment -> {
                if (intent.feedback) {
                    hapticFeedback.vibrate()
                }
                uiState.value.comments
                    .firstOrNull { it.id == intent.commentId }
                    ?.also { comment ->
                        toggleDownVoteComment(comment)
                    }
            }

            is PostDetailMviModel.Intent.DownVotePost -> {
                if (intent.feedback) {
                    hapticFeedback.vibrate()
                }
                toggleDownVotePost(uiState.value.post)
            }

            is PostDetailMviModel.Intent.SaveComment -> {
                if (intent.feedback) {
                    hapticFeedback.vibrate()
                }
                uiState.value.comments
                    .firstOrNull { it.id == intent.commentId }
                    ?.also { comment ->
                        toggleSaveComment(comment)
                    }
            }

            is PostDetailMviModel.Intent.SavePost -> {
                if (intent.feedback) {
                    hapticFeedback.vibrate()
                }
                toggleSavePost(intent.post)
            }

            is PostDetailMviModel.Intent.Share -> {
                shareHelper.share(intent.url)
            }

            is PostDetailMviModel.Intent.UpVoteComment -> {
                if (intent.feedback) {
                    hapticFeedback.vibrate()
                }
                uiState.value.comments
                    .firstOrNull { it.id == intent.commentId }
                    ?.also { comment ->
                        toggleUpVoteComment(comment)
                    }
            }

            is PostDetailMviModel.Intent.UpVotePost -> {
                if (intent.feedback) {
                    hapticFeedback.vibrate()
                }
                toggleUpVotePost(uiState.value.post)
            }

            is PostDetailMviModel.Intent.FetchMoreComments -> {
                loadMoreComments(intent.parentId)
            }

            is PostDetailMviModel.Intent.DeleteComment -> deleteComment(intent.commentId)
            PostDetailMviModel.Intent.DeletePost -> deletePost()

            is PostDetailMviModel.Intent.ToggleExpandComment -> {
                uiState.value.comments
                    .firstOrNull { it.id == intent.commentId }
                    ?.also { comment ->
                        toggleExpanded(comment)
                    }
            }

            PostDetailMviModel.Intent.ModFeaturePost -> feature(uiState.value.post)
            PostDetailMviModel.Intent.AdminFeaturePost -> featureLocal(uiState.value.post)
            PostDetailMviModel.Intent.ModLockPost -> lock(uiState.value.post)
            is PostDetailMviModel.Intent.ModDistinguishComment ->
                uiState.value.comments
                    .firstOrNull {
                        it.id == intent.commentId
                    }?.also { comment ->
                        distinguish(comment)
                    }

            is PostDetailMviModel.Intent.ModToggleModUser -> toggleModeratorStatus(intent.id)

            is PostDetailMviModel.Intent.ChangeSearching -> {
                viewModelScope.launch {
                    updateState { it.copy(searching = intent.value) }
                    if (!intent.value) {
                        updateSearchText("")
                    }
                }
            }

            is PostDetailMviModel.Intent.SetSearch -> updateSearchText(intent.value)
            PostDetailMviModel.Intent.NavigatePrevious -> navigateToPreviousPost()
            PostDetailMviModel.Intent.NavigateNext -> navigateToNextPost()
            is PostDetailMviModel.Intent.NavigatePreviousComment ->
                navigateToPreviousComment(intent.currentIndex)

            is PostDetailMviModel.Intent.NavigateNextComment ->
                navigateToNextComment(intent.currentIndex)

            PostDetailMviModel.Intent.RestorePost -> restorePost()
            is PostDetailMviModel.Intent.RestoreComment -> restoreComment(intent.commentId)
        }
    }

    private fun refreshPost() {
        viewModelScope.launch {
            val auth = identityRepository.authToken.value
            val post = uiState.value.post
            val updatedPost =
                postRepository
                    .get(
                        id = post.id,
                        auth = auth,
                        instance = otherInstance,
                    )?.let {
                        with(userTagHelper) {
                            it.copy(creator = it.creator.withTags())
                        }
                    }
            updateState {
                it.copy(post = updatedPost ?: post)
            }
        }
    }

    private suspend fun refresh(initial: Boolean = false) {
        val currentState = uiState.value
        val sortType = currentState.sortType ?: return
        val postId = currentState.post.id
        commentPaginationManager.reset(
            CommentPaginationSpecification.Replies(
                postId = postId,
                sortType = sortType,
                otherInstance = otherInstance,
                includeDeleted = true,
            ),
        )
        updateState {
            it.copy(
                initial = initial,
                canFetchMore = true,
                refreshing = !initial,
                loading = false,
            )
        }
        val accountId = accountRepository.getActive()?.id
        val adminTagColor =
            userTagRepository.getSpecialTagColor(
                accountId = accountId ?: 0,
                type = UserTagType.Admin,
            )
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
        val modTagColor =
            userTagRepository.getSpecialTagColor(
                accountId = accountId ?: 0,
                type = UserTagType.Moderator,
            )
        val opTagColor =
            userTagRepository.getSpecialTagColor(
                accountId = accountId ?: 0,
                type = UserTagType.OriginalPoster,
            )
        updateState {
            it.copy(
                adminTagColor = adminTagColor,
                botTagColor = botTagColor,
                meTagColor = meTagColor,
                modTagColor = modTagColor,
                opTagColor = opTagColor,
            )
        }
        loadNextPage()
    }

    private suspend fun loadNextPage() {
        val currentState = uiState.value
        if (!currentState.canFetchMore || currentState.loading) {
            updateState { it.copy(refreshing = false) }
            return
        }
        val autoExpandComments = settingsRepository.currentSettings.value.autoExpandComments

        updateState { it.copy(loading = true) }
        val itemList =
            commentPaginationManager
                .loadNextPage()
                .sortToNestedOrder()
                .populateLoadMoreComments()
                .map { comment ->
                    val oldComment = currentState.comments.firstOrNull { c -> c.id == comment.id }
                    if (oldComment != null) {
                        comment.copy(
                            // retain comment expand and children visible state if refreshing or loading more
                            expanded = oldComment.expanded,
                            visible = oldComment.visible,
                        )
                    } else {
                        // only first level are visible and can be expanded
                        val isExpandedAndVisibleByDefault = autoExpandComments || comment.depth == 0
                        comment.copy(
                            expanded = isExpandedAndVisibleByDefault,
                            visible = isExpandedAndVisibleByDefault,
                        )
                    }
                }

        val comments =
            itemList.let {
                if (currentState.searching) {
                    it.filter { comment ->
                        comment.text
                            .orEmpty()
                            .contains(other = currentState.searchText, ignoreCase = true)
                    }
                } else {
                    it
                }
            }
        updateState {
            it.copy(
                comments = comments,
                loading = false,
                canFetchMore = commentPaginationManager.canFetchMore,
                refreshing = false,
                initial = false,
            )
        }
        if (highlightCommentPath != null && !commentWasHighlighted) {
            downloadUntilHighlight()
        }
    }

    private fun applySortType(value: SortType) {
        if (uiState.value.sortType == value) {
            return
        }
        viewModelScope.launch {
            updateState { it.copy(sortType = value) }
            emitEffect(PostDetailMviModel.Effect.BackToTop)
            delay(50)
            refresh()
        }
    }

    private fun handlePostUpdate(post: PostModel) {
        viewModelScope.launch {
            val newPost =
                post.let {
                    with(userTagHelper) {
                        it.copy(creator = it.creator.withTags())
                    }
                }
            updateState {
                it.copy(post = newPost)
            }
        }
    }

    private fun loadMoreComments(parentId: Long, loadUntilHighlight: Boolean = false) {
        val currentState = uiState.value
        val sort = currentState.sortType ?: return
        viewModelScope.launch {
            val auth = identityRepository.authToken.value
            val fetchResult =
                commentRepository
                    .getChildren(
                        auth = auth,
                        parentId = parentId,
                        instance = otherInstance,
                        sort = sort,
                    )?.sortToNestedOrder(
                        ancestorId = parentId,
                    )?.filter { c1 ->
                        // prevents accidental duplication
                        currentState.comments.none { c2 -> c2.id == c1.id }
                    }

            val commentsToInsert = fetchResult.orEmpty()
            if (commentsToInsert.isEmpty()) {
                // abort and disable load more button
                val newList =
                    uiState.value.comments.map { comment ->
                        if (comment.id == parentId) {
                            comment.copy(loadMoreButtonVisible = false)
                        } else {
                            comment
                        }
                    }
                updateState { it.copy(comments = newList) }
            } else {
                val newList =
                    uiState.value.comments
                        .let { list ->
                            val index = list.indexOfFirst { c -> c.id == parentId }
                            list
                                .toMutableList()
                                .apply {
                                    addAll(index + 1, fetchResult.orEmpty())
                                }.toList()
                        }.populateLoadMoreComments()
                updateState { it.copy(comments = newList) }

                if (loadUntilHighlight) {
                    // start indirect recursion
                    downloadUntilHighlight()
                }
            }
        }
    }

    private fun toggleUpVotePost(post: PostModel) {
        val newValue = post.myVote <= 0
        val newPost =
            postRepository.asUpVoted(
                post = post,
                voted = newValue,
            )
        viewModelScope.launch {
            updateState { it.copy(post = newPost) }
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
                updateState { it.copy(post = post) }
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
        viewModelScope.launch {
            updateState {
                it.copy(post = newPost)
            }
            try {
                val auth = identityRepository.authToken.value.orEmpty()
                postRepository.downVote(
                    auth = auth,
                    post = post,
                    downVoted = newValue,
                )
                notificationCenter.send(
                    event = NotificationCenterEvent.PostUpdated(newPost),
                )
            } catch (e: Throwable) {
                e.printStackTrace()
                updateState { it.copy(post = post) }
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
        viewModelScope.launch {
            updateState { it.copy(post = newPost) }
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
                updateState { it.copy(post = post) }
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
                notificationCenter.send(
                    event = NotificationCenterEvent.CommentUpdated(newComment),
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
                notificationCenter.send(
                    event = NotificationCenterEvent.CommentUpdated(newComment),
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
                notificationCenter.send(
                    event = NotificationCenterEvent.CommentUpdated(newComment),
                )
            } catch (e: Throwable) {
                e.printStackTrace()
                handleCommentUpdate(comment)
            }
        }
    }

    private fun deleteComment(id: Long) {
        viewModelScope.launch {
            val auth = identityRepository.authToken.value.orEmpty()
            val newComment = commentRepository.delete(id, auth)
            if (newComment != null) {
                handleCommentUpdate(newComment)
                refreshPost()
            }
        }
    }

    private fun deletePost() {
        viewModelScope.launch {
            val auth = identityRepository.authToken.value.orEmpty()
            val postId = uiState.value.post.id
            val newPost = postRepository.delete(id = postId, auth = auth)
            if (newPost != null) {
                notificationCenter.send(
                    event = NotificationCenterEvent.PostUpdated(newPost),
                )
                emitEffect(PostDetailMviModel.Effect.Close)
            }
        }
    }

    private fun toggleExpanded(comment: CommentModel) {
        viewModelScope.launch(Dispatchers.Main) {
            val commentId = comment.id
            val newExpanded = !comment.expanded
            updateState {
                val newComments =
                    it.comments.map { c ->
                        when {
                            c.id == commentId -> {
                                // change expanded state of comment itself
                                c.copy(expanded = newExpanded)
                            }

                            c.path.contains(".$commentId.") && c.depth > comment.depth -> {
                                // if expanded, make all children visible and expanded
                                // otherwise, make all children not visible (doesn't matter expanded)
                                if (newExpanded) {
                                    c.copy(visible = true, expanded = true)
                                } else {
                                    c.copy(visible = false, expanded = true)
                                }
                            }

                            else -> {
                                c
                            }
                        }
                    }
                it.copy(comments = newComments)
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

    private fun toggleModeratorStatus(userId: Long) {
        viewModelScope.launch {
            val isModerator = uiState.value.moderators.containsId(userId)
            val auth = identityRepository.authToken.value.orEmpty()
            val post = uiState.value.post
            val communityId = post.community?.id
            if (communityId != null) {
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
    }

    private fun updateSearchText(value: String) {
        viewModelScope.launch {
            updateState { it.copy(searchText = value) }
        }
    }

    private fun navigateToPreviousPost() {
        val currentId = uiState.value.post.id
        viewModelScope.launch {
            updateState { it.copy(loading = true, initial = true) }
            postNavigationManager.getPrevious(currentId)?.also { newPost ->
                loadNewPost(newPost)
            }
        }
    }

    private fun navigateToNextPost() {
        val currentId = uiState.value.post.id
        viewModelScope.launch {
            updateState { it.copy(loading = true, initial = true) }
            postNavigationManager.getNext(currentId)?.also { newPost ->
                loadNewPost(newPost)
            }
        }
    }

    private suspend fun loadNewPost(post: PostModel) {
        itemCache.putPost(post)
        updateState {
            it.copy(
                searching = false,
                searchText = "",
                post = post,
            )
        }
        // reset unread comments
        notificationCenter.send(
            event = NotificationCenterEvent.PostUpdated(post.copy(unreadComments = 0)),
        )
        emitEffect(PostDetailMviModel.Effect.BackToTop)
        refresh()
    }

    private fun navigateToPreviousComment(index: Int) {
        val comments = uiState.value.comments.takeIf { it.isNotEmpty() } ?: return
        val (start, end) = 0 to (index - 1).coerceIn(0, comments.lastIndex) + 1
        val newIndex =
            comments
                .subList(
                    fromIndex = start,
                    toIndex = end,
                ).indexOfLast {
                    it.depth == 0
                }.let {
                    // +1 because of the initial item for the post
                    it + 1
                }
        val viewIndex =
            if (newIndex == lastCommentNavigateIndex) {
                ((lastCommentNavigateIndex ?: newIndex) - 1).coerceAtLeast(1)
            } else {
                newIndex
            }
        // save last scrolled index to make function strictly decreasing
        lastCommentNavigateIndex = viewIndex
        viewModelScope.launch {
            emitEffect(PostDetailMviModel.Effect.ScrollToComment(viewIndex))
        }
    }

    private fun navigateToNextComment(index: Int) {
        val comments = uiState.value.comments.takeIf { it.isNotEmpty() } ?: return
        val (start, end) = (index - 1).coerceAtLeast(0) to comments.lastIndex + 1
        val newIndex =
            comments
                .subList(
                    fromIndex = start,
                    toIndex = end,
                ).indexOfFirst {
                    it.depth == 0
                }.takeIf { it >= 0 }
                ?.let {
                    it + start
                }?.let {
                    // +1 because of the initial item for the post
                    it + 1
                }
        if (newIndex != null) {
            val viewIndex =
                if (newIndex == lastCommentNavigateIndex) {
                    (lastCommentNavigateIndex ?: newIndex) + 1
                } else {
                    newIndex.coerceAtLeast(lastCommentNavigateIndex ?: 0)
                }
            // save last scrolled index to make function strictly increasing
            lastCommentNavigateIndex = viewIndex
            viewModelScope.launch {
                emitEffect(PostDetailMviModel.Effect.ScrollToComment(viewIndex))
            }
        } else if (uiState.value.canFetchMore) {
            // fetch a new page and try again if possible (terminates on pagination end)
            viewModelScope.launch {
                loadNextPage()
                navigateToNextComment(index + 1)
            }
        }
    }

    private fun restorePost() {
        viewModelScope.launch {
            val auth = identityRepository.authToken.value.orEmpty()
            val newPost =
                postRepository.restore(
                    id = uiState.value.post.id,
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
