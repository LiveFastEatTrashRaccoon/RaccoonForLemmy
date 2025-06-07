package com.livefast.eattrash.raccoonforlemmy.core.utils.di

import coil3.PlatformContext
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

internal actual val nativeImageLoadModule =
    DI.Module("NativeImageLoadModule") {
        bind<PlatformContext> {
            singleton {
                PlatformContext.INSTANCE
            }
        }
    }
