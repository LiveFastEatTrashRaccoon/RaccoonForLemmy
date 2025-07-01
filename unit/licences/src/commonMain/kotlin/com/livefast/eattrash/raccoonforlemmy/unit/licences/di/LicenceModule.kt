package com.livefast.eattrash.raccoonforlemmy.unit.licences.di

import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.bindViewModel
import com.livefast.eattrash.raccoonforlemmy.unit.licences.LicencesViewModel
import org.kodein.di.DI

val licenceModule =
    DI.Module("LicenceModule") {
        bindViewModel {
            LicencesViewModel()
        }
    }
