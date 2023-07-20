package com.github.diegoberaldin.raccoonforlemmy.core_preferences.di

import com.github.diegoberaldin.raccoonforlemmy.core_preferences.TemporaryKeyStore
import com.github.diegoberaldin.raccoonforlemmy.core_preferences.AndroidKeyStoreFilePathProvider
import com.github.diegoberaldin.raccoonforlemmy.core_preferences.DefaultTemporaryKeyStore
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.inject

actual val corePreferencesModule = module {
    singleOf(::AndroidKeyStoreFilePathProvider)
    singleOf<TemporaryKeyStore>(::DefaultTemporaryKeyStore)
}

actual fun getTemporaryKeyStore(): TemporaryKeyStore {
    val res: TemporaryKeyStore by inject(TemporaryKeyStore::class.java)
    return res
}