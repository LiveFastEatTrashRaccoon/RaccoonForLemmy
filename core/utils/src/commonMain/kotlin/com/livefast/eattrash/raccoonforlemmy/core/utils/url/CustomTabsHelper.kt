package com.livefast.eattrash.raccoonforlemmy.core.utils.url

interface CustomTabsHelper {
    val isSupported: Boolean

    fun handle(url: String)
}
