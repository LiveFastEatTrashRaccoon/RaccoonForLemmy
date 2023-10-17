package com.github.diegoberaldin.raccoonforlemmy.core.appearance.data

import androidx.compose.runtime.Composable

sealed interface UiFontFamily {
    data object TitilliumWeb : UiFontFamily
    data object Ubuntu : UiFontFamily
    data object CormorantGaramond : UiFontFamily
    data object NotoSans : UiFontFamily
    data object CrimsonText : UiFontFamily
}

fun Int.toUiFontFamily() = when (this) {
    4 -> UiFontFamily.CrimsonText
    3 -> UiFontFamily.NotoSans
    2 -> UiFontFamily.CormorantGaramond
    1 -> UiFontFamily.Ubuntu
    else -> UiFontFamily.TitilliumWeb
}

fun UiFontFamily.toInt() = when (this) {
    UiFontFamily.CrimsonText -> 4
    UiFontFamily.NotoSans -> 3
    UiFontFamily.CormorantGaramond -> 2
    UiFontFamily.Ubuntu -> 1
    else -> 0
}

@Composable
fun UiFontFamily.toReadableName() = when (this) {
    UiFontFamily.CrimsonText -> "Crimson Text"
    UiFontFamily.NotoSans -> "Noto Sans"
    UiFontFamily.CormorantGaramond -> "Cormorant Garamond"
    UiFontFamily.Ubuntu -> "Ubuntu"
    else -> "Titillium Web"
}
