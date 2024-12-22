package com.livefast.eattrash.raccoonforlemmy.unit.licences.di

import com.livefast.eattrash.raccoonforlemmy.unit.licences.LicencesMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.licences.LicencesViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.provider

val licenceModule =
    DI.Module("LicenceModule") {
        bind<LicencesMviModel> {
            provider {
                LicencesViewModel()
            }
    }
}
