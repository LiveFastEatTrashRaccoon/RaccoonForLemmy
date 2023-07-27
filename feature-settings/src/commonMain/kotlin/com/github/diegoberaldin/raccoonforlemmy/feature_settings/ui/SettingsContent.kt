package com.github.diegoberaldin.raccoonforlemmy.feature_settings.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.github.diegoberaldin.racconforlemmy.core_utils.onClick
import com.github.diegoberaldin.raccoonforlemmy.core_appearance.data.ThemeState
import com.github.diegoberaldin.raccoonforlemmy.core_appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.feature_settings.viewmodel.SettingsScreenMviModel
import com.github.diegoberaldin.raccoonforlemmy.resources.MR
import dev.icerock.moko.resources.compose.stringResource

@Composable
internal fun SettingsContent(
    uiState: SettingsScreenMviModel.UiState,
    onSelectTheme: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = Spacing.m),
        verticalArrangement = Arrangement.spacedBy(Spacing.xs)
    ) {
        Row(
            modifier = Modifier.onClick {
                onSelectTheme()
            },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(MR.strings.settings_ui_theme)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = uiState.currentTheme.toReadableName()
            )
        }
    }
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