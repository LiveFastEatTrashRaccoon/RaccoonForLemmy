package com.github.diegoberaldin.raccoonforlemmy.feature_settings

import com.github.diegoberaldin.raccoonforlemmy.core_architecture.MviModel

interface SettingsScreenMviModel :
    MviModel<SettingsScreenMviModel.Intent, SettingsScreenMviModel.UiState, SettingsScreenMviModel.Effect> {

    sealed interface Intent {
        data class EnableDarkMode(val value: Boolean) : Intent
    }

    data class UiState(
        val darkTheme: Boolean = false,
    )

    sealed interface Effect
}