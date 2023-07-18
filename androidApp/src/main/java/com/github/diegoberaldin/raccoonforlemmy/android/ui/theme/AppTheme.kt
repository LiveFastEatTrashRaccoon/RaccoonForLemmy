package com.github.diegoberaldin.raccoonforlemmy.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.github.diegoberaldin.raccoonforlemmy.android.ui.theme.DarkColors
import com.github.diegoberaldin.raccoonforlemmy.android.ui.theme.LightColors
import com.github.diegoberaldin.raccoonforlemmy.android.ui.theme.shapes
import com.github.diegoberaldin.raccoonforlemmy.android.ui.theme.typography

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) {
        DarkColors
    } else {
        LightColors
    }


    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        shapes = shapes,
        content = content
    )
}
