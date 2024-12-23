package com.livefast.eattrash.raccoonforlemmy.core.appearance.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings

sealed interface AppColor {
    data object Blue : AppColor

    data object LightBlue : AppColor

    data object Purple : AppColor

    data object Green : AppColor

    data object Red : AppColor

    data object Orange : AppColor

    data object Yellow : AppColor

    data object Pink : AppColor

    data object Gray : AppColor

    data object White : AppColor
}

fun AppColor.toColor(): Color =
    when (this) {
        AppColor.Blue -> Color(0xFF001F7D)
        AppColor.LightBlue -> Color(0xFF36B3B3)
        AppColor.Purple -> Color(0xFF884DFF)
        AppColor.Green -> Color(0xFF00B300)
        AppColor.Red -> Color(0xFFFF0000)
        AppColor.Orange -> Color(0xFFFF66600)
        AppColor.Yellow -> Color(0x94786818)
        AppColor.Pink -> Color(0xFFFC0FC0)
        AppColor.Gray -> Color(0xFF303B47)
        AppColor.White -> Color(0xFFD7D7D7)
    }

@Composable
fun AppColor.toReadableName(): String =
    when (this) {
        AppColor.Blue -> LocalStrings.current.settingsColorBlue
        AppColor.LightBlue -> LocalStrings.current.settingsColorAquamarine
        AppColor.Purple -> LocalStrings.current.settingsColorPurple
        AppColor.Green -> LocalStrings.current.settingsColorGreen
        AppColor.Red -> LocalStrings.current.settingsColorRed
        AppColor.Orange -> LocalStrings.current.settingsColorOrange
        AppColor.Yellow -> LocalStrings.current.settingsColorBanana
        AppColor.Gray -> LocalStrings.current.settingsColorGray
        AppColor.Pink -> LocalStrings.current.settingsColorPink
        AppColor.White -> LocalStrings.current.settingsColorWhite
    }
