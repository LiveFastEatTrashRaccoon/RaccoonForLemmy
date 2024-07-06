package com.github.diegoberaldin.raccoonforlemmy.unit.medialist.di

import com.github.diegoberaldin.raccoonforlemmy.unit.medialist.MediaListMviModel
import com.github.diegoberaldin.raccoonforlemmy.unit.medialist.MediaListViewModel
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
