package com.github.diegoberaldin.raccoonforlemmy.core_preferences

import android.content.Context

internal class AndroidKeyStoreFilePathProvider(
    private val context: Context,
) {
    fun get(): String = context.filesDir.resolve(DefaultTemporaryKeyStore.FILE_NAME).absolutePath
}