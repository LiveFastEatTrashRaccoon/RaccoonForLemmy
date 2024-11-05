package com.livefast.eattrash.raccoonforlemmy.core.commonui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import com.livefast.eattrash.raccoonforlemmy.core.l10n.messages.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.utils.imageload.getImageLoaderProvider

@Composable
fun CustomImage(
    modifier: Modifier = Modifier,
    url: String,
    blurred: Boolean = false,
    autoload: Boolean,
    loadButtonContent: @Composable (() -> Unit)? = null,
    contentDescription: String? = null,
    quality: FilterQuality = FilterQuality.Medium,
    contentScale: ContentScale = ContentScale.Fit,
    alignment: Alignment = Alignment.Center,
    contentAlignment: Alignment = Alignment.Center,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    onLoading: @Composable (BoxScope.(Float?) -> Unit)? = null,
    onFailure: @Composable (BoxScope.(Throwable) -> Unit)? = null,
    onSuccess: @Composable (BoxScope.() -> Unit)? = null,
) {
    val imageLoaderProvider = remember { getImageLoaderProvider() }
    var painterState: AsyncImagePainter.State by remember {
        mutableStateOf(AsyncImagePainter.State.Empty)
    }
    var shouldBeRendered by remember(autoload) { mutableStateOf(autoload) }

    Box(
        modifier = modifier,
        contentAlignment = contentAlignment,
    ) {
        if (shouldBeRendered) {
            AsyncImage(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .blur(radius = if (blurred) 60.dp else 0.dp),
                model = url,
                contentDescription = contentDescription,
                filterQuality = quality,
                contentScale = contentScale,
                alignment = alignment,
                alpha = alpha,
                colorFilter = colorFilter,
                onState = {
                    painterState = it
                },
                imageLoader = imageLoaderProvider.provideImageLoader(),
            )
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Button(
                    onClick = {
                        shouldBeRendered = true
                    },
                ) {
                    if (loadButtonContent != null) {
                        loadButtonContent.invoke()
                    } else {
                        Text(
                            text = LocalStrings.current.buttonLoad,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }

        when (val state = painterState) {
            AsyncImagePainter.State.Empty -> Unit
            is AsyncImagePainter.State.Error -> {
                onFailure?.invoke(this, state.result.throwable)
            }

            is AsyncImagePainter.State.Loading -> {
                onLoading?.invoke(this, null)
            }

            is AsyncImagePainter.State.Success -> {
                onSuccess?.invoke(this)
            }

            else -> Unit
        }
    }
}
