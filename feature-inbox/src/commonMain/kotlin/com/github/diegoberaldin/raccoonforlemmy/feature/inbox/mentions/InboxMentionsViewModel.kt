package com.github.diegoberaldin.raccoonforlemmy.feature.inbox.mentions

import cafe.adriel.voyager.core.model.ScreenModel
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.SortType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.CommentRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

class InboxMentionsViewModel(
    private val mvi: DefaultMviModel<InboxMentionsMviModel.Intent, InboxMentionsMviModel.UiState, InboxMentionsMviModel.Effect>,
    private val identityRepository: IdentityRepository,
    private val userRepository: UserRepository,
) : ScreenModel,
    MviModel<InboxMentionsMviModel.Intent, InboxMentionsMviModel.UiState, InboxMentionsMviModel.Effect> by mvi {

    private var currentPage: Int = 1

    override fun reduce(intent: InboxMentionsMviModel.Intent) {
        when (intent) {
            InboxMentionsMviModel.Intent.LoadNextPage -> loadNextPage()
            InboxMentionsMviModel.Intent.Refresh -> refresh()
            is InboxMentionsMviModel.Intent.ChangeUnreadOnly -> changeUnreadOnly(intent.unread)
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
            val refreshing = currentState.refreshing
            val unreadOnly = currentState.unreadOnly
            val itemList = userRepository.getMentions(
                auth = auth,
                page = currentPage,
                unreadOnly = unreadOnly,
                sort = SortType.New,
            )
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
