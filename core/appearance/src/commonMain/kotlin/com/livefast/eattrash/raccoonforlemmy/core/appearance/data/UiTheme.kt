package com.livefast.eattrash.raccoonforlemmy.core.appearance.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.livefast.eattrash.raccoonforlemmy.core.l10n.messages.LocalStrings

sealed interface UiTheme {
    data object Light : UiTheme

    data object Dark : UiTheme

    data object Black : UiTheme

    data object Default : UiTheme
}

fun Int.toUiTheme(): UiTheme =
    when (this) {
        3 -> UiTheme.Black
        2 -> UiTheme.Dark
        1 -> UiTheme.Light
        else -> UiTheme.Default
    }

fun UiTheme.toInt(): Int =
    when (this) {
        UiTheme.Black -> 3
        UiTheme.Dark -> 2
        UiTheme.Light -> 1
        else -> 0
    }

@Composable
fun UiTheme?.toReadableName(): String =
    when (this) {
        UiTheme.Black -> LocalStrings.current.settingsThemeBlack
        UiTheme.Dark -> LocalStrings.current.settingsThemeDark
        UiTheme.Light -> LocalStrings.current.settingsThemeLight
        else -> LocalStrings.current.settingsFontFamilyDefault
    }

fun UiTheme.toIcon(): ImageVector =
    when (this) {
        UiTheme.Black -> Icons.Default.DarkMode
        UiTheme.Dark -> Icons.Outlined.DarkMode
        UiTheme.Light -> Icons.Default.LightMode
        else -> Icons.Default.Smartphone
    }
