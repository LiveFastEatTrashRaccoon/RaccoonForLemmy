package com.github.diegoberaldin.raccoonforlemmy.core.commonui.di

import com.github.diegoberaldin.raccoonforlemmy.core.architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.postdetail.PostDetailScreenMviModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.postdetail.PostDetailScreenViewModel
import org.koin.dsl.module

val postDetailModule = module {
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
}
