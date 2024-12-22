package com.livefast.eattrash.raccoonforlemmy.unit.zoomableimage.di

import com.livefast.eattrash.raccoonforlemmy.unit.zoomableimage.ZoomableImageMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.zoomableimage.ZoomableImageViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance

val zoomableImageModule =
    DI.Module("ZoomableImageModule") {
        bind<ZoomableImageMviModel> {
            factory { url: String ->
                ZoomableImageViewModel(
                    url = url,
                    settingsRepository = instance(),
                    shareHelper = instance(),
                    galleryHelper = instance(),
                    notificationCenter = instance(),
                    imagePreloadManager = instance(),
            )
        }
    }
}
