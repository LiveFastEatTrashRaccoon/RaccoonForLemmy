package com.github.diegoberaldin.raccoonforlemmy.core.commonui.di

import com.github.diegoberaldin.raccoonforlemmy.core.commonui.communityInfo.CommunityInfoViewModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.communitydetail.CommunityDetailViewModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.instanceinfo.InstanceInfoViewModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.postdetail.PostDetailViewModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.userdetail.UserDetailViewModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.userdetail.comments.UserCommentsViewModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.userdetail.posts.UserPostsViewModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.PostModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.UserModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

actual fun getPostDetailViewModel(post: PostModel): PostDetailViewModel =
    PostDetailScreenViewModelHelper.getPostDetailModel(post)

actual fun getCommunityDetailViewModel(community: CommunityModel): CommunityDetailViewModel =
    PostDetailScreenViewModelHelper.getCommunityDetailModel(community)

actual fun getCommunityInfoViewModel(community: CommunityModel): CommunityInfoViewModel =
    PostDetailScreenViewModelHelper.getCommunityInfoModel(community)

actual fun getInstanceInfoViewModel(url: String): InstanceInfoViewModel =
    PostDetailScreenViewModelHelper.getInstanceInfoModel(url)

actual fun getUserDetailViewModel(user: UserModel): UserDetailViewModel =
    PostDetailScreenViewModelHelper.getUserDetailModel(user)

actual fun getUserPostsViewModel(user: UserModel): UserPostsViewModel =
    PostDetailScreenViewModelHelper.getUserPostsModel(user)

actual fun getUserCommentsViewModel(user: UserModel): UserCommentsViewModel =
    PostDetailScreenViewModelHelper.getUserCommentsModel(user)

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

    fun getCommunityInfoModel(community: CommunityModel): CommunityInfoViewModel {
        val model: CommunityInfoViewModel by inject(
            parameters = { parametersOf(community) },
        )
        return model
    }

    fun getInstanceInfoModel(url: String): InstanceInfoViewModel {
        val model: InstanceInfoViewModel by inject(
            parameters = { parametersOf(url) },
        )
        return model
    }

    fun getUserDetailModel(user: UserModel): UserDetailViewModel {
        val model: UserDetailViewModel by inject(
            parameters = { parametersOf(user) },
        )
        return model
    }

    fun getUserPostsModel(user: UserModel): UserPostsViewModel {
        val model: UserPostsViewModel by inject(
            parameters = { parametersOf(user) },
        )
        return model
    }

    fun getUserCommentsModel(user: UserModel): UserCommentsViewModel {
        val model: UserCommentsViewModel by inject(
            parameters = { parametersOf(user) },
        )
        return model
    }
}
