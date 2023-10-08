package com.github.diegoberaldin.raccoonforlemmy.core.commonui.components

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.toSize
import com.github.diegoberaldin.raccoonforlemmy.resources.MR
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun ZoomableImage(
    modifier: Modifier = Modifier,
    url: String,
    autoLoadImages: Boolean = false,
) {

    var size by remember { mutableStateOf(Size.Zero) }
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    val transformableState = rememberTransformableState { zoom, pan, _ ->
        scale = (scale * zoom).coerceIn(0.5f, 3f)
        offsetX = (offsetX + pan.x).coerceIn(-size.width / scale, size.width / scale)
        offsetY = (offsetY + pan.y).coerceIn(-size.height / scale, size.height / scale)
    }

    LaunchedEffect(transformableState.isTransformInProgress) {
        if (!transformableState.isTransformInProgress) {
            if (scale < 1.1f) {
                scale = 1f
                offsetX = 0f
                offsetY = 0f
            }
        }
    }

    Box(
        modifier = modifier
            .clip(RectangleShape)
            .fillMaxSize()
            .background(Color.Black)
            .onGloballyPositioned {
                size = it.size.toSize()
            }
            .transformable(transformableState, lockRotationOnZoomPan = true)
    ) {
        CustomImage(
            modifier = Modifier
                .align(Alignment.Center)
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offsetX,
                    translationY = offsetY,
                ),
            url = url,
            autoload = autoLoadImages,
            contentDescription = null,
            onFailure = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = stringResource(MR.strings.message_image_loading_error),
                    color = MaterialTheme.colorScheme.onBackground,
                )
            },
            onLoading = { progress ->
                val prog = if (progress != null) {
                    progress
                } else {
                    val transition = rememberInfiniteTransition()
                    val res by transition.animateFloat(
                        initialValue = 0f,
                        targetValue = 1f,
                        animationSpec = InfiniteRepeatableSpec(
                            animation = tween(1000)
                        )
                    )
                    res
                }
                CircularProgressIndicator(
                    progress = prog,
                    color = MaterialTheme.colorScheme.primary,
                )
            },
        )
    }
}
