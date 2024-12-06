package com.livefast.eattrash.raccoonforlemmy.core.utils.gallery

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.annotation.Single
import org.koin.java.KoinJavaComponent.inject

private const val DEFAULT_BASE_PATH = "RaccoonForLemmy"

@Single
internal actual class DefaultGalleryHelper(
    private val context: Context,
) : GalleryHelper {
    actual override val supportsCustomPath: Boolean = true

    actual override fun saveToGallery(
        bytes: ByteArray,
        name: String,
        additionalPathSegment: String?,
    ): Any? {
        val relativePath =
            buildString {
                append(Environment.DIRECTORY_PICTURES)
                append("/")
                append(DEFAULT_BASE_PATH)
                if (!additionalPathSegment.isNullOrEmpty()) {
                    append("/")
                    append(additionalPathSegment)
                }
            }
        val resolver = context.applicationContext.contentResolver
        val details =
            ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, name)
                put(MediaStore.Images.Media.IS_PENDING, 1)
                put(MediaStore.Images.Media.RELATIVE_PATH, relativePath)
            }

        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, details)
        if (uri != null) {
            resolver.openFileDescriptor(uri, "w", null).use { pfd ->
                ParcelFileDescriptor.AutoCloseOutputStream(pfd).use {
                    it.write(bytes)
                }
            }
            details.clear()
            details.put(MediaStore.Images.Media.IS_PENDING, 0)
            resolver.update(uri, details, null, null)
        }

        return uri
    }

    @Composable
    actual override fun getImageFromGallery(result: (ByteArray) -> Unit) {
        val scope = rememberCoroutineScope()
        val resolver = context.contentResolver
        val pickMedia =
            rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    scope.launch(Dispatchers.IO) {
                        resolver.openInputStream(uri)?.use { stream ->
                            val bytes = stream.readBytes()
                            launch(Dispatchers.Main) {
                                result(bytes)
                            }
                        }
                    }
                } else {
                    result(byteArrayOf())
                }
            }
        SideEffect {
            pickMedia.launch(
                PickVisualMediaRequest
                    .Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    .build(),
            )
        }
    }
}

actual fun getGalleryHelper(): GalleryHelper {
    val res: GalleryHelper by inject(GalleryHelper::class.java)
    return res
}