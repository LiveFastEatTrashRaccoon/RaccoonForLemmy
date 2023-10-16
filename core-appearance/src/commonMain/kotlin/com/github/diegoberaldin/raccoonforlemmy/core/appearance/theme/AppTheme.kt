package com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.UiTheme
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.di.getColorSchemeProvider
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.di.getThemeRepository

@Composable
fun AppTheme(
    theme: UiTheme,
    contentFontScale: Float,
    useDynamicColors: Boolean,
    content: @Composable () -> Unit,
) {
    val repository = remember {
        val res = getThemeRepository()
        res.changeUiTheme(theme)
        res.changeContentFontScale(contentFontScale)
        res
    }

    val themeState by repository.uiTheme.collectAsState()
    val customSeedColor by repository.customSeedColor.collectAsState()

    val colorSchemeProvider = remember { getColorSchemeProvider() }
    val colorScheme = colorSchemeProvider.getColorScheme(
        theme = themeState,
        dynamic = useDynamicColors,
        customSeed = customSeedColor,
    )

    val fontFamily by repository.uiFontFamily.collectAsState()
    val typography = getTypography(fontFamily)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        shapes = shapes,
        content = content,
    )
}
