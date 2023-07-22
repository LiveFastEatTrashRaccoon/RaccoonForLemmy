package com.github.diegoberaldin.raccoonforlemmy.feature_profile

import com.github.diegoberaldin.raccoonforlemmy.core_architecture.DefaultMviModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.dsl.module

actual val profileTabModule = module {
    factory {
        ProfileScreenModel(
            mvi = DefaultMviModel(ProfileScreenMviModel.UiState())
        )
    }
}

actual fun getProfileScreenModel() = ProfileScreenModelHelper.model

object ProfileScreenModelHelper : KoinComponent {
    val model: ProfileScreenModel by inject()
}