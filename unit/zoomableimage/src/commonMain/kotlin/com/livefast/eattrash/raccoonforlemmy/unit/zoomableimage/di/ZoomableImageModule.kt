package com.livefast.eattrash.raccoonforlemmy.unit.zoomableimage.di

import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.ViewModelCreationArgs
import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.bindViewModelWithArgs
import com.livefast.eattrash.raccoonforlemmy.unit.zoomableimage.ZoomableImageViewModel
import org.kodein.di.DI
import org.kodein.di.instance

data class ZoomableImageMviModelParams(val url: String) : ViewModelCreationArgs

val zoomableImageModule =
    DI.Module("ZoomableImageModule") {
        bindViewModelWithArgs { args: ZoomableImageMviModelParams ->
            ZoomableImageViewModel(
                url = args.url,
                settingsRepository = instance(),
                shareHelper = instance(),
                galleryHelper = instance(),
                imagePreloadManager = instance(),
            )
        }
    }
