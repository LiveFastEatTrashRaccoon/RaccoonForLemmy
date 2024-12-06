package com.livefast.eattrash.raccoonforlemmy.core.persistence.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan("com.livefast.eattrash.raccoonforlemmy.core.persistence.provider")
internal class ProviderModule

@Module
@ComponentScan("com.livefast.eattrash.raccoonforlemmy.core.persistence.repository")
internal class RepositoryModule

@Module
@ComponentScan("com.livefast.eattrash.raccoonforlemmy.core.persistence.usecase")
internal class UseCaseModule

@Module(
    includes = [
        DriverModule::class,
        KeyModule::class,
        ProviderModule::class,
        RepositoryModule::class,
        UseCaseModule::class,
    ],
)
class PersistenceModule
