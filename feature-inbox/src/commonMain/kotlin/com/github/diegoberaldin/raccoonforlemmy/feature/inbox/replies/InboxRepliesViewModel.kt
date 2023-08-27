package com.github.diegoberaldin.raccoonforlemmy.feature.inbox.replies

import cafe.adriel.voyager.core.model.ScreenModel
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.SortType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.CommentRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.SiteRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

class InboxRepliesViewModel(
    private val mvi: DefaultMviModel<InboxRepliesMviModel.Intent, InboxRepliesMviModel.UiState, InboxRepliesMviModel.Effect>,
    private val identityRepository: IdentityRepository,
    private val userRepository: UserRepository,
    private val siteRepository: SiteRepository,
) : ScreenModel,
    MviModel<InboxRepliesMviModel.Intent, InboxRepliesMviModel.UiState, InboxRepliesMviModel.Effect> by mvi {

    private var currentPage: Int = 1

    override fun reduce(intent: InboxRepliesMviModel.Intent) {
        when (intent) {
            InboxRepliesMviModel.Intent.LoadNextPage -> loadNextPage()
            InboxRepliesMviModel.Intent.Refresh -> refresh()
            is InboxRepliesMviModel.Intent.ChangeUnreadOnly -> changeUnreadOnly(intent.unread)
        }
    }

    private fun refresh() {
        currentPage = 1
        mvi.updateState { it.copy(canFetchMore = true, refreshing = true) }
        loadNextPage()
    }

    private fun changeUnreadOnly(value: Boolean) {
        mvi.updateState { it.copy(unreadOnly = value) }
        refresh()
    }

    private fun loadNextPage() {
        val currentState = mvi.uiState.value
        if (!currentState.canFetchMore || currentState.loading) {
            mvi.updateState { it.copy(refreshing = false) }
            return
        }

        mvi.scope.launch(Dispatchers.IO) {
            mvi.updateState { it.copy(loading = true) }
            val auth = identityRepository.authToken.value
            val currentUser = siteRepository.getCurrentUser(auth.orEmpty())
            val refreshing = currentState.refreshing
            val unreadOnly = currentState.unreadOnly
            val itemList = userRepository.getReplies(
                auth = auth,
                page = currentPage,
                unreadOnly = unreadOnly,
                sort = SortType.New,
            ).map {
                val isOwnPost = it.post.creator?.id == currentUser?.id
                it.copy(isOwnPost = isOwnPost)
            }

            currentPage++
            val canFetchMore = itemList.size >= CommentRepository.DEFAULT_PAGE_SIZE
            mvi.updateState {
                val newItems = if (refreshing) {
                    itemList
                } else {
                    it.mentions + itemList
                }
                it.copy(
                    mentions = newItems,
                    loading = false,
                    canFetchMore = canFetchMore,
                    refreshing = false,
                )
            }
        }
    }
}
