package com.github.diegoberaldin.raccoonforlemmy.feature.home.di

import com.github.diegoberaldin.raccoonforlemmy.core.architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.di.commonUiModule
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.di.postsRepositoryModule
import com.github.diegoberaldin.raccoonforlemmy.feature.home.postlist.PostListViewModel
import com.github.diegoberaldin.raccoonforlemmy.feature.home.postlist.PostListMviModel
import org.koin.dsl.module

val homeTabModule = module {
    includes(
        postsRepositoryModule,
        commonUiModule,
    )
    factory {
        PostListViewModel(
            mvi = DefaultMviModel(PostListMviModel.UiState()),
            postsRepository = get(),
            apiConfigRepository = get(),
            identityRepository = get(),
            keyStore = get(),
            notificationCenter = get(),
        )
    }
}
