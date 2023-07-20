package com.github.diegoberaldin.raccoonforlemmy.core_appearance.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.github.diegoberaldin.raccoonforlemmy.core_appearance.data.ThemeState
import com.github.diegoberaldin.raccoonforlemmy.core_appearance.di.getThemeRepository

@Composable
fun AppTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit,
) {
    val repository = remember {
        val res = getThemeRepository()
        res.changeTheme(
            if (darkTheme) {
                ThemeState.Dark
            } else {
                ThemeState.Light
            }
        )
        res
    }

    val themeState by repository.state.collectAsState()
    val colorScheme = when (themeState) {
        ThemeState.Dark -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        shapes = shapes,
        content = content
    )
}
