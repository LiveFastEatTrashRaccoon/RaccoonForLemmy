package com.livefast.eattrash.raccoonforlemmy.core.appearance.data

import androidx.compose.runtime.Composable
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings

sealed interface UiBarTheme {
    data object Solid : UiBarTheme

    data object Transparent : UiBarTheme

    data object Opaque : UiBarTheme
}

fun UiBarTheme?.toInt(): Int =
    when (this) {
        UiBarTheme.Solid -> 1
        UiBarTheme.Opaque -> 2
        else -> 0
    }

fun Int.toUiBarTheme(): UiBarTheme =
    when (this) {
        2 -> UiBarTheme.Opaque
        1 -> UiBarTheme.Solid
        else -> UiBarTheme.Transparent
    }

@Composable
fun UiBarTheme?.toReadableName(): String =
    when (this) {
        UiBarTheme.Transparent -> LocalStrings.current.barThemeTransparent
        UiBarTheme.Opaque -> LocalStrings.current.barThemeOpaque
        UiBarTheme.Solid -> LocalStrings.current.barThemeSolid
        else -> ""
    }
