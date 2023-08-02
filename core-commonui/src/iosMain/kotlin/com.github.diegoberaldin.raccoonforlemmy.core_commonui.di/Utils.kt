package com.github.diegoberaldin.raccoonforlemmy.core_commonui.di

import com.github.diegoberaldin.raccoonforlemmy.core_commonui.postdetail.PostDetailScreenViewModel
import com.github.diegoberaldin.raccoonforlemmy.domain_lemmy.data.PostModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

actual fun getPostDetailScreenViewModel(post: PostModel): PostDetailScreenViewModel =
    PostDetailScreenViewModelHelper.getModel(post)

object PostDetailScreenViewModelHelper : KoinComponent {

    fun getModel(post: PostModel): PostDetailScreenViewModel {
        val model: PostDetailScreenViewModel by inject(
            parameters = { parametersOf(post) },
        )
        return model
    }
}
