package com.livefast.eattrash.raccoonforlemmy.core.utils.di

import com.livefast.eattrash.raccoonforlemmy.core.utils.appicon.AppIconManager
import com.livefast.eattrash.raccoonforlemmy.core.utils.appicon.DefaultAppIconManager
import com.livefast.eattrash.raccoonforlemmy.core.utils.debug.AppInfoRepository
import com.livefast.eattrash.raccoonforlemmy.core.utils.debug.CrashReportConfiguration
import com.livefast.eattrash.raccoonforlemmy.core.utils.debug.CrashReportWriter
import com.livefast.eattrash.raccoonforlemmy.core.utils.debug.DefaultAppInfoRepository
import com.livefast.eattrash.raccoonforlemmy.core.utils.debug.DefaultCrashReportConfiguration
import com.livefast.eattrash.raccoonforlemmy.core.utils.debug.DefaultCrashReportWriter
import com.livefast.eattrash.raccoonforlemmy.core.utils.fs.DefaultFileSystemManager
import com.livefast.eattrash.raccoonforlemmy.core.utils.fs.FileSystemManager
import com.livefast.eattrash.raccoonforlemmy.core.utils.gallery.DefaultGalleryHelper
import com.livefast.eattrash.raccoonforlemmy.core.utils.gallery.GalleryHelper
import com.livefast.eattrash.raccoonforlemmy.core.utils.network.DefaultNetworkManager
import com.livefast.eattrash.raccoonforlemmy.core.utils.network.NetworkManager
import com.livefast.eattrash.raccoonforlemmy.core.utils.share.DefaultShareHelper
import com.livefast.eattrash.raccoonforlemmy.core.utils.share.ShareHelper
import com.livefast.eattrash.raccoonforlemmy.core.utils.url.CustomTabsHelper
import com.livefast.eattrash.raccoonforlemmy.core.utils.url.DefaultCustomTabsHelper
import com.livefast.eattrash.raccoonforlemmy.core.utils.vibrate.DefaultHapticFeedback
import com.livefast.eattrash.raccoonforlemmy.core.utils.vibrate.HapticFeedback
import org.koin.dsl.module

internal actual val networkModule =
    module {
        single<NetworkManager> {
            DefaultNetworkManager(
                context = get(),
            )
        }
    }

internal actual val appIconModule =
    module {
        single<AppIconManager> {
            DefaultAppIconManager(context = get())
        }
    }

internal actual val crashReportModule =
    module {
        single<CrashReportConfiguration> {
            DefaultCrashReportConfiguration(
                context = get(),
            )
        }
        single<CrashReportWriter> {
            DefaultCrashReportWriter(
                context = get(),
            )
        }
    }

internal actual val hapticFeedbackModule =
    module {
        single<HapticFeedback> {
            DefaultHapticFeedback(
                context = get(),
            )
        }
    }

internal actual val shareHelperModule =
    module {
        single<ShareHelper> {
            DefaultShareHelper(
                context = get(),
            )
        }
    }

internal actual val fileSystemModule =
    module {
        single<FileSystemManager> {
            DefaultFileSystemManager(
                context = get(),
            )
        }
    }

internal actual val galleryHelperModule =
    module {
        single<GalleryHelper> {
            DefaultGalleryHelper(
                context = get(),
            )
        }
    }

internal actual val customTabsModule =
    module {
        single<CustomTabsHelper> {
            DefaultCustomTabsHelper(
                context = get(),
            )
        }
    }

internal actual val appInfoModule =
    module {
        single<AppInfoRepository> {
            DefaultAppInfoRepository(
                context = get(),
            )
        }
    }
