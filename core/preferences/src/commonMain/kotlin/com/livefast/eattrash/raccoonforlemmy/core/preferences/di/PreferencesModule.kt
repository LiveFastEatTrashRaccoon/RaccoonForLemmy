package com.livefast.eattrash.raccoonforlemmy.core.preferences.di

import com.livefast.eattrash.raccoonforlemmy.core.preferences.provider.SettingsProvider
import com.russhwolf.settings.Settings
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan("com.livefast.eattrash.raccoonforlemmy.core.preferences.store")
internal class KeyStoreModule

@Module
internal class SettingsModule {
    @Single
    fun provideSettings(provider: SettingsProvider): Settings = provider.provide()
}

@Module(
    includes = [
        AppConfigModule::class,
        KeyStoreModule::class,
        ProviderModule::class,
        SettingsModule::class,
    ],
)
class PreferencesModule
