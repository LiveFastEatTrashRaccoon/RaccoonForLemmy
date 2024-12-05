package com.livefast.eattrash.raccoonforlemmy.core.utils.share

import org.koin.core.annotation.Single
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIImage

@Single
internal actual class DefaultShareHelper : ShareHelper {
    actual override val supportsShareImage = false

    actual override fun share(
        url: String,
        mimeType: String,
    ) {
        val shareActivity = UIActivityViewController(listOf(url), null)
        val rvc = UIApplication.sharedApplication().keyWindow?.rootViewController
        rvc?.presentViewController(shareActivity, true, null)
    }

    actual override fun shareImage(
        path: Any?,
        mimeType: String,
    ) {
        val shareActivity =
            UIActivityViewController(
                listOf(UIImage(contentsOfFile = path?.toString().orEmpty())),
                null,
            )
        val rvc = UIApplication.sharedApplication().keyWindow?.rootViewController
        rvc?.presentViewController(shareActivity, true, null)
    }
}

actual fun getShareHelper(): ShareHelper = ShareHelperInjectHelper.shareHelper

object ShareHelperInjectHelper : KoinComponent {
    val shareHelper: ShareHelper by inject()
}
