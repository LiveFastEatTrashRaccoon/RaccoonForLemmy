package com.livefast.eattrash.raccoonforlemmy.core.utils.di

import com.livefast.eattrash.raccoonforlemmy.core.utils.imageload.DefaultImageLoaderProvider
import com.livefast.eattrash.raccoonforlemmy.core.utils.imageload.DefaultImagePreloadManager
import com.livefast.eattrash.raccoonforlemmy.core.utils.imageload.ImageLoaderProvider
import com.livefast.eattrash.raccoonforlemmy.core.utils.imageload.ImagePreloadManager
import com.livefast.eattrash.raccoonforlemmy.core.utils.zombiemode.DefaultZombieModeHelper
import com.livefast.eattrash.raccoonforlemmy.core.utils.zombiemode.ZombieModeHelper
import org.koin.dsl.module

val utilsModule =
    module {
        factory<ZombieModeHelper> {
            DefaultZombieModeHelper()
        }
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
