package com.livefast.eattrash.raccoonforlemmy.di

import org.koin.core.annotation.Module

@Module(
    includes = [
        ResourcesModule::class,
        MainModule::class,
        DetailOpenModule::class,
    ],
)
internal class SharedModule
