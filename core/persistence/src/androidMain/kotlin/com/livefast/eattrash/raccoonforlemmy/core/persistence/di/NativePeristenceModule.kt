package com.livefast.eattrash.raccoonforlemmy.core.persistence.di

import com.livefast.eattrash.raccoonforlemmy.core.persistence.driver.DefaultDriverFactory
import com.livefast.eattrash.raccoonforlemmy.core.persistence.driver.DriverFactory
import com.livefast.eattrash.raccoonforlemmy.core.persistence.key.DatabaseKeyProvider
import com.livefast.eattrash.raccoonforlemmy.core.persistence.key.DefaultDatabaseKeyProvider
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

internal actual val nativePersistenceModule =
    DI.Module("NativePersistenceModule") {
        bind<DriverFactory> {
            singleton {
                DefaultDriverFactory(
                    context = instance(),
                    keyProvider = instance(),
                )
            }
        }
        bind<DatabaseKeyProvider> {
            singleton {
                DefaultDatabaseKeyProvider(
                    keyStore = instance(),
                )
            }
        }
    }
