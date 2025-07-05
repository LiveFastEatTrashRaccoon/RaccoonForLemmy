package com.livefast.eattrash.raccoonforlemmy.unit.usertags.detail

import androidx.compose.runtime.Stable
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.UserTagMemberModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.UserTagModel

@Stable
interface UserTagDetailMviModel :
    MviModel<UserTagDetailMviModel.Intent, UserTagDetailMviModel.UiState, UserTagDetailMviModel.Effect> {
    sealed interface Intent {
        data object Refresh : Intent

        data class Remove(val username: String) : Intent
    }

    data class UiState(
        val initial: Boolean = true,
        val refreshing: Boolean = false,
        val tag: UserTagModel? = null,
        val users: List<UserTagMemberModel> = emptyList(),
    )

    sealed interface Effect
}
