package com.livefast.eattrash.raccoonforlemmy.core.preferences.di

import com.livefast.eattrash.raccoonforlemmy.core.preferences.provider.DefaultSettingsProvider
import com.livefast.eattrash.raccoonforlemmy.core.preferences.provider.DefaultSharedPreferencesProvider
import com.livefast.eattrash.raccoonforlemmy.core.preferences.provider.SettingsProvider
import com.livefast.eattrash.raccoonforlemmy.core.preferences.provider.SharedPreferencesProvider
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

internal actual val nativePreferencesModule =
    DI.Module("NativePreferencesModule") {
        bind<SharedPreferencesProvider> {
            singleton {
                DefaultSharedPreferencesProvider(
                    context = instance(),
                )
            }
        }
        bind<SettingsProvider> {
            singleton {
                DefaultSettingsProvider(
                    sharedPreferencesProvider = instance(),
                )
            }
        }
    }
