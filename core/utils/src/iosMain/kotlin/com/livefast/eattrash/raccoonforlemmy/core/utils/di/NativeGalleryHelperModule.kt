package com.livefast.eattrash.raccoonforlemmy.core.utils.di

import com.livefast.eattrash.raccoonforlemmy.core.utils.gallery.DefaultGalleryHelper
import com.livefast.eattrash.raccoonforlemmy.core.utils.gallery.GalleryHelper
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

internal actual val nativeGalleryHelperModule =
    DI.Module("NativeGalleryHelperModule") {
        bind<GalleryHelper> {
            singleton {
                DefaultGalleryHelper()
            }
        }
    }
