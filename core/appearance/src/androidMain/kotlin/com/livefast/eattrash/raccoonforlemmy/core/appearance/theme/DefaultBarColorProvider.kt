package com.livefast.eattrash.raccoonforlemmy.core.appearance.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.UiBarTheme
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.UiTheme

internal class DefaultBarColorProvider : BarColorProvider {
    override val isBarThemeSupported: Boolean
        get() = Build.VERSION.SDK_INT < Build.VERSION_CODES.VANILLA_ICE_CREAM

    @Composable
    override fun setBarColorAccordingToTheme(
        theme: UiTheme,
        barTheme: UiBarTheme,
    ) {
        val view = LocalView.current
        val isSystemInDarkTheme = isSystemInDarkTheme()
        LaunchedEffect(theme, barTheme) {
            (view.context as? Activity)?.window?.apply {
                val baseColor =
                    when (theme) {
                        UiTheme.Light -> Color.White
                        UiTheme.Black, UiTheme.Dark -> Color.Black
                        UiTheme.Default ->
                            if (isSystemInDarkTheme) {
                                Color.Black
                            } else {
                                Color.White
                            }
                        else -> Color.Black
                    }
                val barColor =
                    when (barTheme) {
                        UiBarTheme.Opaque -> baseColor.copy(alpha = 0.25f)
                        UiBarTheme.Transparent -> baseColor.copy(alpha = 0.01f)
                        else -> baseColor
                    }.toArgb()
                if (isBarThemeSupported) {
                    statusBarColor = barColor
                    navigationBarColor = barColor
                }

                WindowCompat.setDecorFitsSystemWindows(this, false)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.VANILLA_ICE_CREAM) {
                        isStatusBarContrastEnforced = true
                    }
                    isNavigationBarContrastEnforced = true
                }

                WindowCompat.getInsetsController(this, decorView).apply {
                    isAppearanceLightStatusBars =
                        when (theme) {
                            UiTheme.Light -> true
                            else -> false
                        }
                    isAppearanceLightNavigationBars =
                        when (theme) {
                            UiTheme.Light -> true
                            else -> false
                        }
                }
            }
        }
    }
}
