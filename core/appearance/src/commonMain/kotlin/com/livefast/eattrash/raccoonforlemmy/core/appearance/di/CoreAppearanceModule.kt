package com.livefast.eattrash.raccoonforlemmy.core.appearance.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan("com.livefast.eattrash.raccoonforlemmy.core.appearance.repository")
internal class AppearanceRepositoryModule

@Module(
    includes = [
        AppearanceRepositoryModule::class,
        AppearanceThemeModule::class,
    ],
)
class AppearanceModule
