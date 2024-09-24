package com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.livefast.eattrash.raccoonforlemmy.core.appearance.di.getThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.readContentAlpha
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.toTypography
import com.livefast.eattrash.raccoonforlemmy.core.markdown.CustomMarkdownWrapper
import com.livefast.eattrash.raccoonforlemmy.core.persistence.di.getSettingsRepository
import com.mikepenz.markdown.m3.markdownColor
import com.mikepenz.markdown.m3.markdownTypography
import kotlinx.coroutines.flow.map

@Composable
fun PostCardTitle(
    text: String,
    modifier: Modifier = Modifier,
    bolder: Boolean = false,
    autoLoadImages: Boolean = true,
    markRead: Boolean = false,
    highlightText: String? = null,
    onClick: (() -> Unit)? = null,
    onOpenImage: ((String) -> Unit)? = null,
    onDoubleClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
) {
    val uriHandler = LocalUriHandler.current
    val settingsRepository = remember { getSettingsRepository() }
    val themeRepository = remember { getThemeRepository() }
    val fontFamily by themeRepository.contentFontFamily.collectAsState()
    val typography = fontFamily.toTypography()
    val weightNormalOrLight =
        if (bolder) {
            FontWeight.Normal
        } else {
            FontWeight.Light
        }
    val weightMediumOrNormal =
        if (bolder) {
            FontWeight.Medium
        } else {
            FontWeight.Normal
        }
    val additionalAlphaFactor = if (markRead) readContentAlpha else 1f
    val enableAlternateMarkdownRendering by settingsRepository.currentSettings
        .map { it.enableAlternateMarkdownRendering }
        .collectAsState(false)

    SelectionContainer {
        CustomMarkdownWrapper(
            modifier = modifier.padding(horizontal = Spacing.xxs),
            content = text,
            autoLoadImages = autoLoadImages,
            typography =
                markdownTypography(
                    h1 = typography.titleLarge.copy(fontWeight = weightNormalOrLight),
                    h2 = typography.titleLarge.copy(fontWeight = weightNormalOrLight),
                    h3 = typography.titleMedium.copy(fontWeight = weightMediumOrNormal),
                    h4 = typography.titleMedium.copy(fontWeight = weightMediumOrNormal),
                    h5 = typography.titleSmall.copy(fontWeight = weightMediumOrNormal),
                    h6 = typography.titleSmall,
                    text = typography.bodyMedium.copy(fontWeight = weightMediumOrNormal),
                    paragraph = typography.bodyMedium.copy(fontWeight = weightMediumOrNormal),
                    quote = typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
                    bullet = typography.bodyMedium,
                    list = typography.bodyMedium,
                    ordered = typography.bodyMedium,
                    code = typography.bodyMedium.copy(fontFamily = FontFamily.Monospace),
                ),
            colors =
                markdownColor(
                    text = MaterialTheme.colorScheme.onBackground.copy(alpha = additionalAlphaFactor),
                    codeText = MaterialTheme.colorScheme.onBackground.copy(alpha = additionalAlphaFactor),
                    codeBackground = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                    dividerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                ),
            highlightText = highlightText,
            enableAlternateRendering = enableAlternateMarkdownRendering,
            onOpenUrl = { url ->
                uriHandler.openUri(url)
            },
            onOpenImage = { url ->
                onOpenImage?.invoke(url)
            },
            onClick = onClick,
            onDoubleClick = onDoubleClick,
            onLongClick = onLongClick,
        )
    }
}
