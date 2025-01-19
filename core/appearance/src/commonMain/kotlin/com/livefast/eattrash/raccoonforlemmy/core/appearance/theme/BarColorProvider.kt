package com.livefast.eattrash.raccoonforlemmy.core.appearance.theme

import androidx.compose.runtime.Composable
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.UiBarTheme
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.UiTheme

interface BarColorProvider {
    val isBarThemeSupported: Boolean

    @Composable
    fun setBarColorAccordingToTheme(
        theme: UiTheme,
        barTheme: UiBarTheme,
    )
}
