package com.livefast.eattrash.raccoonforlemmy.unit.configureswipeactions

import androidx.compose.runtime.Stable
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.ActionOnSwipe
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.ActionOnSwipeDirection
import com.livefast.eattrash.raccoonforlemmy.unit.configureswipeactions.ConfigureSwipeActionsMviModel.Effect
import com.livefast.eattrash.raccoonforlemmy.unit.configureswipeactions.ConfigureSwipeActionsMviModel.Intent
import com.livefast.eattrash.raccoonforlemmy.unit.configureswipeactions.ConfigureSwipeActionsMviModel.UiState

@Stable
interface ConfigureSwipeActionsMviModel : MviModel<Intent, UiState, Effect> {
    sealed interface Intent {
        data object ResetActionsPosts : Intent

        data class DeleteActionPosts(val value: ActionOnSwipe, val direction: ActionOnSwipeDirection) :
            Intent

        data object ResetActionsComments : Intent

        data class DeleteActionComments(val value: ActionOnSwipe, val direction: ActionOnSwipeDirection) :
            Intent

        data object ResetActionsInbox : Intent

        data class DeleteActionInbox(val value: ActionOnSwipe, val direction: ActionOnSwipeDirection) :
            Intent
    }

    data class UiState(
        val actionsOnSwipeToStartPosts: List<ActionOnSwipe> = emptyList(),
        val actionsOnSwipeToEndPosts: List<ActionOnSwipe> = emptyList(),
        val availableOptionsPosts: List<ActionOnSwipe> = emptyList(),
        val actionsOnSwipeToStartComments: List<ActionOnSwipe> = emptyList(),
        val actionsOnSwipeToEndComments: List<ActionOnSwipe> = emptyList(),
        val availableOptionsComments: List<ActionOnSwipe> = emptyList(),
        val actionsOnSwipeToStartInbox: List<ActionOnSwipe> = emptyList(),
        val actionsOnSwipeToEndInbox: List<ActionOnSwipe> = emptyList(),
        val availableOptionsInbox: List<ActionOnSwipe> = emptyList(),
        val downVoteEnabled: Boolean = true,
    )

    sealed interface Effect
}
