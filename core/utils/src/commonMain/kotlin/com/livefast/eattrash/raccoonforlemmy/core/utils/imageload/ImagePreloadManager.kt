package com.livefast.eattrash.raccoonforlemmy.core.utils.imageload

interface ImagePreloadManager {
    fun preload(url: String)

    fun remove(url: String)
}
