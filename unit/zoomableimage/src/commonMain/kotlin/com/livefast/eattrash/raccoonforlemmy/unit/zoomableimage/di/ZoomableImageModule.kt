package com.livefast.eattrash.raccoonforlemmy.unit.zoomableimage.di

import com.livefast.eattrash.raccoonforlemmy.unit.zoomableimage.ZoomableImageMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.zoomableimage.ZoomableImageViewModel
import org.koin.dsl.module

val zoomableImageModule =
    module {
        factory<ZoomableImageMviModel> { params ->
            ZoomableImageViewModel(
                url = params[0],
                shareHelper = get(),
                galleryHelper = get(),
                settingsRepository = get(),
                notificationCenter = get(),
                imagePreloadManager = get(),
            )
        }
    }
