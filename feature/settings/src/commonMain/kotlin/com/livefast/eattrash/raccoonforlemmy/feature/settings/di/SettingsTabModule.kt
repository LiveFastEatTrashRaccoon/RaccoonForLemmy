package com.livefast.eattrash.raccoonforlemmy.feature.settings.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan("com.livefast.eattrash.raccoonforlemmy.feature.settings.main")
internal class SettingsMainModule

@Module
@ComponentScan("com.livefast.eattrash.raccoonforlemmy.feature.settings.advanced")
internal class AdvancedSettingsModule

@Module
@ComponentScan("com.livefast.eattrash.raccoonforlemmy.feature.settings.colors")
internal class ColorAndFontModule

@Module(
    includes = [
        SettingsMainModule::class,
        AdvancedSettingsModule::class,
        ColorAndFontModule::class,
    ],
)
class SettingsTabModule
