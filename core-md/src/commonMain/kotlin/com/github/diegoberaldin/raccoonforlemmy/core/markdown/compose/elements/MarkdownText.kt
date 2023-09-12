package com.github.diegoberaldin.raccoonforlemmy.core.markdown.compose.elements

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.github.diegoberaldin.raccoonforlemmy.core.markdown.compose.LocalMarkdownColors
import com.github.diegoberaldin.raccoonforlemmy.core.markdown.compose.LocalMarkdownTypography
import com.github.diegoberaldin.raccoonforlemmy.core.markdown.compose.LocalReferenceLinkHandler
import com.github.diegoberaldin.raccoonforlemmy.core.markdown.utils.TAG_IMAGE_URL
import com.github.diegoberaldin.raccoonforlemmy.core.markdown.utils.TAG_URL
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
internal fun MarkdownText(
    content: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalMarkdownTypography.current.text,
    onOpenUrl: ((String) -> Unit)? = null,
) {
    MarkdownText(AnnotatedString(content), modifier, style, onOpenUrl)
}

@Composable
internal fun MarkdownText(
    content: AnnotatedString,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalMarkdownTypography.current.text,
    onOpenUrl: ((String) -> Unit)? = null,
) {
    val referenceLinkHandler = LocalReferenceLinkHandler.current
    val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }

    val hasUrl = content.getStringAnnotations(TAG_URL, 0, content.length).any()
    val textModifier = if (hasUrl) {
        modifier.pointerInput(Unit) {
            detectTapGestures { pos ->
                layoutResult.value?.let { layoutResult ->
                    val position = layoutResult.getOffsetForPosition(pos)
                    content.getStringAnnotations(TAG_URL, position, position)
                        .firstOrNull()
                        ?.let {
                            val url = referenceLinkHandler.find(it.item)
                            onOpenUrl?.invoke(url)
                        }
                }
            }
        }
    } else {
        modifier
    }

    Text(
        text = content,
        modifier = textModifier,
        style = style,
        inlineContent = mapOf(
            TAG_IMAGE_URL to InlineTextContent(
                Placeholder(
                    180.sp,
                    180.sp,
                    PlaceholderVerticalAlign.Bottom,
                ), // TODO, identify flexible scaling!
            ) { link ->
                val painterResource = asyncPainterResource(data = link)
                KamelImage(
                    resource = painterResource,
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxWidth(),
                )
            },
        ),
        color = LocalMarkdownColors.current.text,
        onTextLayout = { layoutResult.value = it },
    )
}
