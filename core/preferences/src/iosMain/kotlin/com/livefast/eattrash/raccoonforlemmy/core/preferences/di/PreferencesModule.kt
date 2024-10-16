package com.livefast.eattrash.raccoonforlemmy.core.preferences.di

import com.livefast.eattrash.raccoonforlemmy.core.preferences.DefaultTemporaryKeyStore
import com.livefast.eattrash.raccoonforlemmy.core.preferences.TemporaryKeyStore
import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.KeychainSettings
import com.russhwolf.settings.Settings
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

private const val DEFAULT_NAME = "secret_shared_prefs"

actual val corePreferencesModule =
    module {
        single<Settings> { params ->
            val name: String? = params[0]
            @OptIn(ExperimentalSettingsImplementation::class)
            KeychainSettings(service = name ?: DEFAULT_NAME)
        }
        single<TemporaryKeyStore> {
            DefaultTemporaryKeyStore(settings = get(parameters = { parametersOf(DEFAULT_NAME) }))
        }
    }
