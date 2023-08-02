package com.github.diegoberaldin.raccoonforlemmy.core_appearance.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.runtime.Composable
import com.github.diegoberaldin.raccoonforlemmy.resources.MR
import dev.icerock.moko.resources.compose.stringResource

sealed interface ThemeState {
    object Light : ThemeState
    object Dark : ThemeState
    object Black : ThemeState
}

fun Int.toThemeState() = when (this) {
    2 -> ThemeState.Black
    1 -> ThemeState.Dark
    else -> ThemeState.Light
}

fun ThemeState.toInt() = when (this) {
    ThemeState.Black -> 2
    ThemeState.Dark -> 1
    ThemeState.Light -> 0
}

@Composable
fun ThemeState.toReadableName() = when (this) {
    ThemeState.Black -> stringResource(MR.strings.settings_theme_black)
    ThemeState.Dark -> stringResource(MR.strings.settings_theme_dark)
    ThemeState.Light -> stringResource(MR.strings.settings_theme_light)
}

fun ThemeState.toIcon() = when (this) {
    ThemeState.Black -> Icons.Default.DarkMode
    ThemeState.Dark -> Icons.Outlined.DarkMode
    ThemeState.Light -> Icons.Default.LightMode
}
