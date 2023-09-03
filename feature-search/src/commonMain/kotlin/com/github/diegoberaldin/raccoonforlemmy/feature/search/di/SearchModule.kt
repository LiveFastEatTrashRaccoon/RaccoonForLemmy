package com.github.diegoberaldin.raccoonforlemmy.feature.search.di

import com.github.diegoberaldin.raccoonforlemmy.core.architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.feature.search.communitylist.CommunityListViewModel
import com.github.diegoberaldin.raccoonforlemmy.feature.search.communitylist.CommunityListMviModel
import org.koin.dsl.module

val searchTabModule = module {
    factory {
        CommunityListViewModel(
            mvi = DefaultMviModel(CommunityListMviModel.UiState()),
            apiConfigRepository = get(),
            identityRepository = get(),
            communityRepository = get(),
            keyStore = get(),
        )
    }
}
