package com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material3.Button
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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.VideoPlayer
import com.livefast.eattrash.raccoonforlemmy.core.l10n.messages.LocalStrings

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
        if (blurred) {
            Column(
                modifier = Modifier.padding(vertical = Spacing.s),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Spacing.s),
            ) {
                Text(
                    text = LocalStrings.current.messageVideoNsfw,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Button(
                    onClick = {
                        onOpen?.invoke()
                    },
                ) {
                    Text(
                        text = LocalStrings.current.buttonLoad,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        } else {
            var shouldBeRendered by remember(autoLoadImages) { mutableStateOf(autoLoadImages) }
            if (shouldBeRendered) {
                VideoPlayer(
                    modifier = Modifier.fillMaxSize(),
                    url = url,
                    contentScale = ContentScale.Fit,
                )

                IconButton(
                    modifier =
                        Modifier
                            .align(Alignment.TopStart)
                            .padding(
                                start = Spacing.xs,
                            ),
                    onClick = {
                        onOpenFullScreen?.invoke()
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Fullscreen,
                        contentDescription = null,
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
}
