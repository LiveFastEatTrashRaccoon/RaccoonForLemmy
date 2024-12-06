package com.livefast.eattrash.raccoonforlemmy.core.utils.gallery

import androidx.compose.runtime.Composable
import org.koin.core.annotation.Single

@Single
internal expect class DefaultGalleryHelper : GalleryHelper {
    override val supportsCustomPath: Boolean

    override fun saveToGallery(
        bytes: ByteArray,
        name: String,
        additionalPathSegment: String?,
    ): Any?

    @Composable
    override fun getImageFromGallery(result: (ByteArray) -> Unit)
}
