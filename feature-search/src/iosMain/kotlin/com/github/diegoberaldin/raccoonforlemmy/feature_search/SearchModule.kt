package com.github.diegoberaldin.raccoonforlemmy.feature_search

import com.github.diegoberaldin.raccoonforlemmy.core_architecture.DefaultMviModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.dsl.module

actual val searchTabModule = module {
    factory {
        SearchScreenModel(
            mvi = DefaultMviModel(SearchScreenMviModel.UiState())
        )
    }
}

actual fun getSearchScreenModel() = SearchScreenModelHelper.model

object SearchScreenModelHelper : KoinComponent {
    val model: SearchScreenModel by inject()
}