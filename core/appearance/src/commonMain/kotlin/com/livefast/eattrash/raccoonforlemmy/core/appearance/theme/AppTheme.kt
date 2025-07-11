package com.livefast.eattrash.raccoonforlemmy.core.appearance.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.UiBarTheme
import com.livefast.eattrash.raccoonforlemmy.core.appearance.di.getBarColorProvider
import com.livefast.eattrash.raccoonforlemmy.core.appearance.di.getColorSchemeProvider
import com.livefast.eattrash.raccoonforlemmy.core.appearance.di.getThemeRepository

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AppTheme(
    useDynamicColors: Boolean,
    barTheme: UiBarTheme = UiBarTheme.Solid,
    content: @Composable () -> Unit,
) {
    val repository =
        remember {
            getThemeRepository()
        }

    val themeState by repository.uiTheme.collectAsState()
    val customSeedColor by repository.customSeedColor.collectAsState()

    val colorSchemeProvider = remember { getColorSchemeProvider() }
    val colorScheme =
        colorSchemeProvider.getColorScheme(
            theme = themeState,
            dynamic = useDynamicColors,
            customSeed = customSeedColor,
            isSystemInDarkTheme = isSystemInDarkTheme(),
        )

    val fontFamily by repository.uiFontFamily.collectAsState()
    val typography = fontFamily.toTypography()

    val barColorProvider = remember { getBarColorProvider() }
    barColorProvider.setBarColorAccordingToTheme(
        theme = themeState,
        barTheme = barTheme,
    )

    MaterialExpressiveTheme(
        colorScheme = colorScheme,
        typography = typography,
        shapes = shapes,
        content = content,
        motionScheme = MotionScheme.expressive(),
    )
}
