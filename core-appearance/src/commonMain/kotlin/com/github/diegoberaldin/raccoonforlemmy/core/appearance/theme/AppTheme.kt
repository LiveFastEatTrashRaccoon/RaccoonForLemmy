package com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.ThemeState
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.di.getColorSchemeProvider
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.di.getThemeRepository

@Composable
fun AppTheme(
    theme: ThemeState,
    contentFontScale: Float,
    useDynamicColors: Boolean,
    content: @Composable () -> Unit,
) {
    val repository = remember {
        val res = getThemeRepository()
        res.changeTheme(theme)
        res.changeContentFontScale(contentFontScale)
        res
    }

    val themeState by repository.state.collectAsState()

    val colorSchemeProvider = remember { getColorSchemeProvider() }
    val colorScheme = colorSchemeProvider.getColorScheme(
        theme = themeState,
        dynamic = useDynamicColors
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        shapes = shapes,
        content = content,
    )
}
