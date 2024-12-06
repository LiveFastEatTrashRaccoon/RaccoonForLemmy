package com.livefast.eattrash.raccoonforlemmy.core.utils.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan("com.livefast.eattrash.raccoonforlemmy.core.utils.network")
internal actual class NetworkModule

@Module
@ComponentScan("com.livefast.eattrash.raccoonforlemmy.core.utils.appicon")
internal actual class AppIconModule

@Module
@ComponentScan("com.livefast.eattrash.raccoonforlemmy.core.utils.debug")
internal actual class CrashReportModule

@Module
@ComponentScan("com.livefast.eattrash.raccoonforlemmy.core.utils.vibrate")
internal actual class HapticFeedbackModule

@Module
@ComponentScan("com.livefast.eattrash.raccoonforlemmy.core.utils.share")
internal actual class ShareHelperModule

@Module
@ComponentScan("com.livefast.eattrash.raccoonforlemmy.core.utils.fs")
internal actual class FileSystemModule

@Module
@ComponentScan("com.livefast.eattrash.raccoonforlemmy.core.utils.gallery")
internal actual class GalleryHelperModule

@Module
@ComponentScan("com.livefast.eattrash.raccoonforlemmy.core.utils.url")
internal actual class CustomTabsModule

@Module
@ComponentScan("com.livefast.eattrash.raccoonforlemmy.core.utils.keepscreenon")
internal actual class KeepScreenOnModule
