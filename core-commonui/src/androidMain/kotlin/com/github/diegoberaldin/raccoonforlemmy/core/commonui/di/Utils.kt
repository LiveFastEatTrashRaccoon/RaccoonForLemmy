package com.github.diegoberaldin.raccoonforlemmy.core.commonui.di

import com.github.diegoberaldin.raccoonforlemmy.core.commonui.communitydetail.CommunityDetailViewModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.postdetail.PostDetailViewModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.userdetail.UserDetailViewModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.userdetail.comments.UserCommentsViewModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.userdetail.posts.UserPostsViewModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.PostModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.UserModel
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

actual fun getUserDetailScreenViewModel(user: UserModel): UserDetailViewModel {
    val res: UserDetailViewModel by inject(
        clazz = UserDetailViewModel::class.java,
        parameters = { parametersOf(user) },
    )
    return res
}

actual fun getUserPostsViewModel(
    user: UserModel,
): UserPostsViewModel {
    val res: UserPostsViewModel by inject(
        clazz = UserPostsViewModel::class.java,
        parameters = { parametersOf(user) },
    )
    return res
}

actual fun getUserCommentsViewModel(user: UserModel): UserCommentsViewModel {
    val res: UserCommentsViewModel by inject(
        clazz = UserCommentsViewModel::class.java,
        parameters = { parametersOf(user) },
    )
    return res
}
