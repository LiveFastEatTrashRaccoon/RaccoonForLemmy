package com.livefast.eattrash.raccoonforlemmy.core.utils.di

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

fun getGalleryHelper(): GalleryHelper {
    val res by RootDI.di.instance<GalleryHelper>()
    return res
}

fun getImageLoaderProvider(): ImageLoaderProvider {
    val res by RootDI.di.instance<ImageLoaderProvider>()
    return res
}

fun getShareHelper(): ShareHelper {
    val res by RootDI.di.instance<ShareHelper>()
    return res
}

fun getClipboardHelper(clipboard: Clipboard): ClipboardHelper {
    val res by RootDI.di.instance<Clipboard, ClipboardHelper>(arg = clipboard)
    return res
}

