package com.livefast.eattrash.raccoonforlemmy.core.api.di

import com.livefast.eattrash.raccoonforlemmy.core.api.provider.DefaultServiceProvider
import com.livefast.eattrash.raccoonforlemmy.core.api.provider.ServiceProvider
import com.livefast.eattrash.raccoonforlemmy.core.utils.debug.AppInfoRepository
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Module
class ApiModule {
    @Single
    @Named("default")
    fun provideServiceProvider(appInfoRepository: AppInfoRepository): ServiceProvider =
        DefaultServiceProvider(appInfoRepository = appInfoRepository)

    @Single
    @Named("custom")
    fun provideCustomProvider(appInfoRepository: AppInfoRepository): ServiceProvider =
        DefaultServiceProvider(appInfoRepository = appInfoRepository)
}
