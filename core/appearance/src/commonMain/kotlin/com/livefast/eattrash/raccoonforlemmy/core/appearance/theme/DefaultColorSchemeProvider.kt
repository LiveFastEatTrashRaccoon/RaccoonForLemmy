package com.livefast.eattrash.raccoonforlemmy.core.appearance.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.UiTheme
import org.koin.core.annotation.Single

@Single
internal expect class DefaultColorSchemeProvider : ColorSchemeProvider {
    override val supportsDynamicColors: Boolean

    override fun getColorScheme(
        theme: UiTheme,
        dynamic: Boolean,
        customSeed: Color?,
    ): ColorScheme
}
