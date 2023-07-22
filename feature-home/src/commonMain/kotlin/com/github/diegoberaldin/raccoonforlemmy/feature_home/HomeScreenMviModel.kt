package com.github.diegoberaldin.raccoonforlemmy.feature_home

import com.github.diegoberaldin.raccoonforlemmy.core_architecture.MviModel

interface HomeScreenMviModel :
    MviModel<HomeScreenMviModel.Intent, HomeScreenMviModel.UiState, HomeScreenMviModel.Effect> {

    sealed interface Intent

    data class UiState(
        val loading: Boolean = false,
    )

    sealed interface Effect
}