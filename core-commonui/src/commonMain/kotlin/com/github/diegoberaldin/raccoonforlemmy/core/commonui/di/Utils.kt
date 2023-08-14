package com.github.diegoberaldin.raccoonforlemmy.core.commonui.di

import com.github.diegoberaldin.raccoonforlemmy.core.commonui.communityInfo.CommunityInfoViewModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.communitydetail.CommunityDetailViewModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.postdetail.PostDetailViewModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.userdetail.UserDetailViewModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.userdetail.comments.UserCommentsViewModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.userdetail.posts.UserPostsViewModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.PostModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.UserModel

expect fun getPostDetailScreenViewModel(
    post: PostModel,
): PostDetailViewModel

expect fun getCommunityDetailScreenViewModel(
    community: CommunityModel,
): CommunityDetailViewModel

expect fun getCommunityInfoScreenViewModel(
    community: CommunityModel,
): CommunityInfoViewModel

expect fun getUserDetailScreenViewModel(
    user: UserModel,
): UserDetailViewModel

expect fun getUserPostsViewModel(
    user: UserModel,
): UserPostsViewModel

expect fun getUserCommentsViewModel(
    user: UserModel,
): UserCommentsViewModel
