package com.github.diegoberaldin.racconforlemmy.core_utils

actual object Log {

    private const val TAG = "com.github.diegoberaldin.racconforlemmy"
    actual fun d(message: String) {
        android.util.Log.d(TAG, message)
    }
}