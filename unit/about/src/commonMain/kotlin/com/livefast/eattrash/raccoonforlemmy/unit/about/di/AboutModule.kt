package com.livefast.eattrash.raccoonforlemmy.unit.about.di

import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.bindViewModel
import com.livefast.eattrash.raccoonforlemmy.unit.about.AboutDialogViewModel
import org.kodein.di.DI
import org.kodein.di.instance

val aboutModule =
    DI.Module("AboutModule") {
        bindViewModel {
            AboutDialogViewModel(
                appInfoRepository = instance(),
            )
        }
    }
