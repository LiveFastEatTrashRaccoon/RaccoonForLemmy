package com.livefast.eattrash.raccoonforlemmy.domain.identity.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan("com.livefast.eattrash.raccoonforlemmy.domain.identity.repository")
internal class RepositoryModule

@Module
@ComponentScan("com.livefast.eattrash.raccoonforlemmy.domain.identity.usecase")
internal class UseCaseModule

@Module
@ComponentScan("com.livefast.eattrash.raccoonforlemmy.domain.identity.urlhandler")
internal class UrlHandlerModule

@Module(
    includes = [
        RepositoryModule::class,
        UseCaseModule::class,
        UrlHandlerModule::class,
    ],
)
class IdentityModule
