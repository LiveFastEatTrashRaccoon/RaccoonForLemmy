package com.github.diegoberaldin.raccoonforlemmy.core.commonui.postdetail

import cafe.adriel.voyager.core.model.ScreenModel
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.core.notifications.NotificationCenter
import com.github.diegoberaldin.raccoonforlemmy.core.preferences.KeyStoreKeys
import com.github.diegoberaldin.raccoonforlemmy.core.preferences.TemporaryKeyStore
import com.github.diegoberaldin.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommentModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.PostModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.toSortType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.CommentRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.PostsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

class PostDetailScreenViewModel(
    private val mvi: DefaultMviModel<PostDetailScreenMviModel.Intent, PostDetailScreenMviModel.UiState, PostDetailScreenMviModel.Effect>,
    private val post: PostModel,
    private val identityRepository: IdentityRepository,
    private val postsRepository: PostsRepository,
    private val commentRepository: CommentRepository,
    private val keyStore: TemporaryKeyStore,
    private val notificationCenter: NotificationCenter,
) : MviModel<PostDetailScreenMviModel.Intent, PostDetailScreenMviModel.UiState, PostDetailScreenMviModel.Effect> by mvi,
    ScreenModel {
    private var currentPage: Int = 1
    override fun onStarted() {
        mvi.onStarted()
        mvi.updateState { it.copy(post = post) }
        refresh()
    }

    override fun reduce(intent: PostDetailScreenMviModel.Intent) {
        when (intent) {
            PostDetailScreenMviModel.Intent.LoadNextPage -> loadNextPage()
            PostDetailScreenMviModel.Intent.Refresh -> refresh()
            is PostDetailScreenMviModel.Intent.DownVoteComment -> downVoteComment(
                intent.comment,
                intent.value,
            )

            is PostDetailScreenMviModel.Intent.DownVotePost -> downVotePost(
                intent.post,
                intent.value,
            )

            is PostDetailScreenMviModel.Intent.SaveComment -> saveComment(
                intent.comment,
                intent.value,
            )

            is PostDetailScreenMviModel.Intent.SavePost -> savePost(
                intent.post,
                intent.value,
            )

            is PostDetailScreenMviModel.Intent.UpVoteComment -> upVoteComment(
                intent.comment,
                intent.value,
            )

            is PostDetailScreenMviModel.Intent.UpVotePost -> upVotePost(
                intent.post,
                intent.value,
            )
        }
    }

    private fun refresh() {
        currentPage = 1
        mvi.updateState { it.copy(canFetchMore = true, refreshing = true) }
        loadNextPage()
    }

    private fun loadNextPage() {
        val currentState = mvi.uiState.value
        if (!currentState.canFetchMore || currentState.loading) {
            return
        }

        mvi.scope.launch(Dispatchers.IO) {
            mvi.updateState { it.copy(loading = true) }
            val auth = identityRepository.authToken.value
            val refreshing = currentState.refreshing
            val sort = keyStore[KeyStoreKeys.DefaultCommentSortType, 3].toSortType()
            val commentList = commentRepository.getAll(
                auth = auth,
                postId = post.id,
                page = currentPage,
                sort = sort,
            )
            currentPage++
            val canFetchMore = commentList.size >= CommentRepository.DEFAULT_PAGE_SIZE
            mvi.updateState {
                val newcomments = if (refreshing) {
                    commentList
                } else {
                    it.comments + commentList
                }
                it.copy(
                    comments = newcomments,
                    loading = false,
                    canFetchMore = canFetchMore,
                    refreshing = false,
                )
            }
        }
    }

    private fun upVotePost(post: PostModel, value: Boolean) {
        mvi.scope.launch(Dispatchers.IO) {
            val auth = identityRepository.authToken.value.orEmpty()
            val newPost = postsRepository.upVote(
                auth = auth,
                post = post,
                voted = value,
            )
            mvi.updateState { it.copy(post = newPost) }
            notificationCenter.send(NotificationCenter.Event.PostUpdate(newPost))
        }
    }

    private fun downVotePost(post: PostModel, value: Boolean) {
        mvi.scope.launch(Dispatchers.IO) {
            val auth = identityRepository.authToken.value.orEmpty()
            val newPost = postsRepository.downVote(
                auth = auth,
                post = post,
                downVoted = value,
            )
            mvi.updateState { it.copy(post = newPost) }
            notificationCenter.send(NotificationCenter.Event.PostUpdate(newPost))
        }
    }

    private fun savePost(post: PostModel, value: Boolean) {
        mvi.scope.launch(Dispatchers.IO) {
            val auth = identityRepository.authToken.value.orEmpty()
            val newPost = postsRepository.save(
                auth = auth,
                post = post,
                saved = value,
            )
            mvi.updateState { it.copy(post = newPost) }
            notificationCenter.send(NotificationCenter.Event.PostUpdate(newPost))
        }
    }

    private fun upVoteComment(comment: CommentModel, value: Boolean) {
        mvi.scope.launch(Dispatchers.IO) {
            val auth = identityRepository.authToken.value.orEmpty()
            val newComment = commentRepository.upVote(
                auth = auth,
                comment = comment,
                voted = value,
            )
            mvi.updateState {
                it.copy(
                    comments = it.comments.map { c ->
                        if (c.id == comment.id) {
                            newComment
                        } else {
                            c
                        }
                    },
                )
            }
            notificationCenter.send(NotificationCenter.Event.CommentUpdate(newComment))
        }
    }

    private fun downVoteComment(comment: CommentModel, value: Boolean) {
        mvi.scope.launch(Dispatchers.IO) {
            val auth = identityRepository.authToken.value.orEmpty()
            val newComment = commentRepository.downVote(
                auth = auth,
                comment = comment,
                downVoted = value,
            )
            mvi.updateState {
                it.copy(
                    comments = it.comments.map { c ->
                        if (c.id == comment.id) {
                            newComment
                        } else {
                            c
                        }
                    },
                )
            }
            notificationCenter.send(NotificationCenter.Event.CommentUpdate(newComment))
        }
    }

    private fun saveComment(comment: CommentModel, value: Boolean) {
        mvi.scope.launch(Dispatchers.IO) {
            val auth = identityRepository.authToken.value.orEmpty()
            val newComment = commentRepository.save(
                auth = auth,
                comment = comment,
                saved = value,
            )
            mvi.updateState {
                it.copy(
                    comments = it.comments.map { c ->
                        if (c.id == comment.id) {
                            newComment
                        } else {
                            c
                        }
                    },
                )
            }
            notificationCenter.send(NotificationCenter.Event.CommentUpdate(newComment))
        }
    }
}
