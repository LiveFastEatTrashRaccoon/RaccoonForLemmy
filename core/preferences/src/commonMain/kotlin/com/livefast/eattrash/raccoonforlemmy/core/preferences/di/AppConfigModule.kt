package com.livefast.eattrash.raccoonforlemmy.core.preferences.di

import com.livefast.eattrash.raccoonforlemmy.core.preferences.appconfig.AppConfigDataSource
import com.livefast.eattrash.raccoonforlemmy.core.preferences.appconfig.AppConfigStore
import com.livefast.eattrash.raccoonforlemmy.core.preferences.appconfig.DefaultAppConfigStore
import com.livefast.eattrash.raccoonforlemmy.core.preferences.appconfig.LocalAppConfigDataSource
import com.livefast.eattrash.raccoonforlemmy.core.preferences.appconfig.RemoteAppConfigDataSource
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

internal val appConfigModule =
    DI.Module("AppConfigModule") {
        bind<AppConfigStore> {
            singleton {
                DefaultAppConfigStore(
                    localDataSource = instance(tag = "local"),
                    remoteDataSource = instance(tag = "remote"),
                )
            }
        }
        bind<AppConfigDataSource>(tag = "local") {
            singleton {
                LocalAppConfigDataSource(
                    keyStore = instance(),
                )
            }
        }
        bind<AppConfigDataSource>(tag = "remote") {
            singleton {
                RemoteAppConfigDataSource()
            }
        }
    }
