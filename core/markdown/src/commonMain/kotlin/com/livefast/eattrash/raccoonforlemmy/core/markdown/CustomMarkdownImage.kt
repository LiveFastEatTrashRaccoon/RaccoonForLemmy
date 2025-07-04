package com.livefast.eattrash.raccoonforlemmy.core.markdown

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.CustomImage
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.onClick
import com.mikepenz.markdown.compose.LocalMarkdownTypography
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode

@Composable
internal fun CustomMarkdownImage(
    node: ASTNode,
    content: String,
    blurred: Boolean = false,
    autoLoadImages: Boolean = true,
    onOpenImage: ((String) -> Unit)? = null,
) {
    val link =
        runCatching {
            node
                .findChildOfTypeRecursive(MarkdownElementTypes.LINK_DESTINATION)
                ?.getTextInNode(content)
                ?.toString()
                .orEmpty()
        }.getOrElse { "" }
    CustomMarkdownImage(
        url = link,
        autoLoadImages = autoLoadImages,
        blurred = blurred,
        onOpenImage = onOpenImage,
    )
}

private const val LOADING_ANIMATION_DURATION = 1000

@Composable
internal fun CustomMarkdownImage(
    url: String,
    blurred: Boolean = false,
    autoLoadImages: Boolean = true,
    onOpenImage: ((String) -> Unit)?,
) {
    if (url.isBlank()) {
        return
    }

    CustomImage(
        modifier =
        Modifier
            .fillMaxWidth()
            .onClick(
                onClick = {
                    onOpenImage?.invoke(url)
                },
            ),
        url = url,
        autoload = autoLoadImages,
        blurred = blurred,
        quality = FilterQuality.Low,
        contentScale = ContentScale.FillWidth,
        onFailure = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = LocalStrings.current.messageImageLoadingError,
                style = LocalMarkdownTypography.current.text,
            )
        },
        onLoading = { progress ->
            val prog =
                if (progress != null) {
                    progress
                } else {
                    val transition = rememberInfiniteTransition()
                    val res by transition.animateFloat(
                        initialValue = 0f,
                        targetValue = 1f,
                        animationSpec =
                        InfiniteRepeatableSpec(
                            animation = tween(LOADING_ANIMATION_DURATION),
                        ),
                    )
                    res
                }
            CircularProgressIndicator(
                progress = { prog },
                color = MaterialTheme.colorScheme.primary,
            )
        },
    )
}
