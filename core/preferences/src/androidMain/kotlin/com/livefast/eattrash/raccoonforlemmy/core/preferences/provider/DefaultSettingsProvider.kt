package com.livefast.eattrash.raccoonforlemmy.core.preferences.provider

import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings

internal class DefaultSettingsProvider(
    private val sharedPreferencesProvider: SharedPreferencesProvider,
) : SettingsProvider {
    override fun provide(): Settings =
        SharedPreferencesSettings(
            delegate = sharedPreferencesProvider.provide(PREFERENCES_NAME),
            commit = false,
        )

    companion object {
        private const val PREFERENCES_NAME = "secret_shared_prefs"
    }
}
