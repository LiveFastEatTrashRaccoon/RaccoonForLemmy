package com.livefast.eattrash.raccoonforlemmy.core.commonui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import chaintech.videoplayer.host.MediaPlayerHost
import chaintech.videoplayer.model.ScreenResize
import chaintech.videoplayer.ui.preview.VideoPreviewComposable
import chaintech.videoplayer.ui.video.VideoPlayerComposable
import com.livefast.eattrash.raccoonforlemmy.core.resources.di.getCoreResources

@Composable
fun VideoPlayer(
    url: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.FillWidth,
    muted: Boolean = true,
) {
    val resources = remember { getCoreResources() }
    val playerHost =
        remember {
            MediaPlayerHost(
                mediaUrl = url,
                isMuted = muted,
                initialVideoFitMode =
                if (contentScale == ContentScale.Fit) {
                    ScreenResize.FIT
                } else {
                    ScreenResize.FILL
                },
            )
        }
    VideoPlayerComposable(
        modifier = modifier,
        playerHost = playerHost,
        playerConfig = resources.videoPlayerConfig,
    )
}

@Composable
fun VideoPlayerPreview(url: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        VideoPreviewComposable(
            url = url,
            loadingIndicatorColor = MaterialTheme.colorScheme.onBackground,
            frameCount = 1,
        )
    }
}
