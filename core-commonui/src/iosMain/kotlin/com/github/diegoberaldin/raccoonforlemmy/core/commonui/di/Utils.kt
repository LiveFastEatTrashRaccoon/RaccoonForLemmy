package com.github.diegoberaldin.raccoonforlemmy.core.commonui.di

import com.github.diegoberaldin.raccoonforlemmy.core.commonui.communitydetail.CommunityDetailScreenViewModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.postdetail.PostDetailScreenViewModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.PostModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

actual fun getPostDetailScreenViewModel(post: PostModel): PostDetailScreenViewModel =
    PostDetailScreenViewModelHelper.getPostDetailModel(post)

actual fun getCommunityDetailScreenViewModel(community: CommunityModel): CommunityDetailScreenViewModel =
    PostDetailScreenViewModelHelper.getCommunityDetailModel(community)

object PostDetailScreenViewModelHelper : KoinComponent {

    fun getPostDetailModel(post: PostModel): PostDetailScreenViewModel {
        val model: PostDetailScreenViewModel by inject(
            parameters = { parametersOf(post) },
        )
        return model
    }

    fun getCommunityDetailModel(community: CommunityModel): CommunityDetailScreenViewModel {
        val model: CommunityDetailScreenViewModel by inject(
            parameters = { parametersOf(community) },
        )
        return model
    }
}
