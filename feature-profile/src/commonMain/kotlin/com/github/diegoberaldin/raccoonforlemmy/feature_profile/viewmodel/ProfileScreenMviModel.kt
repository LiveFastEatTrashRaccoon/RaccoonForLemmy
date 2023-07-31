package com.github.diegoberaldin.raccoonforlemmy.feature_profile.viewmodel

import com.github.diegoberaldin.raccoonforlemmy.core_architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.data.UserCounterModel
import com.github.diegoberaldin.raccoonforlemmy.data.UserModel

interface ProfileScreenMviModel :
    MviModel<ProfileScreenMviModel.Intent, ProfileScreenMviModel.UiState, ProfileScreenMviModel.Effect> {

    sealed interface Intent {
        object Logout : Intent
    }

    data class UiState(
        val isLogged: Boolean = false,
        val currentUser: UserModel? = null,
        val currentCounters: UserCounterModel? = null,
    )

    sealed interface Effect
}