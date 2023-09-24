package com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.ThemeState

interface ColorSchemeProvider {

    val supportsDynamicColors: Boolean

    fun getColorScheme(
        theme: ThemeState,
        dynamic: Boolean,
        customPrimary: Color? = null,
        customSecondary: Color? = null,
        customTertiary: Color? = null,
    ): ColorScheme
}

internal fun ColorScheme.applyCustom(
    customPrimary: Color? = null,
    customSecondary: Color? = null,
    customTertiary: Color? = null,
): ColorScheme = copy(
    primary = customPrimary ?: primary,
    secondary = customSecondary ?: secondary,
    tertiary = customTertiary ?: tertiary,
)