package com.livefast.eattrash.raccoonforlemmy.unit.usertags.list

import androidx.compose.runtime.Stable
import cafe.adriel.voyager.core.model.ScreenModel
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.UserTagModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.UserTagType

@Stable
interface UserTagsMviModel :
    MviModel<UserTagsMviModel.Intent, UserTagsMviModel.UiState, UserTagsMviModel.Effect>,
    ScreenModel {
    sealed interface Intent {
        data object Refresh : Intent

        data class Delete(val id: Long) : Intent

        data class Add(val name: String, val color: Int? = null) : Intent

        data class Edit(val id: Long, val name: String, val type: UserTagType, val color: Int? = null) : Intent
    }

    data class UiState(
        val initial: Boolean = true,
        val refreshing: Boolean = false,
        val specialTags: List<UserTagModel> = emptyList(),
        val regularTags: List<UserTagModel> = emptyList(),
    )

    sealed interface Effect {
        data object BackToTop : Effect
    }
}
