package com.livefast.eattrash.raccoonforlemmy.core.preferences.di

import com.livefast.eattrash.raccoonforlemmy.core.preferences.store.DefaultTemporaryKeyStore
import com.livefast.eattrash.raccoonforlemmy.core.preferences.store.TemporaryKeyStore
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

val preferencesModule =
    DI.Module("PreferencesModule") {
        importAll(
            nativePreferencesModule,
            appConfigModule,
        )

        bind<TemporaryKeyStore> {
            singleton {
                DefaultTemporaryKeyStore(
                    settings = instance(),
                )
            }
        }
    }
