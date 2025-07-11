package com.livefast.eattrash.raccoonforlemmy.core.commonui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import com.livefast.eattrash.raccoonforlemmy.core.appearance.di.getThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.ContentFontClass

@Composable
fun CustomizedContent(contentClass: ContentFontClass, content: @Composable () -> Unit) {
    val themeRepository = remember { getThemeRepository() }
    val fontScale by themeRepository.contentFontScale.collectAsState()
    val uiFontScale by themeRepository.uiFontScale.collectAsState()
    val scaleFactor =
        when (contentClass) {
            ContentFontClass.Title -> fontScale.title
            ContentFontClass.Body -> fontScale.body
            ContentFontClass.Comment -> fontScale.comment
            ContentFontClass.AncillaryText -> fontScale.ancillary
        } * uiFontScale

    CompositionLocalProvider(
        LocalDensity provides
            Density(
                density = LocalDensity.current.density,
                fontScale = scaleFactor,
            ),
    ) {
        content()
    }
}
