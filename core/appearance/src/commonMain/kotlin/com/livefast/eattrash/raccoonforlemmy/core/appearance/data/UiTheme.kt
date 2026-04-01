package com.livefast.eattrash.raccoonforlemmy.core.appearance.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.resources.LocalResources

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

@Composable
fun UiTheme.toIcon(): ImageVector =
    when (this) {
        UiTheme.Black -> LocalResources.current.darkModeFill
        UiTheme.Dark -> LocalResources.current.darkMode
        UiTheme.Light -> LocalResources.current.lightMode
        else -> LocalResources.current.computer
    }
