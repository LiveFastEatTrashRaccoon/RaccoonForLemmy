package com.livefast.eattrash.raccoonforlemmy.core.utils.imageload

import coil3.ImageLoader

interface ImageLoaderProvider {
    fun provideImageLoader(): ImageLoader
}
