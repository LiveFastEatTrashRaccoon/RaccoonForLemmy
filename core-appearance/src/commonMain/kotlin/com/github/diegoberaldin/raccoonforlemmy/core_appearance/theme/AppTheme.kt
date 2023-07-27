package com.github.diegoberaldin.raccoonforlemmy.core_appearance.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.github.diegoberaldin.raccoonforlemmy.core_appearance.data.ThemeState
import com.github.diegoberaldin.raccoonforlemmy.core_appearance.di.getThemeRepository

@Composable
fun AppTheme(
    theme: ThemeState,
    content: @Composable () -> Unit,
) {
    val repository = remember {
        val res = getThemeRepository()
        res.changeTheme(theme)
        res
    }

    val themeState by repository.state.collectAsState()
    val colorScheme = when (themeState) {
        ThemeState.Dark -> DarkColors
        ThemeState.Black -> BlackColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        shapes = shapes,
        content = content
    )
}
