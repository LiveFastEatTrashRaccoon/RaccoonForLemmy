package com.github.diegoberaldin.raccoonforlemmy.feature.search

import com.github.diegoberaldin.raccoonforlemmy.core.architecture.DefaultMviModel
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.inject

actual val searchTabModule = module {
    factory {
        SearchScreenModel(
            mvi = DefaultMviModel(SearchScreenMviModel.UiState()),
        )
    }
}

actual fun getSearchScreenModel(): SearchScreenModel {
    val res: SearchScreenModel by inject(SearchScreenModel::class.java)
    return res
}
