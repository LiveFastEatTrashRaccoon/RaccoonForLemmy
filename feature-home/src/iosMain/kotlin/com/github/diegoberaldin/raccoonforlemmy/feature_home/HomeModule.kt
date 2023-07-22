package com.github.diegoberaldin.raccoonforlemmy.feature_home

import com.github.diegoberaldin.raccoonforlemmy.core_architecture.DefaultMviModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.dsl.module

actual val homeTabModule = module {
    factory {
        HomeScreenModel(
            mvi = DefaultMviModel(HomeScreenMviModel.UiState())
        )
    }
}

actual fun getHomeScreenModel() = HomeScreenModelHelper.model

object HomeScreenModelHelper : KoinComponent {
    val model: HomeScreenModel by inject()
}