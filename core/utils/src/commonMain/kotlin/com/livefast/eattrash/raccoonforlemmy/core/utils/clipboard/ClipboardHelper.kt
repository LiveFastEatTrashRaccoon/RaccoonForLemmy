package com.livefast.eattrash.raccoonforlemmy.core.utils.clipboard

interface ClipboardHelper {
    suspend fun setText(text: String)

    suspend fun getText(): String?
}
