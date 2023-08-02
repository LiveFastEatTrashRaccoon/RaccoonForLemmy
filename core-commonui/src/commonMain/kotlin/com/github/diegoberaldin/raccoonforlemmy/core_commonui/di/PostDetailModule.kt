package com.github.diegoberaldin.raccoonforlemmy.core_commonui.di

import com.github.diegoberaldin.raccoonforlemmy.core_architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.core_commonui.postdetail.PostDetailScreenMviModel
import com.github.diegoberaldin.raccoonforlemmy.core_commonui.postdetail.PostDetailScreenViewModel
import org.koin.dsl.module

val postDetailModule = module {
    factory { params ->
        PostDetailScreenViewModel(
            mvi = DefaultMviModel(PostDetailScreenMviModel.UiState()),
            post = params[0],
            identityRepository = get(),
            commentRepository = get(),
            keyStore = get(),
        )
    }
}
