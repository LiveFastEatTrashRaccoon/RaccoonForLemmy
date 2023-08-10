package com.github.diegoberaldin.raccoonforlemmy.core.commonui.di

import com.github.diegoberaldin.raccoonforlemmy.core.commonui.communitydetail.CommunityDetailViewModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.postdetail.PostDetailViewModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.PostModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

actual fun getPostDetailScreenViewModel(post: PostModel): PostDetailViewModel =
    PostDetailScreenViewModelHelper.getPostDetailModel(post)

actual fun getCommunityDetailScreenViewModel(community: CommunityModel): CommunityDetailViewModel =
    PostDetailScreenViewModelHelper.getCommunityDetailModel(community)

object PostDetailScreenViewModelHelper : KoinComponent {

    fun getPostDetailModel(post: PostModel): PostDetailViewModel {
        val model: PostDetailViewModel by inject(
            parameters = { parametersOf(post) },
        )
        return model
    }

    fun getCommunityDetailModel(community: CommunityModel): CommunityDetailViewModel {
        val model: CommunityDetailViewModel by inject(
            parameters = { parametersOf(community) },
        )
        return model
    }
}
