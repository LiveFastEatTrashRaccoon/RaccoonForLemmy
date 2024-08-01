package com.github.diegoberaldin.raccoonforlemmy.core.markdown

import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isUnspecified
import com.github.diegoberaldin.raccoonforlemmy.core.utils.compose.onClick
import com.mikepenz.markdown.compose.LocalMarkdownTypography
import com.mikepenz.markdown.compose.Markdown
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.compose.elements.MarkdownBlockQuote
import com.mikepenz.markdown.compose.elements.MarkdownText
import com.mikepenz.markdown.m3.markdownColor
import com.mikepenz.markdown.m3.markdownTypography
import com.mikepenz.markdown.model.MarkdownColors
import com.mikepenz.markdown.model.MarkdownPadding
import com.mikepenz.markdown.model.MarkdownTypography
import com.mikepenz.markdown.model.markdownPadding
import com.mikepenz.markdown.utils.buildMarkdownAnnotatedString
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.floor

private val String.containsSpoiler: Boolean
    get() = SpoilerRegex.spoilerOpening.containsMatchIn(this)

private val String.isImage: Boolean
    get() = ImageRegex.image.matches(this)

private const val MAX_LINES_SCALE_FACTOR = 0.97f
private const val GLOBAL_SCALE_FACTOR = 0.99f
private const val LINK_DELAY = 250L

@Composable
fun CustomMarkdownWrapper(
    content: String,
    modifier: Modifier,
    colors: MarkdownColors = markdownColor(),
    typography: MarkdownTypography = markdownTypography(),
    padding: MarkdownPadding = markdownPadding(),
    autoLoadImages: Boolean,
    maxLines: Int? = null,
    highlightText: String?,
    enableAlternateRendering: Boolean = false,
    blurImages: Boolean = false,
    onOpenUrl: ((String) -> Unit)?,
    onOpenImage: ((String) -> Unit)?,
    onClick: (() -> Unit)?,
    onDoubleClick: (() -> Unit)?,
    onLongClick: (() -> Unit)?,
) {/*
    if (enableAlternateRendering) {
        val parsedMarkdown = MarkdownParser(GFMFlavourDescriptor()).buildMarkdownTreeFromString(content)
        parsedMarkdown.children.forEachIndexed { index, child ->
            when (child.type.name) {
                else -> {
                    Text (
                        buildAnnotatedString {
                            if (child.children.isNotEmpty()) {
                                buildCustomMarkdownAnnotatedString(content = content, children = child.children)
                            }
                        }
                    )
                }
            }
        }
        return
    }*/

    val maxHeightDp =
        with(LocalDensity.current) {
            if (maxLines == null) {
                Dp.Unspecified
            } else {
                val lineHeight = typography.paragraph.lineHeight
                val base =
                    if (lineHeight.isUnspecified) {
                        floor(typography.paragraph.fontSize.toPx() * MAX_LINES_SCALE_FACTOR)
                    } else {
                        lineHeight.toPx() * MAX_LINES_SCALE_FACTOR
                    }
                (base * maxLines).toDp()
            }
        }
    val scope = rememberCoroutineScope()
    var isOpeningUrl by remember { mutableStateOf(false) }
    val customUriHandler =
        remember {
            object : UriHandler {
                override fun openUri(uri: String) {
                    isOpeningUrl = true
                    onOpenUrl?.invoke(uri)
                    scope.launch {
                        delay(LINK_DELAY)
                        isOpeningUrl = false
                    }
                }
            }
        }
    val components =
        markdownComponents(
            paragraph = { model ->
                val substring = model.content.substring(
                    startIndex = model.node.startOffset,
                    endIndex = model.node.endOffset,
                )
                when {
                    substring.containsSpoiler -> {
                        CustomMarkdownSpoiler(content = substring)
                    }

                    substring.isImage -> {
                        val res = ImageRegex.image.find(substring)
                        val link =
                            res
                                ?.groups
                                ?.get("url")
                                ?.value
                                .orEmpty()
                        CustomMarkdownImage(
                            url = link,
                            autoLoadImages = autoLoadImages,
                            blurred = blurImages,
                            onOpenImage = onOpenImage,
                        )
                    }

                    else -> {
                        val style = LocalMarkdownTypography.current.paragraph
                        var styledText = buildAnnotatedString {
                            pushStyle(style.toSpanStyle())
                            if (enableAlternateRendering) {
                                buildCustomMarkdownAnnotatedString(model.content, model.node.children)
                            } else {
                                buildMarkdownAnnotatedString(model.content, model.node)
                            }
                            pop()
                        }

                        styledText = applyAnnotatedStringHighlight(
                            annotatedString = styledText,
                            highlightText = highlightText,
                        )

                        MarkdownText(
                            styledText,
                            modifier = modifier,
                            style = style,
                        )
                    }
                }
            },
            image = { model ->
                CustomMarkdownImage(
                    node = model.node,
                    content = content,
                    autoLoadImages = autoLoadImages,
                    blurred = blurImages,
                    onOpenImage = onOpenImage,
                )
            },
            blockQuote = { model ->
                if (enableAlternateRendering) {
                    CustomBlockQuote(
                        content = model.content,
                        node = model.node,
                    )
                } else {
                    MarkdownBlockQuote(
                        content = model.content,
                        node = model.node,
                    )
                }
            }
        )

    CompositionLocalProvider(
        LocalUriHandler provides customUriHandler,
        LocalDensity provides
            Density(
                density = LocalDensity.current.density,
                fontScale = LocalDensity.current.fontScale * GLOBAL_SCALE_FACTOR,
            ),
    ) {
        Markdown(
            modifier =
                modifier
                    .heightIn(min = 0.dp, max = maxHeightDp)
                    .onClick(
                        onClick = {
                            if (!isOpeningUrl) {
                                onClick?.invoke()
                            }
                        },
                        onLongClick = {
                            onLongClick?.invoke()
                        },
                        onDoubleClick = {
                            onDoubleClick?.invoke()
                        },
                    ),
            content = content.sanitize(),
            colors = colors,
            typography = typography,
            padding = padding,
            components = components,
            imageTransformer = provideImageTransformer(),
        )
    }
}

@Composable
internal fun applyAnnotatedStringHighlight(
    annotatedString: AnnotatedString,
    highlightText: String?
): AnnotatedString {
    if (highlightText == null) {
        return annotatedString
    }

    val highlightColor = Color(255, 194, 10, 150)
    val startIndex = annotatedString.indexOf(highlightText, 0, true)
    val builder = AnnotatedString.Builder(annotatedString)
    if (startIndex > -1) {
        builder.addStyle(
            style = SpanStyle(background = highlightColor),
            startIndex,
            startIndex + highlightText.length
        )
    }

    return builder.toAnnotatedString()
}