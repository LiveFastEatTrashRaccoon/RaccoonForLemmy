package com.livefast.eattrash.raccoonforlemmy.unit.about

import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModel

interface AboutDialogMviModel :
    MviModel<AboutDialogMviModel.Intent, AboutDialogMviModel.UiState, AboutDialogMviModel.Effect> {
    sealed interface Intent

    data class UiState(val version: String = "")

    sealed interface Effect
}
