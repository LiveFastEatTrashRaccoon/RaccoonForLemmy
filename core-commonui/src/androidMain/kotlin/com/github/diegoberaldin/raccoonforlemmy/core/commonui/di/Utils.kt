package com.github.diegoberaldin.raccoonforlemmy.core.commonui.di

import com.github.diegoberaldin.raccoonforlemmy.core.commonui.communitydetail.CommunityDetailViewModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.postdetail.PostDetailViewModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.PostModel
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject

actual fun getPostDetailScreenViewModel(post: PostModel): PostDetailViewModel {
    val res: PostDetailViewModel by inject(
        clazz = PostDetailViewModel::class.java,
        parameters = { parametersOf(post) },
    )
    return res
}

actual fun getCommunityDetailScreenViewModel(community: CommunityModel): CommunityDetailViewModel {
    val res: CommunityDetailViewModel by inject(
        clazz = CommunityDetailViewModel::class.java,
        parameters = { parametersOf(community) },
    )
    return res
}
