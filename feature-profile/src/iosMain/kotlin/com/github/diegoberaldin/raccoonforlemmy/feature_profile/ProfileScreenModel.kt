package com.github.diegoberaldin.raccoonforlemmy.feature_profile

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual fun getProfileScreenModel() = ProfileScreenModelHelper.model

object ProfileScreenModelHelper : KoinComponent {
    val model: ProfileScreenModel by inject()
}