package com.github.diegoberaldin.raccoonforlemmy.feature_settings.viewmodel

import com.github.diegoberaldin.raccoonforlemmy.core_appearance.data.ThemeState
import com.github.diegoberaldin.raccoonforlemmy.core_architecture.MviModel

interface SettingsScreenMviModel :
    MviModel<SettingsScreenMviModel.Intent, SettingsScreenMviModel.UiState, SettingsScreenMviModel.Effect> {

    sealed interface Intent {
        data class ChangeTheme(val value: ThemeState) : Intent
    }

    data class UiState(
        val currentTheme: ThemeState = ThemeState.Light,
    )

    sealed interface Effect
}