package com.livefast.eattrash.raccoonforlemmy.core.utils.di

import com.livefast.eattrash.raccoonforlemmy.core.utils.imageload.DefaultImageLoaderProvider
import com.livefast.eattrash.raccoonforlemmy.core.utils.imageload.DefaultImagePreloadManager
import com.livefast.eattrash.raccoonforlemmy.core.utils.imageload.ImageLoaderProvider
import com.livefast.eattrash.raccoonforlemmy.core.utils.imageload.ImagePreloadManager
import com.livefast.eattrash.raccoonforlemmy.core.utils.zombiemode.DefaultZombieModeHelper
import com.livefast.eattrash.raccoonforlemmy.core.utils.zombiemode.ZombieModeHelper
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

val utilsModule =
    DI.Module("UtilsModule") {
        importAll(
            nativeAppIconModule,
            nativeClipboardModule,
            nativeCrashReportModule,
            nativeCustomTabsModule,
            nativeFileSystemModule,
            nativeGalleryHelperModule,
            nativeImageLoadModule,
            nativeHapticFeedbackModule,
            nativeNetworkModule,
            nativeShareHelperModule,
        )

        bind<ImageLoaderProvider> {
            singleton {
                DefaultImageLoaderProvider(
                    context = instance(),
                    fileSystemManager = instance(),
                )
            }
        }
        bind<ImagePreloadManager> {
            singleton {
                DefaultImagePreloadManager(
                    context = instance(),
                    imageLoaderProvider = instance(),
                )
            }
        }
        bind<ZombieModeHelper> {
            singleton {
                DefaultZombieModeHelper()
            }
        }
    }
