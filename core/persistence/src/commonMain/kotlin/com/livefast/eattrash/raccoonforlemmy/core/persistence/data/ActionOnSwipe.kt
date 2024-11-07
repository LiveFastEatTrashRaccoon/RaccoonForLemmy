package com.livefast.eattrash.raccoonforlemmy.core.persistence.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MarkChatUnread
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.livefast.eattrash.raccoonforlemmy.core.l10n.messages.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.utils.VoteAction
import com.livefast.eattrash.raccoonforlemmy.core.utils.toIcon
import com.livefast.eattrash.raccoonforlemmy.core.utils.toModifier

sealed interface ActionOnSwipeDirection {
    data object ToStart : ActionOnSwipeDirection

    data object ToEnd : ActionOnSwipeDirection
}

sealed interface ActionOnSwipeTarget {
    data object Posts : ActionOnSwipeTarget

    data object Comments : ActionOnSwipeTarget

    data object Inbox : ActionOnSwipeTarget
}

sealed interface ActionOnSwipe {
    data object None : ActionOnSwipe

    data object UpVote : ActionOnSwipe

    data object DownVote : ActionOnSwipe

    data object Reply : ActionOnSwipe

    data object Save : ActionOnSwipe

    data object ToggleRead : ActionOnSwipe

    data object Edit : ActionOnSwipe

    companion object {
        val DEFAULT_SWIPE_TO_START_POSTS =
            listOf(
                UpVote,
                Save,
            )

        val DEFAULT_SWIPE_TO_END_POSTS =
            listOf(
                DownVote,
                Reply,
            )

        val DEFAULT_SWIPE_TO_START_COMMENTS =
            listOf(
                UpVote,
                Save,
            )

        val DEFAULT_SWIPE_TO_END_COMMENTS =
            listOf(
                DownVote,
                Reply,
            )

        val DEFAULT_SWIPE_TO_START_INBOX =
            listOf(
                UpVote,
                ToggleRead,
            )

        val DEFAULT_SWIPE_TO_END_INBOX =
            listOf(
                DownVote,
            )
    }
}

internal fun ActionOnSwipe.toInt(): Int =
    when (this) {
        ActionOnSwipe.None -> 0
        ActionOnSwipe.UpVote -> 1
        ActionOnSwipe.DownVote -> 2
        ActionOnSwipe.Reply -> 3
        ActionOnSwipe.Save -> 4
        ActionOnSwipe.ToggleRead -> 5
        ActionOnSwipe.Edit -> 6
    }

internal fun Int.toActionOnSwipe(): ActionOnSwipe =
    when (this) {
        1 -> ActionOnSwipe.UpVote
        2 -> ActionOnSwipe.DownVote
        3 -> ActionOnSwipe.Reply
        4 -> ActionOnSwipe.Save
        5 -> ActionOnSwipe.ToggleRead
        6 -> ActionOnSwipe.Edit
        else -> ActionOnSwipe.None
    }

@Composable
fun ActionOnSwipe.toReadableName(): String =
    when (this) {
        ActionOnSwipe.DownVote -> LocalStrings.current.actionDownvote
        ActionOnSwipe.None -> ""
        ActionOnSwipe.Reply -> LocalStrings.current.actionReply
        ActionOnSwipe.Save -> LocalStrings.current.actionSave
        ActionOnSwipe.ToggleRead -> LocalStrings.current.actionToggleRead
        ActionOnSwipe.UpVote -> LocalStrings.current.actionUpvote
        ActionOnSwipe.Edit -> LocalStrings.current.postActionEdit
    }

@Composable
fun ActionOnSwipe.toIcon(): ImageVector? =
    when (this) {
        ActionOnSwipe.DownVote -> VoteAction.DownVote.toIcon()
        ActionOnSwipe.None -> null
        ActionOnSwipe.Reply -> Icons.AutoMirrored.Default.Reply
        ActionOnSwipe.Save -> Icons.Default.Bookmark
        ActionOnSwipe.ToggleRead -> Icons.Default.MarkChatUnread
        ActionOnSwipe.UpVote -> VoteAction.UpVote.toIcon()
        ActionOnSwipe.Edit -> Icons.Default.Edit
    }

@Composable
fun ActionOnSwipe.toModifier(): Modifier =
    when (this) {
        ActionOnSwipe.DownVote -> VoteAction.DownVote.toModifier()
        ActionOnSwipe.UpVote -> VoteAction.UpVote.toModifier()
        else -> Modifier
    }
