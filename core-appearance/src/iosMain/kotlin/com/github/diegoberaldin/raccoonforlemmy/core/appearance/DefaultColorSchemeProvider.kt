package com.github.diegoberaldin.raccoonforlemmy.core.appearance

import androidx.compose.material3.ColorScheme
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.ThemeState
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.BlackColors
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.ColorSchemeProvider
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.DarkColors
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.LightColors

internal class DefaultColorSchemeProvider : ColorSchemeProvider {

    override val supportsDynamicColors = false

    override fun getColorScheme(theme: ThemeState, dynamic: Boolean): ColorScheme {
        return when (theme) {
            ThemeState.Dark -> DarkColors
            ThemeState.Black -> BlackColors
            else -> LightColors
        }
    }
}