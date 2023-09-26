package com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.ThemeState

interface ColorSchemeProvider {

    val supportsDynamicColors: Boolean

    fun getColorScheme(
        theme: ThemeState,
        dynamic: Boolean,
        customSeed: Color? = null,
    ): ColorScheme
}

fun ColorScheme.blackify(): ColorScheme = copy(
    primaryContainer = md_theme_black_primaryContainer,
    onPrimaryContainer = md_theme_black_onPrimaryContainer,
    secondaryContainer = md_theme_black_secondaryContainer,
    onSecondaryContainer = md_theme_black_onSecondaryContainer,
    tertiaryContainer = md_theme_black_tertiaryContainer,
    onTertiaryContainer = md_theme_black_onTertiaryContainer,
    errorContainer = md_theme_black_errorContainer,
    onErrorContainer = md_theme_black_onErrorContainer,
    background = md_theme_black_background,
    onBackground = md_theme_black_onBackground,
    surface = md_theme_black_surface,
    onSurface = md_theme_black_onSurface,
    surfaceVariant = md_theme_black_surfaceVariant,
    onSurfaceVariant = md_theme_black_onSurfaceVariant,
)
