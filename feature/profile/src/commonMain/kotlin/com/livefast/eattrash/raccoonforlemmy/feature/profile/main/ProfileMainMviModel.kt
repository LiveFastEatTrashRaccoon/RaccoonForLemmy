package com.livefast.eattrash.raccoonforlemmy.feature.profile.main

import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel

interface ProfileMainMviModel :
    MviModel<ProfileMainMviModel.Intent, ProfileMainMviModel.UiState, ProfileMainMviModel.Effect> {
    sealed interface Intent {
        data object Logout : Intent
    }

    data class UiState(val logged: Boolean? = null, val user: UserModel? = null)

    sealed interface Effect
}
