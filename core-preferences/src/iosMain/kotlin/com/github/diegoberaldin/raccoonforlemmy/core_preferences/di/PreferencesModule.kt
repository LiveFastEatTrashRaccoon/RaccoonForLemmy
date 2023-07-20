package com.github.diegoberaldin.raccoonforlemmy.core_preferences.di

import com.github.diegoberaldin.raccoonforlemmy.core_preferences.TemporaryKeyStore
import com.github.diegoberaldin.raccoonforlemmy.core_preferences.DefaultTemporaryKeyStore
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val corePreferencesModule = module {
    singleOf<TemporaryKeyStore>(::DefaultTemporaryKeyStore)
}

actual fun getTemporaryKeyStore(): TemporaryKeyStore {
    return TemporaryKeyStoreHelper.keyStore
}

object TemporaryKeyStoreHelper : KoinComponent {
    val keyStore: TemporaryKeyStore by inject()
}