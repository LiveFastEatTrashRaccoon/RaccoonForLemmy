package com.livefast.eattrash.raccoonforlemmy.core.utils.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan("com.livefast.eattrash.raccoonforlemmy.core.utils.imageload")
internal class ImageLoadModule

@Module
@ComponentScan("com.livefast.eattrash.raccoonforlemmy.core.utils.zombiemode")
internal class ZombieModeModule

@Module(
    includes = [
        ImageLoadModule::class,
        ZombieModeModule::class,
        NetworkModule::class,
        AppIconModule::class,
        CrashReportModule::class,
        FileSystemModule::class,
        GalleryHelperModule::class,
        ShareHelperModule::class,
        CustomTabsModule::class,
        HapticFeedbackModule::class,
        KeepScreenOnModule::class,
    ],
)
class UtilsModule
