package com.livefast.eattrash.raccoonforlemmy.core.commonui.detailopener.api

import org.koin.java.KoinJavaComponent.inject

actual fun getDetailOpener(): DetailOpener {
    val res: DetailOpener by inject(DetailOpener::class.java)
    return res
}
