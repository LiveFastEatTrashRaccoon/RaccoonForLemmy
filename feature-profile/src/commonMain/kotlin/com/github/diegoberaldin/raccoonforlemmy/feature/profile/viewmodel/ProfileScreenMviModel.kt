package com.github.diegoberaldin.raccoonforlemmy.feature.profile.viewmodel

import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.UserModel

interface ProfileScreenMviModel :
    MviModel<ProfileScreenMviModel.Intent, ProfileScreenMviModel.UiState, ProfileScreenMviModel.Effect> {

    sealed interface Intent {
        object Logout : Intent
    }

    data class UiState(
        val initial: Boolean = true,
        val currentUser: UserModel? = null,
    )

    sealed interface Effect
}
