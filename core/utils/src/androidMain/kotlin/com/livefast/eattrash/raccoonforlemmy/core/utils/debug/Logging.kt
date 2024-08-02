package com.livefast.eattrash.raccoonforlemmy.core.utils.debug

private const val TAG = "com.livefast.eattrash.raccoonforlemmy"

actual fun logDebug(message: String) {
    android.util.Log.d(TAG, message)
}
