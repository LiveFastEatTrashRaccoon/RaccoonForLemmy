package com.livefast.eattrash.raccoonforlemmy.core.markdown

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isUnspecified
import androidx.compose.ui.unit.sp
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.onClick
import com.mikepenz.markdown.compose.LocalMarkdownAnnotator
import com.mikepenz.markdown.compose.LocalMarkdownTypography
import com.mikepenz.markdown.compose.Markdown
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.compose.elements.MarkdownText
import com.mikepenz.markdown.m3.markdownColor
import com.mikepenz.markdown.m3.markdownTypography
import com.mikepenz.markdown.model.MarkdownAnnotator
import com.mikepenz.markdown.model.MarkdownColors
import com.mikepenz.markdown.model.MarkdownPadding
import com.mikepenz.markdown.model.MarkdownTypography
import com.mikepenz.markdown.model.markdownPadding
import com.mikepenz.markdown.utils.buildMarkdownAnnotatedString
import com.mikepenz.markdown.utils.codeSpanStyle
import com.mikepenz.markdown.utils.linkTextSpanStyle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.intellij.markdown.ast.ASTNode
import kotlin.math.floor

private val String.isImage: Boolean
    get() = ImageRegex.image.matches(this)

private const val MAX_LINES_SCALE_FACTOR = 0.97f
private const val GLOBAL_SCALE_FACTOR = 0.99f
private const val LINK_DELAY = 250L

