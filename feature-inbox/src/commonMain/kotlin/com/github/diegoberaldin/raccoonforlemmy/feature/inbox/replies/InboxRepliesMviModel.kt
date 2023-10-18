package com.github.diegoberaldin.raccoonforlemmy.feature.inbox.replies

import cafe.adriel.voyager.core.model.ScreenModel
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.PostLayout
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.PersonMentionModel

interface InboxRepliesMviModel :
    MviModel<InboxRepliesMviModel.Intent, InboxRepliesMviModel.UiState, InboxRepliesMviModel.Effect>,
    ScreenModel {

    sealed interface Intent {
        data object Refresh : Intent
        data object LoadNextPage : Intent
        data class MarkAsRead(val read: Boolean, val index: Int) : Intent
        data object HapticIndication : Intent
        data class UpVoteComment(val index: Int) : Intent
        data class DownVoteComment(val index: Int) : Intent
    }

    data class UiState(
        val initial: Boolean = true,
        val refreshing: Boolean = false,
        val loading: Boolean = false,
        val canFetchMore: Boolean = true,
        val unreadOnly: Boolean = true,
        val replies: List<PersonMentionModel> = emptyList(),
        val postLayout: PostLayout = PostLayout.Card,
        val swipeActionsEnabled: Boolean = true,
        val autoLoadImages: Boolean = true,
    )

    sealed interface Effect {
        data class UpdateUnreadItems(val value: Int) : Effect
    }
}
