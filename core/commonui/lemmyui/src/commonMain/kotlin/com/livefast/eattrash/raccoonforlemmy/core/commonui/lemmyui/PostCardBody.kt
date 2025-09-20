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
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import com.livefast.eattrash.raccoonforlemmy.core.appearance.di.getThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.readContentAlpha
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.toTypography
import com.livefast.eattrash.raccoonforlemmy.core.markdown.CustomMarkdownWrapperController
import com.livefast.eattrash.raccoonforlemmy.core.persistence.di.getSettingsRepository
import com.mikepenz.markdown.m3.markdownColor
import com.mikepenz.markdown.m3.markdownTypography
import kotlinx.coroutines.flow.map

@Composable
fun PostCardBody(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int? = null,
    autoLoadImages: Boolean = true,
    blurImages: Boolean = false,
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
    val additionalAlphaFactor = if (markRead) readContentAlpha else 1f
    val enableAlternateMarkdownRendering by settingsRepository.currentSettings
        .map { it.enableAlternateMarkdownRendering }
        .collectAsState(false)

    if (text.isNotEmpty()) {
        SelectionContainer {
            CustomMarkdownWrapperController(
                modifier = modifier.padding(horizontal = Spacing.xxs),
                content = text,
                maxLines = maxLines,
                autoLoadImages = autoLoadImages,
                typography =
                markdownTypography(
                    h1 = typography.titleLarge,
                    h2 = typography.titleLarge,
                    h3 = typography.titleMedium,
                    h4 = typography.titleMedium,
                    h5 = typography.titleSmall,
                    h6 = typography.titleSmall,
                    text = typography.bodyMedium,
                    paragraph = typography.bodyMedium,
                    quote = typography.bodyMedium,
                    bullet = typography.bodyMedium,
                    list = typography.bodyMedium,
                    ordered = typography.bodyMedium,
                    code = typography.bodyMedium.copy(fontFamily = FontFamily.Monospace),
                    inlineCode = typography.bodyMedium.copy(fontFamily = FontFamily.Monospace),
                    textLink = TextLinkStyles(
                        style = typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline,
                        ).toSpanStyle(),
                    ),
                    table = typography.bodyMedium,
                ),
                colors =
                markdownColor(
                    text = MaterialTheme.colorScheme.onBackground.copy(alpha = additionalAlphaFactor),
                    codeBackground = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                    dividerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                    inlineCodeBackground = MaterialTheme.colorScheme.onBackground.copy(alpha = additionalAlphaFactor),
                ),
                highlightText = highlightText,
                enableAlternateRendering = enableAlternateMarkdownRendering,
                blurImages = blurImages,
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
}
