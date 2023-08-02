package com.github.diegoberaldin.raccoonforlemmy.core_commonui.di

import com.github.diegoberaldin.raccoonforlemmy.core_commonui.postdetail.PostDetailScreenViewModel
import com.github.diegoberaldin.raccoonforlemmy.domain_lemmy.data.PostModel
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject

actual fun getPostDetailScreenViewModel(post: PostModel): PostDetailScreenViewModel {
    val res: PostDetailScreenViewModel by inject(
        clazz = PostDetailScreenViewModel::class.java,
        parameters = { parametersOf(post) },
    )
    return res
}
