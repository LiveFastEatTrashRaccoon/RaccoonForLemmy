package com.github.diegoberaldin.raccoonforlemmy.core_preferences.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.github.diegoberaldin.raccoonforlemmy.core_preferences.DefaultTemporaryKeyStore
import com.github.diegoberaldin.raccoonforlemmy.core_preferences.TemporaryKeyStore
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.inject

actual val corePreferencesModule = module {
    singleOf(::SharedPreferencesProvider)
    single<Settings> {
        val sharedPreferencesProvider: SharedPreferencesProvider by inject()
        SharedPreferencesSettings(
            sharedPreferencesProvider.sharedPreferences,
            false,
        )
    }
    single<TemporaryKeyStore> {
        DefaultTemporaryKeyStore(settings = get())
    }
}

actual fun getTemporaryKeyStore(): TemporaryKeyStore {
    val res by inject<TemporaryKeyStore>(TemporaryKeyStore::class.java)
    return res
}

private class SharedPreferencesProvider(
    context: Context,
) {

    companion object {
        const val PREFERENCES_NAME = "secret_shared_prefs"
    }

    private val masterKeyAlias: String = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        PREFERENCES_NAME,
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
}