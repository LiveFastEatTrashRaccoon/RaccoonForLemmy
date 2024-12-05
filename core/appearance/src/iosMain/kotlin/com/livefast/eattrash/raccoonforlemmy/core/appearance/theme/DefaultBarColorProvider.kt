package com.livefast.eattrash.raccoonforlemmy.core.appearance.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.UiBarTheme
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.UiTheme
import org.koin.core.annotation.Single
import platform.UIKit.UIApplication
import platform.UIKit.UIStatusBarStyleDarkContent
import platform.UIKit.UIStatusBarStyleLightContent
import platform.UIKit.setStatusBarStyle

@Single
internal actual class DefaultBarColorProvider : BarColorProvider {
    @Composable
    actual override fun setBarColorAccordingToTheme(
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
