package com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.VideoPlayerPreview
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings

@Composable
fun PostCardVideo(
    modifier: Modifier = Modifier,
    url: String,
    blurred: Boolean = false,
    autoLoadImages: Boolean = true,
    onOpen: (() -> Unit)? = null,
    onOpenFullScreen: (() -> Unit)? = null,
) {
    if (url.isEmpty()) {
        return
    }

    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f)
                .clipToBounds(),
        contentAlignment = Alignment.Center,
    ) {
        var shouldBeRendered by remember(autoLoadImages) { mutableStateOf(autoLoadImages) }
        if (shouldBeRendered) {
            VideoPlayerPreview(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .blur(radius = if (blurred) 60.dp else 0.dp),
                url = url,
            )

            FilledIconButton(
                colors =
                    IconButtonDefaults.filledIconButtonColors().copy(
                        containerColor = MaterialTheme.colorScheme.onBackground,
                    ),
                modifier =
                    Modifier
                        .align(Alignment.Center)
                        .padding(
                            start = Spacing.xs,
                        ),
                onClick = {
                    onOpenFullScreen?.invoke()
                },
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = LocalStrings.current.actionOpenFullScreen,
                )
            }
        } else {
            Button(
                modifier = Modifier.padding(vertical = Spacing.s),
                onClick = {
                    shouldBeRendered = true
                },
            ) {
                Text(
                    text = LocalStrings.current.buttonLoad,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