@Composable
fun CustomMarkdownWrapperController(
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
) {
    if (SpoilerRegex.spoilerOpening.containsMatchIn(content)) {
        val codeBlockMatches = Regex("`{3,}(.|\\n)+?(`{3,})").findAll(content)
        var previousIndex = 0
        Column {
            SpoilerRegex.spoilerFull
                .findAll(content)
                .filter { spoiler ->
                    codeBlockMatches.none { codeBlock ->
                        codeBlock.range.first < spoiler.range.first && codeBlock.range.last > spoiler.range.last
                    }
                }.forEach { matchRes ->
                    if (previousIndex < matchRes.range.first) {
                        CustomMarkdownWrapper(
                            content = content.substring(previousIndex, matchRes.range.first),
                            modifier = modifier,
                            colors = colors,
                            typography = typography,
                            padding = padding,
                            autoLoadImages = autoLoadImages,
                            maxLines = maxLines,
                            highlightText = highlightText,
                            enableAlternateRendering = enableAlternateRendering,
                            blurImages = blurImages,
                            onOpenUrl = onOpenUrl,
                            onOpenImage = onOpenImage,
                            onClick = onClick,
                            onDoubleClick = onDoubleClick,
                            onLongClick = onLongClick,
                        )
                    }
                    markdownSpoilerBlock(
                        content = content.substring(matchRes.range.first, matchRes.range.last),
                        modifier = modifier,
                        colors = colors,
                        typography = typography,
                        padding = padding,
                        autoLoadImages = autoLoadImages,
                        maxLines = maxLines,
                        highlightText = highlightText,
                        enableAlternateRendering = enableAlternateRendering,
                        blurImages = blurImages,
                        onOpenUrl = onOpenUrl,
                        onOpenImage = onOpenImage,
                        onClick = onClick,
                        onDoubleClick = onDoubleClick,
                        onLongClick = onLongClick,
                    )
                    previousIndex = matchRes.range.last
                }
            if (previousIndex < content.length - 1) {
                CustomMarkdownWrapper(
                    content = content.substring(previousIndex, content.length),
                    modifier = modifier,
                    colors = colors,
                    typography = typography,
                    padding = padding,
                    autoLoadImages = autoLoadImages,
                    maxLines = maxLines,
                    highlightText = highlightText,
                    enableAlternateRendering = enableAlternateRendering,
                    blurImages = blurImages,
                    onOpenUrl = onOpenUrl,
                    onOpenImage = onOpenImage,
                    onClick = onClick,
                    onDoubleClick = onDoubleClick,
                    onLongClick = onLongClick,
                )
            }
        }
    } else {
        CustomMarkdownWrapper(
            content = content,
            modifier = modifier,
            colors = colors,
            typography = typography,
            padding = padding,
            autoLoadImages = autoLoadImages,
            maxLines = maxLines,
            highlightText = highlightText,
            enableAlternateRendering = enableAlternateRendering,
            blurImages = blurImages,
            onOpenUrl = onOpenUrl,
            onOpenImage = onOpenImage,
            onClick = onClick,
            onDoubleClick = onDoubleClick,
            onLongClick = onLongClick,
        )
    }
}

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
) {
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
                val substring =
                    model.content.substring(
                        startIndex = model.node.startOffset,
                        endIndex = model.node.endOffset,
                    )
                when {
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
                        markdownParagraphWithHighlights(
                            content = model.content,
                            node = model.node,
                            highlightText = highlightText,
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
                        indication = null,
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
internal fun markdownParagraphWithHighlights(
    content: String,
    node: ASTNode,
    modifier: Modifier = Modifier,
    typography: MarkdownTypography = LocalMarkdownTypography.current,
    annotator: MarkdownAnnotator = LocalMarkdownAnnotator.current,
    highlightText: String? = null,
) {
    val highlightColor = Color(255, 194, 10, 150)
    var styledText =
        buildAnnotatedString {
            pushStyle(typography.paragraph.toSpanStyle())
            buildMarkdownAnnotatedString(
                content = content,
                node = node,
                linkTextStyle = typography.linkTextSpanStyle,
                codeStyle = typography.codeSpanStyle,
                annotator = annotator,
            )
            pop()
        }

    if (highlightText != null) {
        val startIndex = styledText.indexOf(highlightText, 0, true)
        val builder = AnnotatedString.Builder(styledText)
        if (startIndex > -1) {
            builder.addStyle(
                style = SpanStyle(background = highlightColor),
                startIndex,
                startIndex + highlightText.length,
            )

            styledText = builder.toAnnotatedString()
        }
    }

    MarkdownText(
        styledText,
        modifier = modifier,
        style = typography.paragraph,
    )
}

@Composable
private fun markdownSpoilerBlock(
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
) {
    val matches = SpoilerRegex.spoilerOpening.findAll(content)
    val spoilerTitle =
        matches
            .first()
            .groups["title"]
            ?.value
            .orEmpty()
    val spoilerBody = content.replaceFirst(SpoilerRegex.spoilerOpening, "")
    val spoilerModifier =
        Modifier
            .padding(
                start = 9.dp,
                bottom = 9.dp,
            )
    var isExpanded by remember { mutableStateOf(false) }
    Column(
        modifier =
            Modifier
                .clickable {
                    isExpanded = !isExpanded
                },
    ) {
        Text(
            modifier =
                Modifier
                    .fillMaxWidth(),
            text =
                buildAnnotatedString {
                    withStyle(SpanStyle(fontSize = 20.sp)) {
                        append(
                            if (isExpanded) {
                                "▼︎ "
                            } else {
                                "▶︎ "
                            },
                        )
                    }
                    append(spoilerTitle)
                },
        )
        if (isExpanded) {
            if (matches.count() > 1) {
                var previousIndex = 0
                Column(
                    modifier = spoilerModifier,
                ) {
                    SpoilerRegex.spoilerFull.findAll(spoilerBody).forEach { matchRes ->
                        if (previousIndex < matchRes.range.first) {
                            CustomMarkdownWrapper(
                                content = spoilerBody.substring(previousIndex, matchRes.range.first),
                                modifier = modifier,
                                colors = colors,
                                typography = typography,
                                padding = padding,
                                autoLoadImages = autoLoadImages,
                                maxLines = maxLines,
                                highlightText = highlightText,
                                enableAlternateRendering = enableAlternateRendering,
                                blurImages = blurImages,
                                onOpenUrl = onOpenUrl,
                                onOpenImage = onOpenImage,
                                onClick = onClick,
                                onDoubleClick = onDoubleClick,
                                onLongClick = onLongClick,
                            )
                        }
                        markdownSpoilerBlock(
                            content = spoilerBody.substring(matchRes.range.first, matchRes.range.last),
                            modifier = modifier,
                            colors = colors,
                            typography = typography,
                            padding = padding,
                            autoLoadImages = autoLoadImages,
                            maxLines = maxLines,
                            highlightText = highlightText,
                            enableAlternateRendering = enableAlternateRendering,
                            blurImages = blurImages,
                            onOpenUrl = onOpenUrl,
                            onOpenImage = onOpenImage,
                            onClick = onClick,
                            onDoubleClick = onDoubleClick,
                            onLongClick = onLongClick,
                        )
                        previousIndex = matchRes.range.last
                    }
                }
            } else {
                Column(
                    modifier = spoilerModifier,
                ) {
                    CustomMarkdownWrapper(
                        content = spoilerBody.trimEnd(':'),
                        modifier = modifier,
                        colors = colors,
                        typography = typography,
                        padding = padding,
                        autoLoadImages = autoLoadImages,
                        maxLines = maxLines,
                        highlightText = highlightText,
                        enableAlternateRendering = enableAlternateRendering,
                        blurImages = blurImages,
                        onOpenUrl = onOpenUrl,
                        onOpenImage = onOpenImage,
                        onClick = onClick,
                        onDoubleClick = onDoubleClick,
                        onLongClick = onLongClick,
                    )
                }
            }
        }
    }
}
