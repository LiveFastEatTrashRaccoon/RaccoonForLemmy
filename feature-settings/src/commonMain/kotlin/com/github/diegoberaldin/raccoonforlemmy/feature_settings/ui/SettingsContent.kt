package com.github.diegoberaldin.raccoonforlemmy.feature_settings.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.diegoberaldin.raccoonforlemmy.core_appearance.data.ThemeState
import com.github.diegoberaldin.raccoonforlemmy.core_appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.feature_settings.viewmodel.SettingsScreenMviModel
import com.github.diegoberaldin.raccoonforlemmy.resources.MR
import dev.icerock.moko.resources.compose.stringResource

@Composable
internal fun SettingsContent(
    uiState: SettingsScreenMviModel.UiState,
    onSelectTheme: () -> Unit,
    onSelectLanguage: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = Spacing.m),
        verticalArrangement = Arrangement.spacedBy(Spacing.xs)
    ) {
        // theme
        SettingsRow(
            title = stringResource(MR.strings.settings_ui_theme),
            value = uiState.currentTheme.toReadableName(),
            onTap = {
                onSelectTheme()
            }
        )

        // language
        SettingsRow(
            title = stringResource(MR.strings.settings_language),
            value = uiState.lang.toLanguageName(),
            onTap = {
                onSelectLanguage()
            }
        )
    }
}

@Composable
internal fun String.toLanguageName() = when (this) {
    "it" -> stringResource(MR.strings.language_it)
    else -> stringResource(MR.strings.language_en)
}

@Composable
internal fun ThemeState.toReadableName() = when (this) {
    ThemeState.Black -> stringResource(MR.strings.settings_theme_black)
    ThemeState.Dark -> stringResource(MR.strings.settings_theme_dark)
    ThemeState.Light -> stringResource(MR.strings.settings_theme_light)
}

internal fun ThemeState.toIcon() = when (this) {
    ThemeState.Black -> Icons.Default.DarkMode
    ThemeState.Dark -> Icons.Outlined.DarkMode
    ThemeState.Light -> Icons.Default.LightMode
}