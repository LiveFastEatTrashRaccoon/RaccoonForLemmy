package com.livefast.eattrash.raccoonforlemmy.unit.about.di

import com.livefast.eattrash.raccoonforlemmy.unit.about.AboutDialogMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.about.AboutDialogViewModel
import org.koin.dsl.module

val aboutModule =
    module {
        factory<AboutDialogMviModel> {
            AboutDialogViewModel(
                appInfoRepository = get(),
            )
        }
    }
