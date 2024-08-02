package com.livefast.eattrash.raccoonforlemmy.unit.licences.di

import com.livefast.eattrash.raccoonforlemmy.unit.licences.LicencesMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.licences.LicencesViewModel
import org.koin.dsl.module

val licenceModule =
    module {
        factory<LicencesMviModel> { LicencesViewModel() }
    }
