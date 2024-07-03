package com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.github.diegoberaldin.raccoonforlemmy.core.l10n.messages.LocalStrings
import kotlin.random.Random

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

interface AppColorRepository {
    fun getColors(): List<AppColor>

    fun getRandomColor(): AppColor
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
        AppColor.Green -> LocalStrings.current.settingsColorAquamarine
        AppColor.Gray -> LocalStrings.current.settingsColorPurple
        AppColor.LightBlue -> LocalStrings.current.settingsColorGreen
        AppColor.Orange -> LocalStrings.current.settingsColorRed
        AppColor.Pink -> LocalStrings.current.settingsColorOrange
        AppColor.Purple -> LocalStrings.current.settingsColorBanana
        AppColor.Red -> LocalStrings.current.settingsColorPink
        AppColor.White -> LocalStrings.current.settingsColorGray
        AppColor.Yellow -> LocalStrings.current.settingsColorWhite
    }

internal class DefaultAppColorRepository : AppColorRepository {
    override fun getColors(): List<AppColor> =
        listOf(
            AppColor.Blue,
            AppColor.LightBlue,
            AppColor.Purple,
            AppColor.Green,
            AppColor.Red,
            AppColor.Orange,
            AppColor.Yellow,
            AppColor.Pink,
            AppColor.Gray,
            AppColor.White,
        )

    override fun getRandomColor(): AppColor {
        val index = Random.nextInt(10)
        return getColors()[index]
    }
}
