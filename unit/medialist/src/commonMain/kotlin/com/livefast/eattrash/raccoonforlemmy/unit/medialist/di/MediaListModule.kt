package com.livefast.eattrash.raccoonforlemmy.unit.medialist.di

import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.bindViewModel
import com.livefast.eattrash.raccoonforlemmy.unit.medialist.MediaListViewModel
import org.kodein.di.DI
import org.kodein.di.instance

val mediaListModule =
    DI.Module("MediaListModule") {
        bindViewModel {
            MediaListViewModel(
                apiConfigurationRepository = instance(),
                identityRepository = instance(),
                mediaRepository = instance(),
                settingsRepository = instance(),
                themeRepository = instance(),
            )
        }
    }
