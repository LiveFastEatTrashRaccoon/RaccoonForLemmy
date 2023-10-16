package com.github.diegoberaldin.raccoonforlemmy.core.appearance.data

import androidx.compose.runtime.Composable

sealed interface UiFontFamily {
    data object TitilliumWeb : UiFontFamily
    data object Roboto : UiFontFamily
    data object EbGaramond : UiFontFamily
}

fun Int.toUiFontFamily() = when (this) {
    2 -> UiFontFamily.EbGaramond
    1 -> UiFontFamily.Roboto
    else -> UiFontFamily.TitilliumWeb
}

fun UiFontFamily.toInt() = when (this) {
    UiFontFamily.EbGaramond -> 2
    UiFontFamily.Roboto -> 1
    else -> 0
}

@Composable
fun UiFontFamily.toReadableName() = when (this) {
    UiFontFamily.EbGaramond -> "EB Garamond"
    UiFontFamily.Roboto -> "Roboto"
    else -> "Titillium Web"
}
