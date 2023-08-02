package com.github.diegoberaldin.raccoonforlemmy.feature_home.di

import com.github.diegoberaldin.raccoonforlemmy.core_architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.core_commonui.di.postDetailModule
import com.github.diegoberaldin.raccoonforlemmy.domain_lemmy.repository.di.postsRepositoryModule
import com.github.diegoberaldin.raccoonforlemmy.feature_home.viewmodel.HomeScreenModel
import com.github.diegoberaldin.raccoonforlemmy.feature_home.viewmodel.HomeScreenMviModel
import org.koin.dsl.module

val homeTabModule = module {
    includes(
        postsRepositoryModule,
        postDetailModule,
    )
    factory {
        HomeScreenModel(
            mvi = DefaultMviModel(HomeScreenMviModel.UiState()),
            postsRepository = get(),
            apiConfigRepository = get(),
            identityRepository = get(),
            keyStore = get(),
        )
    }
}
