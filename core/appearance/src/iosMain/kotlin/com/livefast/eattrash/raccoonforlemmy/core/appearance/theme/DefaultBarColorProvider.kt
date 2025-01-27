package com.livefast.eattrash.raccoonforlemmy.core.appearance.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.UiBarTheme
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.UiTheme
import platform.UIKit.UIApplication
import platform.UIKit.UIStatusBarStyleDarkContent
import platform.UIKit.UIStatusBarStyleLightContent
import platform.UIKit.setStatusBarStyle

internal class DefaultBarColorProvider : BarColorProvider {
    override val isBarThemeSupported = false
    override val isOpaqueThemeSupported = false

    @Composable
    override fun setBarColorAccordingToTheme(
        theme: UiTheme,
        barTheme: UiBarTheme,
    ) {
        LaunchedEffect(theme) {
            val style =
                when {
                    theme == UiTheme.Light -> UIStatusBarStyleLightContent
                    else -> UIStatusBarStyleDarkContent
                }
            UIApplication.sharedApplication().setStatusBarStyle(style)
        }
    }
}
