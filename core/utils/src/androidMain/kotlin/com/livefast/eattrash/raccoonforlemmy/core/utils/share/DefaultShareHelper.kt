package com.livefast.eattrash.raccoonforlemmy.core.utils.share

import android.content.Context
import android.content.Intent
import android.net.Uri
import org.koin.core.annotation.Single

@Single
internal actual class DefaultShareHelper(
    private val context: Context,
) : ShareHelper {
    actual override val supportsShareImage = true

    actual override fun share(
        url: String,
        mimeType: String,
    ) {
        val sendIntent: Intent =
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, url)
                type = mimeType
            }

        val shareIntent =
            Intent.createChooser(sendIntent, null).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        context.startActivity(shareIntent)
    }

    actual override fun shareImage(
        path: Any?,
        mimeType: String,
    ) {
        val uri = path as? Uri ?: throw Exception("Path must be an Uri")
        val sendIntent: Intent =
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, uri)
                flags += Intent.FLAG_GRANT_READ_URI_PERMISSION
                type = mimeType
            }

        val shareIntent =
            Intent.createChooser(sendIntent, null).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        context.startActivity(shareIntent)
    }
}
