package com.livefast.eattrash.raccoonforlemmy.core.appearance.theme

import androidx.compose.runtime.Composable
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.UiBarTheme
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.UiTheme
import org.koin.core.annotation.Single

@Single
internal expect class DefaultBarColorProvider : BarColorProvider {
    @Composable
    override fun setBarColorAccordingToTheme(
        theme: UiTheme,
        barTheme: UiBarTheme,
    )
}
