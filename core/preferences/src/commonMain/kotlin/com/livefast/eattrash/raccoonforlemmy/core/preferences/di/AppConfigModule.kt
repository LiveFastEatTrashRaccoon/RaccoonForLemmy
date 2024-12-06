package com.livefast.eattrash.raccoonforlemmy.core.preferences.di

import com.livefast.eattrash.raccoonforlemmy.core.preferences.appconfig.AppConfigDataSource
import com.livefast.eattrash.raccoonforlemmy.core.preferences.appconfig.LocalAppConfigDataSource
import com.livefast.eattrash.raccoonforlemmy.core.preferences.appconfig.RemoteAppConfigDataSource
import com.livefast.eattrash.raccoonforlemmy.core.preferences.store.TemporaryKeyStore
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Module
@ComponentScan("com.livefast.eattrash.raccoonforlemmy.core.preferences.appconfig")
internal class AppConfigModule {
    @Single
    @Named("local")
    fun provideLocalDatastore(keyStore: TemporaryKeyStore): AppConfigDataSource = LocalAppConfigDataSource(keyStore = keyStore)

    @Single
    @Named("remote")
    fun provideRemoteDatastore(): AppConfigDataSource = RemoteAppConfigDataSource()
}
