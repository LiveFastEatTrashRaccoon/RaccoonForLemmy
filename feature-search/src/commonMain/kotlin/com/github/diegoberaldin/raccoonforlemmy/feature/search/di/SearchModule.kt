package com.github.diegoberaldin.raccoonforlemmy.feature.search.di

import com.github.diegoberaldin.raccoonforlemmy.core.architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.feature.search.viewmodel.SearchScreenModel
import com.github.diegoberaldin.raccoonforlemmy.feature.search.viewmodel.SearchScreenMviModel
import org.koin.dsl.module

val searchTabModule = module {
    factory {
        SearchScreenModel(
            mvi = DefaultMviModel(SearchScreenMviModel.UiState()),
            apiConfigRepository = get(),
            identityRepository = get(),
            communityRepository = get(),
        )
    }
}
