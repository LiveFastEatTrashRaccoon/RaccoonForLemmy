package com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.CustomImage
import com.livefast.eattrash.raccoonforlemmy.core.l10n.messages.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.onClick

@Composable
fun PostCardImage(
    modifier: Modifier = Modifier,
    imageUrl: String,
    autoLoadImages: Boolean = true,
    contentScale: ContentScale = ContentScale.FillWidth,
    loadButtonContent: @Composable (() -> Unit)? = null,
    minHeight: Dp = 200.dp,
    blurred: Boolean = false,
    onImageClick: ((String) -> Unit)? = null,
    onDoubleClick: (() -> Unit)? = null,
) {
    if (imageUrl.isEmpty()) {
        return
    }
    var revealing by remember { mutableStateOf(!blurred) }

    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .heightIn(min = minHeight)
                .onClick(
                    onDoubleClick = onDoubleClick ?: {},
                ),
    ) {
        CustomImage(
            modifier =
                Modifier
                    .fillMaxSize()
                    .then(
                        if (onImageClick != null) {
                            Modifier.clickable {
                                onImageClick(imageUrl)
                            }
                        } else {
                            Modifier
                        },
                    ),
            url = imageUrl,
            quality = FilterQuality.Low,
            autoload = autoLoadImages,
            blurred = !revealing,
            loadButtonContent = loadButtonContent,
            contentScale = contentScale,
            onFailure = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = LocalStrings.current.messageImageLoadingError,
                    color = MaterialTheme.colorScheme.onSurface,
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
                                    animation = tween(1000),
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
        Row(
            modifier =
                Modifier.align(Alignment.BottomEnd).padding(
                    bottom = Spacing.xxs,
                    end = Spacing.xs,
                ),
        ) {
            val iconModifier =
                Modifier
                    .border(
                        width = Dp.Hairline,
                        color = MaterialTheme.colorScheme.onBackground,
                        shape = CircleShape,
                    ).padding(2.5.dp)
                    .clip(CircleShape)
            if (blurred) {
                IconButton(
                    onClick = {
                        revealing = !revealing
                    },
                ) {
                    Icon(
                        modifier = iconModifier,
                        imageVector =
                            if (revealing) {
                                Icons.Default.VisibilityOff
                            } else {
                                Icons.Default.Visibility
                            },
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }
        }
    }
}
