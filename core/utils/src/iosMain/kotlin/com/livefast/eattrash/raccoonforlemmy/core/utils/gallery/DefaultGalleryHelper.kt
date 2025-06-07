@file:OptIn(ExperimentalForeignApi::class)

package com.livefast.eattrash.raccoonforlemmy.core.utils.gallery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.interop.LocalUIViewController
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.create
import platform.PhotosUI.PHPickerConfiguration
import platform.PhotosUI.PHPickerResult
import platform.PhotosUI.PHPickerViewController
import platform.PhotosUI.PHPickerViewControllerDelegateProtocol
import platform.UIKit.UIImage
import platform.UIKit.UIImageWriteToSavedPhotosAlbum
import platform.UniformTypeIdentifiers.UTTypeImage
import platform.darwin.NSObject
import platform.posix.memcpy

typealias ImageBytes = NSData

@OptIn(BetaInteropApi::class, ExperimentalForeignApi::class)
fun ByteArray.toImageBytes(): ImageBytes =
    memScoped {
        return NSData.create(
            bytes = allocArrayOf(this@toImageBytes),
            length = this@toImageBytes.size.toULong(),
        )
    }

@OptIn(ExperimentalForeignApi::class)
fun ImageBytes.toByteArray(): ByteArray =
    ByteArray(this@toByteArray.length.toInt()).apply {
        usePinned {
            memcpy(it.addressOf(0), this@toByteArray.bytes, this@toByteArray.length)
        }
    }

internal class DefaultGalleryHelper : GalleryHelper {
    override val supportsCustomPath: Boolean = false

    @OptIn(ExperimentalForeignApi::class)
    override fun saveToGallery(
        bytes: ByteArray,
        name: String,
        additionalPathSegment: String?,
    ): Any? {
        val image = UIImage(bytes.toImageBytes())
        UIImageWriteToSavedPhotosAlbum(image, null, null, null)
        return null
    }

    @Composable
    override fun getImageFromGallery(result: (ByteArray) -> Unit) {
        val uiViewController = LocalUIViewController.current
        val pickerDelegate =
            remember {
                object : NSObject(), PHPickerViewControllerDelegateProtocol {
                    override fun picker(
                        picker: PHPickerViewController,
                        didFinishPicking: List<*>,
                    ) {
                        picker.dismissViewControllerAnimated(flag = false, completion = {})
                        val pickerResult = didFinishPicking.firstOrNull() as? PHPickerResult
                        if (pickerResult?.itemProvider?.hasItemConformingToTypeIdentifier(UTTypeImage.identifier) ==
                            true
                        ) {
                            pickerResult.itemProvider.loadDataRepresentationForTypeIdentifier(
                                typeIdentifier = UTTypeImage.identifier,
                            ) { data, _ ->
                                val bytes = data?.toByteArray() ?: byteArrayOf()
                                result(bytes)
                            }
                        } else {
                            result(byteArrayOf())
                        }
                    }
                }
            }
        SideEffect {
            val configuration = PHPickerConfiguration()
            val pickerController = PHPickerViewController(configuration)
            pickerController.setDelegate(pickerDelegate)
            uiViewController.presentViewController(
                pickerController,
                animated = true,
                completion = null,
            )
        }
    }
}
