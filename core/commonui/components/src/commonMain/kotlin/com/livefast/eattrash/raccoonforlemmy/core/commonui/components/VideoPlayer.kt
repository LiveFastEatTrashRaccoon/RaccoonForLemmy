package com.livefast.eattrash.raccoonforlemmy.core.commonui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import chaintech.videoplayer.ui.video.VideoPlayerView
import com.livefast.eattrash.raccoonforlemmy.core.resources.di.getCoreResources

@Composable
fun VideoPlayer(
    url: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.FillWidth,
) {
    val resources = remember { getCoreResources() }
    VideoPlayerView(
        modifier = modifier,
        url = url,
        playerConfig = resources.getPlayerConfig(contentScale),
    )
}
