package com.github.diegoberaldin.raccoonforlemmy.feature_profile.viewmodel

import com.github.diegoberaldin.raccoonforlemmy.core_architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.domain_lemmy.data.UserModel

interface ProfileScreenMviModel :
    MviModel<ProfileScreenMviModel.Intent, ProfileScreenMviModel.UiState, ProfileScreenMviModel.Effect> {

    sealed interface Intent {
        object Logout : Intent
    }

    data class UiState(
        val currentUser: UserModel? = null,
    )

    sealed interface Effect
}