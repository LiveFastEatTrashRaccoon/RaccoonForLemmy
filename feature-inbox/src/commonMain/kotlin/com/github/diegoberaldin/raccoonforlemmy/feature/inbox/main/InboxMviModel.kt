package com.github.diegoberaldin.raccoonforlemmy.feature.inbox.main

import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel

interface InboxMviModel :
    MviModel<InboxMviModel.Intent, InboxMviModel.UiState, InboxMviModel.Effect> {

    sealed interface Intent {
        data class ChangeSection(val value: InboxSection) : Intent
        data class ChangeUnreadOnly(val unread: Boolean) : Intent
        object ReadAll : Intent
    }

    data class UiState(
        val currentUserId: Int? = null,
        val section: InboxSection = InboxSection.REPLIES,
        val unreadOnly: Boolean = true,
    )

    sealed interface Effect {
        object Refresh : Effect
    }
}
