package com.github.diegoberaldin.raccoonforlemmy.core.appearance

import android.content.Context
import android.os.Build
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.ThemeState
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.BlackColors
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.ColorSchemeProvider
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.DarkColors
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.LightColors
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.md_theme_black_background
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.md_theme_black_errorContainer
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.md_theme_black_onBackground
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.md_theme_black_onErrorContainer
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.md_theme_black_onPrimaryContainer
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.md_theme_black_onSecondaryContainer
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.md_theme_black_onSurface
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.md_theme_black_onSurfaceVariant
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.md_theme_black_onTertiaryContainer
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.md_theme_black_primaryContainer
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.md_theme_black_secondaryContainer
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.md_theme_black_surface
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.md_theme_black_surfaceVariant
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.md_theme_black_tertiaryContainer

internal class DefaultColorSchemeProvider(private val context: Context) : ColorSchemeProvider {

    override val supportsDynamicColors: Boolean
        get() {
            return Build.VERSION.SDK_INT >= 31
        }

    override fun getColorScheme(theme: ThemeState, dynamic: Boolean): ColorScheme {
        return when (theme) {
            ThemeState.Dark -> if (dynamic) dynamicDarkColorScheme(context) else DarkColors
            ThemeState.Black -> if (dynamic) dynamicDarkColorScheme(context).copy(
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

                ) else BlackColors

            else -> if (dynamic) dynamicLightColorScheme(context) else LightColors
        }
    }
}