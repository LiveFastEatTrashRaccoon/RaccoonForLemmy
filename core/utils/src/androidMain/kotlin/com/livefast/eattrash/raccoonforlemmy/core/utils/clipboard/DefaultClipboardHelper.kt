package com.livefast.eattrash.raccoonforlemmy.core.utils.clipboard

import android.content.ClipData
import android.content.Context
import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.platform.toClipEntry

class DefaultClipboardHelper(
    private val clipboard: Clipboard,
    private val context: Context,
) : ClipboardHelper {
    override suspend fun setText(text: String) {
        val newEntry = ClipData.newPlainText("", text).toClipEntry()
        clipboard.setClipEntry(newEntry)
    }

    override suspend fun getText(): String? {
        val data = clipboard.getClipEntry()?.clipData ?: return null
        val count = data.itemCount
        check(count > 0) { return null }

        val item = data.getItemAt(count - 1)
        return item.coerceToText(context).toString()
    }
}
