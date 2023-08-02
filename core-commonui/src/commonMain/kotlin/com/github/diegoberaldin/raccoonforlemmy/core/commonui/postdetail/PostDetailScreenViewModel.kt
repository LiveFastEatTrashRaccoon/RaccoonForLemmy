package com.github.diegoberaldin.raccoonforlemmy.core.commonui.postdetail

import cafe.adriel.voyager.core.model.ScreenModel
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.core.preferences.KeyStoreKeys
import com.github.diegoberaldin.raccoonforlemmy.core.preferences.TemporaryKeyStore
import com.github.diegoberaldin.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.PostModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.toSortType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.CommentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

class PostDetailScreenViewModel(
    private val mvi: DefaultMviModel<PostDetailScreenMviModel.Intent, PostDetailScreenMviModel.UiState, PostDetailScreenMviModel.Effect>,
    private val post: PostModel,
    private val identityRepository: IdentityRepository,
    private val commentRepository: CommentRepository,
    private val keyStore: TemporaryKeyStore,
) : MviModel<PostDetailScreenMviModel.Intent, PostDetailScreenMviModel.UiState, PostDetailScreenMviModel.Effect> by mvi,
    ScreenModel {
    private var currentPage: Int = 1
    override fun onStarted() {
        mvi.onStarted()
        refresh()
    }

    override fun reduce(intent: PostDetailScreenMviModel.Intent) {
        when (intent) {
            PostDetailScreenMviModel.Intent.LoadNextPage -> loadNextPage()
            PostDetailScreenMviModel.Intent.Refresh -> refresh()
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
            val commentList = commentRepository.getComments(
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
}
