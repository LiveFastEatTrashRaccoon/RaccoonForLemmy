package com.livefast.eattrash.raccoonforlemmy.core.utils.clipboard

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.Clipboard

@OptIn(ExperimentalComposeUiApi::class)
class DefaultClipboardHelper(
    private val clipboard: Clipboard,
) : ClipboardHelper {

    override suspend fun setText(text: String) {
        val newEntry = ClipEntry.withPlainText(text)
        clipboard.setClipEntry(newEntry)
    }

    override suspend fun getText(): String? =
        clipboard.getClipEntry()?.getPlainText()
}
