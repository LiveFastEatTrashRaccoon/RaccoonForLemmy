package com.github.diegoberaldin.raccoonforlemmy

import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel

interface MainScreenMviModel :
    MviModel<MainScreenMviModel.Intent, MainScreenMviModel.UiState, MainScreenMviModel.Effect> {

    sealed interface Intent {
        data class SetBottomBarOffsetHeightPx(val value: Float) : Intent
    }

    data class UiState(
        val bottomBarOffsetHeightPx: Float = 0f,
    )

    sealed interface Effect {
        data class UnreadItemsDetected(val value: Int) : Effect
    }
}
