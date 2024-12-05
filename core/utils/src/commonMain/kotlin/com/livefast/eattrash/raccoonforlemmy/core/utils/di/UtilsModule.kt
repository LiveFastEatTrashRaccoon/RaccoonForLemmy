package com.livefast.eattrash.raccoonforlemmy.core.utils.di

import com.livefast.eattrash.raccoonforlemmy.core.utils.imageload.DefaultImageLoaderProvider
import com.livefast.eattrash.raccoonforlemmy.core.utils.imageload.DefaultImagePreloadManager
import com.livefast.eattrash.raccoonforlemmy.core.utils.imageload.ImageLoaderProvider
import com.livefast.eattrash.raccoonforlemmy.core.utils.imageload.ImagePreloadManager
import com.livefast.eattrash.raccoonforlemmy.core.utils.zombiemode.DefaultZombieModeHelper
import com.livefast.eattrash.raccoonforlemmy.core.utils.zombiemode.ZombieModeHelper
import org.koin.dsl.module

private val imageLoadModule = module {
    single<ImageLoaderProvider> {
        DefaultImageLoaderProvider(
            context = get(),
            fileSystemManager = get(),
        )
    }

    single<ImagePreloadManager> {
        DefaultImagePreloadManager(
            context = get(),
            imageLoaderProvider = get(),
        )
    }
}

private val zombieModeModule = module {
    factory<ZombieModeHelper> {
        DefaultZombieModeHelper()
    }
}

val utilsModule =
    module {
        includes(
            appIconModule,
            appInfoModule,
            crashReportModule,
            customTabsModule,
            fileSystemModule,
            galleryHelperModule,
            hapticFeedbackModule,
            imageLoadModule,
            networkModule,
            shareHelperModule,
            zombieModeModule,
        )
    }
