package com.github.diegoberaldin.raccoonforlemmy.core.commonui.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
actual fun CustomImage(
    modifier: Modifier,
    url: String,
    contentDescription: String?,
    quality: FilterQuality,
    contentScale: ContentScale,
    alignment: Alignment,
    contentAlignment: Alignment,
    alpha: Float,
    colorFilter: ColorFilter?,
    onLoading: @Composable (BoxScope.(Float?) -> Unit)?,
    onFailure: @Composable (BoxScope.(Throwable) -> Unit)?,
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
