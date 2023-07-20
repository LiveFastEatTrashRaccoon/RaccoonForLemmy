package com.github.diegoberaldin.raccoonforlemmy.core_preferences

import org.koin.java.KoinJavaComponent.inject


actual fun getKeyStoreFilePath(): String {
    val provider: AndroidKeyStoreFilePathProvider by inject(
        AndroidKeyStoreFilePathProvider::class.java
    )
    return provider.get()
}

