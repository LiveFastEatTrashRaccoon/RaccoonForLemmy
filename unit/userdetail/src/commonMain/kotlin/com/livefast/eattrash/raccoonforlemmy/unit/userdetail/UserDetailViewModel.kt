package com.livefast.eattrash.raccoonforlemmy.unit.userdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.ThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.UserDetailSection
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.UserTagModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.isSpecial
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.AccountRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.UserSortRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.UserTagRepository
import com.livefast.eattrash.raccoonforlemmy.core.utils.imageload.ImagePreloadManager
import com.livefast.eattrash.raccoonforlemmy.core.utils.share.ShareHelper
import com.livefast.eattrash.raccoonforlemmy.core.utils.vibrate.HapticFeedback
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.ApiConfigurationRepository
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommentModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SortType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.imageUrl
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.readableHandle
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toInt
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toSortType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.CommentPaginationManager
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.CommentPaginationSpecification
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.PostNavigationManager
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.PostPaginationManager
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.pagination.PostPaginationSpecification
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.CommentRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.LemmyItemCache
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.LemmyValueCache
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.PostRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.SiteRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.UserRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.UserTagHelper
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.usecase.GetSortTypesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class UserDetailViewModel(
    private val userId: Long,
    private val otherInstance: String,
    private val identityRepository: IdentityRepository,
    private val apiConfigurationRepository: ApiConfigurationRepository,
    private val postPaginationManager: PostPaginationManager,
    private val commentPaginationManager: CommentPaginationManager,
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
    private val siteRepository: SiteRepository,
    private val themeRepository: ThemeRepository,
    private val shareHelper: ShareHelper,
    private val hapticFeedback: HapticFeedback,
    private val settingsRepository: SettingsRepository,
    private val userTagRepository: UserTagRepository,
    private val userTagHelper: UserTagHelper,
    private val accountRepository: AccountRepository,
    private val notificationCenter: NotificationCenter,
    private val imagePreloadManager: ImagePreloadManager,
    private val getSortTypesUseCase: GetSortTypesUseCase,
    private val itemCache: LemmyItemCache,
    private val postNavigationManager: PostNavigationManager,
    private val lemmyValueCache: LemmyValueCache,
    private val userSortRepository: UserSortRepository,
) : ViewModel(),
    MviModelDelegate<UserDetailMviModel.Intent, UserDetailMviModel.UiState, UserDetailMviModel.Effect>
    by DefaultMviModelDelegate(initialState = UserDetailMviModel.UiState()),
    UserDetailMviModel {
    init {
        viewModelScope.launch {
            updateState {
                it.copy(
                    instance =
                    otherInstance.takeIf { n -> n.isNotEmpty() }
                        ?: apiConfigurationRepository.instance.value,
                )
            }

            if (uiState.value.user.id == 0L) {
                val user = itemCache.getUser(userId) ?: UserModel()
                updateState {
                    it.copy(user = user)
                }

                val accountId = accountRepository.getActive()?.id
                if (accountId != null) {
                    val tags = userTagRepository.getAll(accountId).filter { !it.isSpecial }
                    updateState {
                        it.copy(availableUserTags = tags)
                    }
                }
                refreshCurrentUserTags()
            }
            themeRepository.postLayout
                .onEach { layout ->
                    updateState { it.copy(postLayout = layout) }
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.PostUpdated::class)
                .onEach { evt ->
                    handlePostUpdate(evt.model)
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.ChangeSortType::class)
                .onEach { evt ->
                    val userHandle = uiState.value.user.readableHandle
                    if (evt.screenKey == userHandle) {
                        val section = uiState.value.section
                        if (evt.saveAsDefault) {
                            when (section) {
                                UserDetailSection.Comments ->
                                    userSortRepository.saveForComments(
                                        handle = userHandle,
                                        value = evt.value.toInt(),
                                    )

                                UserDetailSection.Posts ->
                                    userSortRepository.saveForPosts(
                                        handle = userHandle,
                                        value = evt.value.toInt(),
                                    )
                            }
                        }
                        applySortType(
                            value = evt.value,
                            section = section,
                        )
                    }
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.Share::class)
                .onEach { evt ->
                    shareHelper.share(evt.url)
                }.launchIn(this)

            lemmyValueCache.isDownVoteEnabled
                .onEach { value ->
                    updateState {
                        it.copy(
                            downVoteEnabled = value,
                        )
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
        }

        viewModelScope.launch {
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
                            actionsOnSwipeToStartComments = settings.actionsOnSwipeToStartComments,
                            actionsOnSwipeToEndComments = settings.actionsOnSwipeToEndComments,
                            showScores = settings.showScores,
                        )
                    }
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
                val userHandle = uiState.value.user.readableHandle
                val defaultPostSortType =
                    settingsRepository.currentSettings.value.defaultPostSortType
                        .toSortType()
                val customPostSortType =
                    userSortRepository.getForPosts(userHandle)?.toSortType()
                val defaultCommentSortType =
                    settingsRepository.currentSettings.value.defaultCommentSortType
                        .toSortType()
                val customCommentSortType =
                    userSortRepository.getForComments(userHandle)?.toSortType()
                updateState {
                    it.copy(
                        postSortType = customPostSortType ?: defaultPostSortType,
                        commentSortType = customCommentSortType ?: defaultCommentSortType,
                    )
                }

                refresh(initial = true)
            }
        }
    }

    override fun reduce(intent: UserDetailMviModel.Intent) {
        when (intent) {
            is UserDetailMviModel.Intent.ChangeSection -> changeSection(intent.section)
            is UserDetailMviModel.Intent.DownVoteComment -> {
                if (intent.feedback) {
                    hapticFeedback.vibrate()
                }
                uiState.value.comments.firstOrNull { it.id == intent.id }?.also { comment ->
                    toggleDownVoteComment(comment)
                }
            }

            is UserDetailMviModel.Intent.DownVotePost -> {
                if (intent.feedback) {
                    hapticFeedback.vibrate()
                }
                uiState.value.posts.firstOrNull { it.id == intent.id }?.also { post ->
                    toggleDownVote(post)
                }
            }

            UserDetailMviModel.Intent.HapticIndication -> hapticFeedback.vibrate()
            UserDetailMviModel.Intent.LoadNextPage ->
                viewModelScope.launch {
                    loadNextPage()
                }

            UserDetailMviModel.Intent.Refresh ->
                viewModelScope.launch {
                    refresh()
                }

            is UserDetailMviModel.Intent.SaveComment -> {
                if (intent.feedback) {
                    hapticFeedback.vibrate()
                }
                uiState.value.comments.firstOrNull { it.id == intent.id }?.also { comment ->
                    toggleSaveComment(comment)
                }
            }

            is UserDetailMviModel.Intent.SavePost -> {
                if (intent.feedback) {
                    hapticFeedback.vibrate()
                }
                uiState.value.posts.firstOrNull { it.id == intent.id }?.also { post ->
                    toggleSave(post)
                }
            }

            is UserDetailMviModel.Intent.UpVoteComment -> {
                if (intent.feedback) {
                    hapticFeedback.vibrate()
                }
                uiState.value.comments.firstOrNull { it.id == intent.id }?.also { comment ->
                    toggleUpVoteComment(comment)
                }
            }

            is UserDetailMviModel.Intent.UpVotePost -> {
                if (intent.feedback) {
                    hapticFeedback.vibrate()
                }
                uiState.value.posts.firstOrNull { it.id == intent.id }?.also { post ->
                    toggleUpVote(post)
                }
            }

            is UserDetailMviModel.Intent.Share -> {
                shareHelper.share(intent.url)
            }

            UserDetailMviModel.Intent.Block -> blockUser()
            UserDetailMviModel.Intent.BlockInstance -> blockInstance()

            is UserDetailMviModel.Intent.WillOpenDetail ->
                viewModelScope.launch {
                    if (intent.commentId == null) {
                        val state = postPaginationManager.extractState()
                        postNavigationManager.push(state)
                    }
                    emitEffect(
                        UserDetailMviModel.Effect.OpenDetail(
                            postId = intent.postId,
                            commentId = intent.commentId,
                        ),
                    )
                }

            is UserDetailMviModel.Intent.AddUserTag ->
                addUserTag(name = intent.name, color = intent.color)
            is UserDetailMviModel.Intent.UpdateTags -> updateTags(intent.ids)
        }
    }

    private fun applySortType(value: SortType, section: UserDetailSection) {
        if (uiState.value.postSortType == value && section == UserDetailSection.Posts) {
            return
        }
        if (uiState.value.commentSortType == value && section == UserDetailSection.Comments) {
            return
        }
        viewModelScope.launch(Dispatchers.Main) {
            updateState {
                when (section) {
                    UserDetailSection.Comments -> it.copy(commentSortType = value)
                    UserDetailSection.Posts -> it.copy(postSortType = value)
                }
            }
            emitEffect(UserDetailMviModel.Effect.BackToTop)
            delay(50)
            refresh()
        }
    }

    private fun changeSection(section: UserDetailSection) {
        viewModelScope.launch {
            updateState {
                it.copy(section = section)
            }
            updateAvailableSortTypes()
        }
    }

    private suspend fun updateAvailableSortTypes() {
        val sortTypes =
            if (uiState.value.section == UserDetailSection.Posts) {
                getSortTypesUseCase.getTypesForPosts(otherInstance = otherInstance)
            } else {
                getSortTypesUseCase.getTypesForComments(otherInstance = otherInstance)
            }
        updateState { it.copy(availableSortTypes = sortTypes) }
    }

    private suspend fun refresh(initial: Boolean = false) {
        postPaginationManager.reset(
            PostPaginationSpecification.User(
                id = userId,
                name = uiState.value.user.name,
                sortType = uiState.value.postSortType,
                otherInstance = otherInstance,
            ),
        )
        commentPaginationManager.reset(
            CommentPaginationSpecification.User(
                id = userId,
                name = uiState.value.user.name,
                sortType = uiState.value.commentSortType,
                otherInstance = otherInstance,
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
        val auth = identityRepository.authToken.value
        val refreshedUser =
            userRepository.get(
                id = userId,
                auth = auth,
                otherInstance = otherInstance,
                username = uiState.value.user.name,
            )
        if (refreshedUser != null) {
            updateState { it.copy(user = refreshedUser) }
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
        val refreshing = currentState.refreshing
        val section = currentState.section
        if (section == UserDetailSection.Posts) {
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
                    if (uiState.value.autoLoadImages) {
                        posts.forEach { post ->
                            post.imageUrl.takeIf { i -> i.isNotEmpty() }?.also { url ->
                                imagePreloadManager.preload(url)
                            }
                        }
                    }
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

    private fun toggleUpVote(post: PostModel) {
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

    private fun blockUser() {
        viewModelScope.launch {
            updateState { it.copy(asyncInProgress = true) }
            try {
                val auth = identityRepository.authToken.value
                userRepository.block(
                    id = userId,
                    blocked = true,
                    auth = auth,
                )
                emitEffect(UserDetailMviModel.Effect.Success)
            } catch (e: Throwable) {
                emitEffect(UserDetailMviModel.Effect.Error(e.message))
            } finally {
                updateState { it.copy(asyncInProgress = false) }
            }
        }
    }

    private fun blockInstance() {
        viewModelScope.launch {
            updateState { it.copy(asyncInProgress = true) }
            try {
                val user = uiState.value.user
                val instanceId = user.instanceId
                val auth = identityRepository.authToken.value
                siteRepository.block(
                    id = instanceId,
                    blocked = true,
                    auth = auth,
                )
                emitEffect(UserDetailMviModel.Effect.Success)
            } catch (e: Throwable) {
                emitEffect(UserDetailMviModel.Effect.Error(e.message))
            } finally {
                updateState { it.copy(asyncInProgress = false) }
            }
        }
    }

    private suspend fun refreshCurrentUserTags() {
        val accountId = accountRepository.getActive()?.id ?: return
        val user = uiState.value.user
        val currentTags =
            userTagRepository.getBelonging(
                accountId = accountId,
                username = user.readableHandle,
            )
        updateState {
            it.copy(
                currentUserTagIds = currentTags.mapNotNull { tag -> tag.id },
            )
        }
    }

    private fun addUserTag(name: String, color: Int?) {
        viewModelScope.launch {
            val accountId = accountRepository.getActive()?.id ?: return@launch
            val model = UserTagModel(name = name, color = color)
            userTagRepository.create(model = model, accountId = accountId)
            val tags = userTagRepository.getAll(accountId).filter { !it.isSpecial }
            updateState {
                it.copy(availableUserTags = tags)
            }
        }
    }

    private fun updateTags(ids: List<Long>) {
        viewModelScope.launch {
            val accountId = accountRepository.getActive()?.id ?: return@launch
            val username = uiState.value.user.readableHandle
            val currentTagIds =
                userTagRepository
                    .getTags(
                        username = username,
                        accountId = accountId,
                    ).mapNotNull { it.id }

            val idsToRemove = currentTagIds.filter { it !in ids }
            for (id in idsToRemove) {
                userTagRepository.removeMember(
                    username = username,
                    userTagId = id,
                )
            }

            val idsToAdd = ids.filter { it !in currentTagIds }
            for (id in idsToAdd) {
                userTagRepository.addMember(
                    username = username,
                    userTagId = id,
                )
            }
            userTagHelper.clear()
            refreshCurrentUserTags()
        }
    }
}
