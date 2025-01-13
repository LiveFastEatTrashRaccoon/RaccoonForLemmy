package com.livefast.eattrash.raccoonforlemmy.unit.replies

import androidx.compose.runtime.Stable
import cafe.adriel.voyager.core.model.ScreenModel
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.PostLayout
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.VoteFormat
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.ActionOnSwipe
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PersonMentionModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel

@Stable
interface InboxRepliesMviModel :
    MviModel<InboxRepliesMviModel.Intent, InboxRepliesMviModel.UiState, InboxRepliesMviModel.Effect>,
    ScreenModel {
    sealed interface Intent {
        data object Refresh : Intent

        data object LoadNextPage : Intent

        data class MarkAsRead(
            val read: Boolean,
            val id: Long,
        ) : Intent

        data object HapticIndication : Intent

        data class UpVoteComment(
            val id: Long,
        ) : Intent

        data class DownVoteComment(
            val id: Long,
        ) : Intent

        data class WillOpenDetail(
            val id: Long,
            val post: PostModel,
            val commentId: Long,
        ) : Intent
    }

    data class UiState(
        val initial: Boolean = true,
        val refreshing: Boolean = false,
        val loading: Boolean = false,
        val canFetchMore: Boolean = true,
        val unreadOnly: Boolean = true,
        val replies: List<PersonMentionModel> = emptyList(),
        val previewMaxLines: Int? = null,
        val postLayout: PostLayout = PostLayout.Card,
        val swipeActionsEnabled: Boolean = true,
        val autoLoadImages: Boolean = true,
        val preferNicknames: Boolean = true,
        val showScores: Boolean = true,
        val downVoteEnabled: Boolean = true,
        val voteFormat: VoteFormat = VoteFormat.Aggregated,
        val actionsOnSwipeToStartInbox: List<ActionOnSwipe> = emptyList(),
        val actionsOnSwipeToEndInbox: List<ActionOnSwipe> = emptyList(),
    )

    sealed interface Effect {
        data class UpdateUnreadItems(
            val value: Int,
        ) : Effect

        data object BackToTop : Effect

        data class OpenDetail(
            val post: PostModel,
            val commentId: Long,
        ) : Effect
    }
}
