package com.livefast.eattrash.raccoonforlemmy.core.utils.di

import com.livefast.eattrash.raccoonforlemmy.core.di.RootDI
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
