package com.github.diegoberaldin.raccoonforlemmy.feature_profile.viewmodel

import com.github.diegoberaldin.raccoonforlemmy.core_architecture.MviModel

interface ProfileScreenMviModel :
    MviModel<ProfileScreenMviModel.Intent, ProfileScreenMviModel.UiState, ProfileScreenMviModel.Effect> {

    sealed interface Intent

    data class UiState(val isLogged: Boolean = false)

    sealed interface Effect
}