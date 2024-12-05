package com.livefast.eattrash.raccoonforlemmy.core.preferences.provider

import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.KeychainSettings
import com.russhwolf.settings.Settings
import org.koin.core.annotation.Single

@Single
internal actual class DefaultSettingsProvider : SettingsProvider {
    actual override fun provide(): Settings {
        @OptIn(ExperimentalSettingsImplementation::class)
        return KeychainSettings(service = DEFAULT_NAME)
    }

    companion object {
        const val DEFAULT_NAME = "secret_shared_prefs"
    }
}
