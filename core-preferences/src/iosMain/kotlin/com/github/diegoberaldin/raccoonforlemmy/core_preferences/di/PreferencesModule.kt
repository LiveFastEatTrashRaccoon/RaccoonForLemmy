package com.github.diegoberaldin.raccoonforlemmy.core_preferences.di

import com.github.diegoberaldin.raccoonforlemmy.core_preferences.DefaultTemporaryKeyStore
import com.github.diegoberaldin.raccoonforlemmy.core_preferences.TemporaryKeyStore
import com.russhwolf.settings.KeychainSettings
import com.russhwolf.settings.Settings
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.dsl.module

actual val corePreferencesModule = module {
    single<Settings> {
        KeychainSettings(service = "secret_shared_prefs")
    }
    single<TemporaryKeyStore> {
        DefaultTemporaryKeyStore(settings = get())
    }
}

actual fun getTemporaryKeyStore(): TemporaryKeyStore {
    return TemporaryKeyStoreHelper.temporaryKeyStore
}

object TemporaryKeyStoreHelper : KoinComponent {
    val temporaryKeyStore: TemporaryKeyStore by inject()
}