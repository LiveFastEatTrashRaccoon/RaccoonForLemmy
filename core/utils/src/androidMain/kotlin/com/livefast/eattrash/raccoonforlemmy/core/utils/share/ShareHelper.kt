package com.livefast.eattrash.raccoonforlemmy.core.utils.share

import org.koin.java.KoinJavaComponent.inject

actual fun getShareHelper(): ShareHelper {
    val res: ShareHelper by inject(ShareHelper::class.java)
    return res
}
