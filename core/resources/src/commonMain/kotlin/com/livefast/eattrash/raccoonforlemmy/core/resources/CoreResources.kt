package com.livefast.eattrash.raccoonforlemmy.core.resources

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import chaintech.videoplayer.model.PlayerConfig

interface CoreResources {
    val github: Painter @Composable get
    val lemmy: Painter @Composable get
    val appIconDefault: Painter @Composable get
    val appIconAlt1: Painter @Composable get
    val appIconAlt2: Painter @Composable get
    val notoSans: FontFamily @Composable get
    val poppins: FontFamily @Composable get
    val charisSil: FontFamily @Composable get

    fun getPlayerConfig(contentScale: ContentScale): PlayerConfig
}
