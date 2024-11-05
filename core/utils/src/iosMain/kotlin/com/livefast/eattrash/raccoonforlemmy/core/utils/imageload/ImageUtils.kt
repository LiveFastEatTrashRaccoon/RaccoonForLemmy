package com.livefast.eattrash.raccoonforlemmy.core.utils.imageload

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import coil3.decode.Decoder
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.ColorAlphaType
import org.jetbrains.skia.ColorType
import org.jetbrains.skia.Image
import org.jetbrains.skia.ImageInfo
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual fun ByteArray.toComposeImageBitmap(): ImageBitmap = Image.makeFromEncoded(this).toComposeImageBitmap()

actual fun IntArray.toComposeImageBitmap(
    width: Int,
    height: Int,
): ImageBitmap {
    val bmp = Bitmap()
    val info = ImageInfo(width, height, ColorType.RGBA_8888, ColorAlphaType.PREMUL)
    bmp.installPixels(info, map { it.toByte() }.toByteArray(), info.minRowBytes)
    return bmp.asComposeImageBitmap()
}

actual fun getNativeDecoders(): List<Decoder.Factory> = emptyList()

actual fun getImageLoaderProvider(): ImageLoaderProvider = ImageUtilsDiHelper.imageLoaderProvider

internal object ImageUtilsDiHelper : KoinComponent {
    val imageLoaderProvider: ImageLoaderProvider by inject()
}
