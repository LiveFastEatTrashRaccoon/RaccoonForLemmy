package com.livefast.eattrash.raccoonforlemmy.core.utils.di

import org.kodein.di.DI

internal actual val nativeImageLoadModule =
    DI.Module("NativeImageLoadModule") {
        // on Android nothing is needed
    }
