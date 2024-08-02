package com.livefast.eattrash.raccoonforlemmy.core.commonui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/*
 * CREDITS:
 * https://www.droidcon.com/2023/07/31/unifying-video-players-compose-multiplatform-for-ios-android-desktop/
 */
@Composable
expect fun VideoPlayer(
    url: String,
    modifier: Modifier = Modifier,
    onPlaybackStarted: (() -> Unit)? = null,
)
