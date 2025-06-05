package com.livefast.eattrash.raccoonforlemmy.core.appearance.theme

import android.app.Activity
import android.content.res.Configuration
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

internal class DefaultBarColorProvider :
    BarColorProvider,
    SolidBarColorWorkaround {
    override val isBarThemeSupported = true
    override val isOpaqueThemeSupported: Boolean
        get() {
            return Build.VERSION.SDK_INT < Build.VERSION_CODES.VANILLA_ICE_CREAM
        }

    @Composable
    override fun setBarColorAccordingToTheme(
        theme: UiTheme,
        barTheme: UiBarTheme,
    ) {
        val view = LocalView.current
        val isSystemInDarkTheme = isSystemInDarkTheme()
        LaunchedEffect(theme, barTheme) {
            (view.context as? Activity)?.window?.apply {
                val baseColor = theme.getBaseColor(isSystemInDarkTheme)
                val barColor = barTheme.getBarColor(baseColor).toArgb()
                if (isBarThemeSupported) {
                    statusBarColor = barColor
                    navigationBarColor = barColor
                }

                WindowCompat.setDecorFitsSystemWindows(this, false)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    if (isBarThemeSupported) {
                        isStatusBarContrastEnforced = true
                    }
                    isNavigationBarContrastEnforced = true
                }

                WindowCompat.getInsetsController(this, decorView).apply {
                    isAppearanceLightStatusBars =
                        when (theme) {
                            UiTheme.Light -> true
                            UiTheme.Default -> !isSystemInDarkTheme
                            else -> false
                        }
                    isAppearanceLightNavigationBars =
                        when (theme) {
                            UiTheme.Light -> true
                            UiTheme.Default -> !isSystemInDarkTheme
                            else -> false
                        }
                }
            }
        }
    }

    override fun apply(
        activity: Activity,
        theme: UiTheme,
        barTheme: UiBarTheme,
    ) {
        val decorView = activity.window.decorView
        val uiMode = activity.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val isSystemInDarkTheme = uiMode == Configuration.UI_MODE_NIGHT_YES
        val baseColor = theme.getBaseColor(isSystemInDarkTheme)
        val barColor = barTheme.getBarColor(baseColor).toArgb()

        decorView.setOnApplyWindowInsetsListener { view, insets ->
            view.setBackgroundColor(barColor)
            insets
        }
    }
}

private fun UiTheme.getBaseColor(isSystemInDarkTheme: Boolean): Color =
    when (this) {
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

private fun UiBarTheme.getBarColor(baseColor: Color): Color =
    when (this) {
        UiBarTheme.Opaque -> baseColor.copy(alpha = 0.25f)
        UiBarTheme.Transparent -> baseColor.copy(alpha = 0.01f)
        else -> baseColor
    }
