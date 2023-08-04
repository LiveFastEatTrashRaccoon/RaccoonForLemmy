package com.github.diegoberaldin.raccoonforlemmy.core.commonui.di

import com.github.diegoberaldin.raccoonforlemmy.core.architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.communitydetail.CommunityDetailScreenMviModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.communitydetail.CommunityDetailScreenViewModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.postdetail.PostDetailScreenMviModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.postdetail.PostDetailScreenViewModel
import org.koin.dsl.module

val commonUiModule = module {
    factory { params ->
        PostDetailScreenViewModel(
            mvi = DefaultMviModel(PostDetailScreenMviModel.UiState()),
            post = params[0],
            identityRepository = get(),
            postsRepository = get(),
            commentRepository = get(),
            keyStore = get(),
            notificationCenter = get(),
        )
    }
    factory { params ->
        CommunityDetailScreenViewModel(
            mvi = DefaultMviModel(CommunityDetailScreenMviModel.UiState()),
            community = params[0],
            identityRepository = get(),
            postsRepository = get(),
            keyStore = get(),
        )
    }
}
