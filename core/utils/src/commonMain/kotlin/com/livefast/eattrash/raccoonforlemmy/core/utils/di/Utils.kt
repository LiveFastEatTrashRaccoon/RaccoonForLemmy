package com.livefast.eattrash.raccoonforlemmy.core.utils.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.Clipboard
import com.livefast.eattrash.raccoonforlemmy.core.di.RootDI
import com.livefast.eattrash.raccoonforlemmy.core.utils.clipboard.ClipboardHelper
import com.livefast.eattrash.raccoonforlemmy.core.utils.fs.FileSystemManager
import com.livefast.eattrash.raccoonforlemmy.core.utils.gallery.GalleryHelper
import com.livefast.eattrash.raccoonforlemmy.core.utils.imageload.ImageLoaderProvider
import com.livefast.eattrash.raccoonforlemmy.core.utils.share.ShareHelper
import org.kodein.di.instance

fun getFileSystemManager(): FileSystemManager {
    val res by RootDI.di.instance<FileSystemManager>()
    return res
}

@Composable
fun rememberFileSystemManager(): FileSystemManager = remember { getFileSystemManager() }

fun getGalleryHelper(): GalleryHelper {
    val res by RootDI.di.instance<GalleryHelper>()
    return res
}

@Composable
fun rememberGalleryHelper(): GalleryHelper = remember { getGalleryHelper() }

fun getImageLoaderProvider(): ImageLoaderProvider {
    val res by RootDI.di.instance<ImageLoaderProvider>()
    return res
}

@Composable
fun rememberImageLoaderProvider(): ImageLoaderProvider = remember { getImageLoaderProvider() }

fun getShareHelper(): ShareHelper {
    val res by RootDI.di.instance<ShareHelper>()
    return res
}

@Composable
fun rememberShareHelper(): ShareHelper = remember { getShareHelper() }

fun getClipboardHelper(clipboard: Clipboard): ClipboardHelper {
    val res by RootDI.di.instance<Clipboard, ClipboardHelper>(arg = clipboard)
    return res
}

@Composable
fun rememberClipboardHelper(clipboard: Clipboard): ClipboardHelper = remember { getClipboardHelper(clipboard) }

