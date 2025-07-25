package com.livefast.eattrash.raccoonforlemmy.core.appearance.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.UiTheme
import com.materialkolor.PaletteStyle
import com.materialkolor.dynamicColorScheme

internal class DefaultColorSchemeProvider : ColorSchemeProvider {
    override val supportsDynamicColors = false

    override fun getColorScheme(
        theme: UiTheme,
        dynamic: Boolean,
        customSeed: Color?,
        isSystemInDarkTheme: Boolean,
    ): ColorScheme =
        when (theme) {
            UiTheme.Dark -> {
                if (customSeed != null) {
                    dynamicColorScheme(
                        seedColor = customSeed,
                        isDark = true,
                        isAmoled = false,
                        style = PALETTE_STYLE,
                    )
                } else {
                    DarkColors
                }
            }

            UiTheme.Black -> {
                if (customSeed != null) {
                    dynamicColorScheme(
                        seedColor = customSeed,
                        isDark = true,
                        isAmoled = true,
                        style = PALETTE_STYLE,
                    )
                } else {
                    BlackColors
                }
            }

            else -> {
                if (customSeed != null) {
                    dynamicColorScheme(
                        seedColor = customSeed,
                        isDark = false,
                        isAmoled = false,
                        style = PALETTE_STYLE,
                    )
                } else {
                    if (isSystemInDarkTheme) {
                        DarkColors
                    } else {
                        LightColors
                    }
                }
            }
        }
}

private val PALETTE_STYLE = PaletteStyle.Expressive
