package com.livefast.eattrash.raccoonforlemmy.core.preferences.di

import com.livefast.eattrash.raccoonforlemmy.core.preferences.appconfig.AppConfigDataSource
import com.livefast.eattrash.raccoonforlemmy.core.preferences.appconfig.AppConfigStore
import com.livefast.eattrash.raccoonforlemmy.core.preferences.appconfig.DefaultAppConfigStore
import com.livefast.eattrash.raccoonforlemmy.core.preferences.appconfig.LocalAppConfigDataSource
import com.livefast.eattrash.raccoonforlemmy.core.preferences.appconfig.RemoteAppConfigDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.qualifier.named
import org.koin.dsl.module

val coreAppConfigModule =
    module {
        single<AppConfigStore> {
            DefaultAppConfigStore(
                localDataSource = get(named("local")),
                remoteDataSource = get(named("remote")),
                dispatcher = Dispatchers.IO,
            )
        }
        single<AppConfigDataSource>(named("local")) {
            LocalAppConfigDataSource(
                keyStore = get(),
            )
        }
        single<AppConfigDataSource>(named("remote")) {
            RemoteAppConfigDataSource()
        }
    }
