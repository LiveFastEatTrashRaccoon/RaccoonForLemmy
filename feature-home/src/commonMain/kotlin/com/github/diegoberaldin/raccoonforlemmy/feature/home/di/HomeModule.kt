package com.github.diegoberaldin.raccoonforlemmy.feature.home.di

import com.github.diegoberaldin.raccoonforlemmy.core.architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.di.commonUiModule
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.di.postsRepositoryModule
import com.github.diegoberaldin.raccoonforlemmy.feature.home.viewmodel.HomeScreenModel
import com.github.diegoberaldin.raccoonforlemmy.feature.home.viewmodel.HomeScreenMviModel
import org.koin.dsl.module

val homeTabModule = module {
    includes(
        postsRepositoryModule,
        commonUiModule,
    )
    factory {
        HomeScreenModel(
            mvi = DefaultMviModel(HomeScreenMviModel.UiState()),
            postsRepository = get(),
            apiConfigRepository = get(),
            identityRepository = get(),
            keyStore = get(),
            notificationCenter = get(),
        )
    }
}
