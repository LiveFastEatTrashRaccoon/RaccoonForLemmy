package com.livefast.eattrash.raccoonforlemmy.core.utils.imageload

import coil3.PlatformContext
import coil3.memory.MemoryCache
import coil3.request.ImageRequest

internal class DefaultImagePreloadManager(
    private val context: PlatformContext,
    private val imageLoaderProvider: ImageLoaderProvider,
) : ImagePreloadManager {
    override fun preload(url: String) {
        val imageLoader = imageLoaderProvider.provideImageLoader()
        val request =
            ImageRequest
                .Builder(context)
                .data(url)
                .build()
        imageLoader.enqueue(request)
    }

    override fun remove(url: String) {
        val imageLoader = imageLoaderProvider.provideImageLoader()
        imageLoader.memoryCache?.remove(MemoryCache.Key(url))
        imageLoader.diskCache?.remove(url)
    }
}
