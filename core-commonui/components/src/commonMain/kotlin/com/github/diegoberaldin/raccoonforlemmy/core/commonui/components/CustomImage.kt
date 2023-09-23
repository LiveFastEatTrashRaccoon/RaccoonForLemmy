package com.github.diegoberaldin.raccoonforlemmy.core.commonui.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun CustomImage(
    modifier: Modifier = Modifier,
    url: String,
    contentDescription: String? = null,
    quality: FilterQuality = FilterQuality.Medium,
    contentScale: ContentScale = ContentScale.Fit,
    alignment: Alignment = Alignment.Center,
    contentAlignment: Alignment = Alignment.Center,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    onLoading: @Composable (BoxScope.(Float) -> Unit)? = null,
    onFailure: @Composable (BoxScope.(Throwable) -> Unit)? = null,
) {
    val painterResource = asyncPainterResource(
        data = url,
        filterQuality = quality,
    )
    KamelImage(
        modifier = modifier,
        resource = painterResource,
        contentDescription = contentDescription,
        contentScale = contentScale,
        alignment = alignment,
        contentAlignment = contentAlignment,
        alpha = alpha,
        colorFilter = colorFilter,
        onLoading = onLoading,
        onFailure = onFailure,
    )
}