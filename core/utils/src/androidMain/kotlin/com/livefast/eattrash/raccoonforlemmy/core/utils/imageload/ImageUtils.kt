package com.livefast.eattrash.raccoonforlemmy.core.utils.imageload

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import coil3.decode.Decoder
import coil3.gif.AnimatedImageDecoder
import coil3.gif.GifDecoder
import coil3.network.NetworkFetcher

actual fun ByteArray.toComposeImageBitmap(): ImageBitmap = BitmapFactory.decodeByteArray(this, 0, size).asImageBitmap()

actual fun IntArray.toComposeImageBitmap(
    width: Int,
    height: Int,
): ImageBitmap = Bitmap.createBitmap(this, width, height, Bitmap.Config.ARGB_8888).asImageBitmap()

actual fun getNativeDecoders(): List<Decoder.Factory> =
    buildList {
        if (Build.VERSION.SDK_INT >= 28) {
            add(AnimatedImageDecoder.Factory())
        } else {
            add(GifDecoder.Factory())
        }
    }

actual fun getNativeFetchers(): List<NetworkFetcher.Factory> = emptyList()
