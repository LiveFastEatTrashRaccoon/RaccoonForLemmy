package com.livefast.eattrash.raccoonforlemmy.unit.medialist.di

import com.livefast.eattrash.raccoonforlemmy.unit.medialist.MediaListMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.medialist.MediaListViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

val mediaListModule =
    DI.Module("MediaListModule") {
        bind<MediaListMviModel> {
            provider {
                MediaListViewModel(
                    apiConfigurationRepository = instance(),
                    identityRepository = instance(),
                    mediaRepository = instance(),
                    settingsRepository = instance(),
                    themeRepository = instance(),
                )
            }
        }
    }
