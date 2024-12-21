package com.livefast.eattrash.raccoonforlemmy.unit.about.di

import com.livefast.eattrash.raccoonforlemmy.unit.about.AboutDialogMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.about.AboutDialogViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

val aboutModule =
    DI.Module("AboutModule") {
        bind<AboutDialogMviModel> {
            provider {
                AboutDialogViewModel(
                    appInfoRepository = instance(),
                )
            }
        }
    }
