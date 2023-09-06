package com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme

import androidx.compose.material3.ColorScheme
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.ThemeState

interface ColorSchemeProvider {

    val supportsDynamicColors: Boolean
    fun getColorScheme(theme: ThemeState, dynamic: Boolean): ColorScheme
}