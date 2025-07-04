package com.livefast.eattrash.raccoonforlemmy.unit.chat

import androidx.compose.runtime.Stable
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PrivateMessageModel

@Stable
interface InboxChatMviModel :
    MviModel<InboxChatMviModel.Intent, InboxChatMviModel.UiState, InboxChatMviModel.Effect> {
    sealed interface Intent {
        data object LoadNextPage : Intent

        data class ImageSelected(val value: ByteArray) : Intent {
            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other == null || this::class != other::class) return false

                other as ImageSelected

                return value.contentEquals(other.value)
            }

            override fun hashCode(): Int = value.contentHashCode()
        }

        data class EditMessage(val value: Long) : Intent

        data class DeleteMessage(val value: Long) : Intent

        data class SubmitNewMessage(val value: String) : Intent
    }

    data class UiState(
        val initial: Boolean = true,
        val refreshing: Boolean = false,
        val loading: Boolean = false,
        val canFetchMore: Boolean = true,
        val currentUserId: Long? = null,
        val otherUserName: String = "",
        val otherUserAvatar: String? = null,
        val messages: List<PrivateMessageModel> = emptyList(),
        val autoLoadImages: Boolean = true,
        val preferNicknames: Boolean = true,
        val editedMessageId: Long? = null,
    )

    sealed interface Effect {
        data class AddImageToText(val url: String) : Effect

        data object ScrollToBottom : Effect
    }
}
