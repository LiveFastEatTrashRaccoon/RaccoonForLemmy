package com.github.diegoberaldin.raccoonforlemmy.feature_home.di

import com.github.diegoberaldin.raccoonforlemmy.core_architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.domain_post.repository.di.postsRepositoryModule
import com.github.diegoberaldin.raccoonforlemmy.feature_home.viewmodel.HomeScreenModel
import com.github.diegoberaldin.raccoonforlemmy.feature_home.viewmodel.HomeScreenMviModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.dsl.module

actual val homeTabModule = module {
    includes(postsRepositoryModule)
    factory {
        HomeScreenModel(
            mvi = DefaultMviModel(HomeScreenMviModel.UiState()),
            postsRepository = get(),
            apiConfigRepository = get(),
            identityRepository = get(),
        )
    }
}

actual fun getHomeScreenModel() = HomeScreenModelHelper.model

object HomeScreenModelHelper : KoinComponent {
    val model: HomeScreenModel by inject()
}