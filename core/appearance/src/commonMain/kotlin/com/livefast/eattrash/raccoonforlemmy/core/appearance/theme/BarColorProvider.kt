package com.livefast.eattrash.raccoonforlemmy.core.appearance.theme

import androidx.compose.runtime.Composable
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.UiBarTheme
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.UiTheme

interface BarColorProvider {
    @Composable
    fun setBarColorAccordingToTheme(
        theme: UiTheme,
        barTheme: UiBarTheme,
    )
}
