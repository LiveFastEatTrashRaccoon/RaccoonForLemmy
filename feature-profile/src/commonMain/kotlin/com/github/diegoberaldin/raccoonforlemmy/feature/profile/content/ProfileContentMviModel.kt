package com.github.diegoberaldin.raccoonforlemmy.feature.profile.content

import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel

interface ProfileContentMviModel :
    MviModel<ProfileContentMviModel.Intent, ProfileContentMviModel.UiState, ProfileContentMviModel.Effect> {

    sealed interface Intent {
        data object Logout : Intent
    }

    data class UiState(
        val logged: Boolean? = null,
    )

    sealed interface Effect
}
