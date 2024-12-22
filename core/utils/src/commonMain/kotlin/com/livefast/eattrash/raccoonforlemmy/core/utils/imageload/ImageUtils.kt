package com.livefast.eattrash.raccoonforlemmy.core.utils.imageload

import androidx.compose.ui.graphics.ImageBitmap
import coil3.decode.Decoder

expect fun ByteArray.toComposeImageBitmap(): ImageBitmap

expect fun IntArray.toComposeImageBitmap(
    width: Int,
    height: Int,
): ImageBitmap

expect fun getNativeDecoders(): List<Decoder.Factory>
