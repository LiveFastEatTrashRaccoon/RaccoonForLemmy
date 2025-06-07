package com.livefast.eattrash.raccoonforlemmy.unit.about

import cafe.adriel.voyager.core.model.ScreenModel
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModel

interface AboutDialogMviModel :
    MviModel<AboutDialogMviModel.Intent, AboutDialogMviModel.UiState, AboutDialogMviModel.Effect>,
    ScreenModel {
    sealed interface Intent

    data class UiState(val version: String = "")

    sealed interface Effect
}
