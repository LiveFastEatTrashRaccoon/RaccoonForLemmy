package com.livefast.eattrash.raccoonforlemmy.core.preferences.provider

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

internal class DefaultSharedPreferencesProvider(private val context: Context) : SharedPreferencesProvider {
    private val masterKeyAlias: String = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    override fun provide(name: String): SharedPreferences = EncryptedSharedPreferences.create(
        name,
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
    )
}
