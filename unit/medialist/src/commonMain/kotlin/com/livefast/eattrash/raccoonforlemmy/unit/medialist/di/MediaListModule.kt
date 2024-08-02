package com.livefast.eattrash.raccoonforlemmy.unit.medialist.di

import com.livefast.eattrash.raccoonforlemmy.unit.medialist.MediaListMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.medialist.MediaListViewModel
import org.koin.dsl.module

val mediaListModule =
    module {
        factory<MediaListMviModel> {
            MediaListViewModel(
                apiConfigurationRepository = get(),
                identityRepository = get(),
                mediaRepository = get(),
                settingsRepository = get(),
                themeRepository = get(),
            )
        }
    }
