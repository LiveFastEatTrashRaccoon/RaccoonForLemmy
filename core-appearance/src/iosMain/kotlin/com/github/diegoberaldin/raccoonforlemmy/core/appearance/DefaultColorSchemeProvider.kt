package com.github.diegoberaldin.raccoonforlemmy.core.appearance

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.UiTheme
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.BlackColors
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.ColorSchemeProvider
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.DarkColors
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.LightColors
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.blackify
import com.materialkolor.dynamicColorScheme

internal class DefaultColorSchemeProvider : ColorSchemeProvider {

    override val supportsDynamicColors = false

    override fun getColorScheme(
        theme: UiTheme,
        dynamic: Boolean,
        customSeed: Color?,
    ): ColorScheme = when (theme) {
        UiTheme.Dark -> {
            if (customSeed != null) {
                dynamicColorScheme(customSeed, true)
            } else {
                DarkColors
            }
        }

        UiTheme.Black -> {
            if (customSeed != null) {
                dynamicColorScheme(customSeed, true).blackify()
            } else {
                BlackColors
            }
        }

        else -> {
            if (customSeed != null) {
                dynamicColorScheme(customSeed, false)
            } else {
                LightColors
            }
        }
    }
}
