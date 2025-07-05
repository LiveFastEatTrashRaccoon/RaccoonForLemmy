package com.livefast.eattrash.raccoonforlemmy.unit.messages

import androidx.compose.runtime.Stable
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PrivateMessageModel

@Stable
interface InboxMessagesMviModel :
    MviModel<InboxMessagesMviModel.Intent, InboxMessagesMviModel.UiState, InboxMessagesMviModel.Effect> {
    sealed interface Intent {
        data object Refresh : Intent

        data object LoadNextPage : Intent
    }

    data class UiState(
        val initial: Boolean = true,
        val refreshing: Boolean = false,
        val loading: Boolean = false,
        val canFetchMore: Boolean = true,
        val unreadOnly: Boolean = true,
        val currentUserId: Long = 0,
        val chats: List<PrivateMessageModel> = emptyList(),
        val autoLoadImages: Boolean = true,
        val preferNicknames: Boolean = true,
    )

    sealed interface Effect {
        data class UpdateUnreadItems(val value: Int) : Effect

        data object BackToTop : Effect
    }
}
