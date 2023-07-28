package com.github.diegoberaldin.raccoonforlemmy.feature_home.di

import com.github.diegoberaldin.raccoonforlemmy.core_architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.domain_post.repository.di.postsRepositoryModule
import com.github.diegoberaldin.raccoonforlemmy.feature_home.viewmodel.HomeScreenModel
import com.github.diegoberaldin.raccoonforlemmy.feature_home.viewmodel.HomeScreenMviModel
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.inject

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

actual fun getHomeScreenModel(): HomeScreenModel {
    val res: HomeScreenModel by inject(HomeScreenModel::class.java)
    return res
}