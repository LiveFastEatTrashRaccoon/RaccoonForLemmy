package com.github.diegoberaldin.raccoonforlemmy.core.commonui.di

import com.github.diegoberaldin.raccoonforlemmy.core.commonui.chat.InboxChatViewModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.communityInfo.CommunityInfoViewModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.communitydetail.CommunityDetailViewModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.createcomment.CreateCommentViewModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.createpost.CreatePostViewModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.image.ZoomableImageViewModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.instanceinfo.InstanceInfoViewModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.navigation.NavigationCoordinator
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.postdetail.PostDetailViewModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.userdetail.UserDetailViewModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.PostModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.UserModel

expect fun getNavigationCoordinator(): NavigationCoordinator

expect fun getPostDetailViewModel(
    post: PostModel,
    highlightCommentId: Int? = null,
): PostDetailViewModel

expect fun getCommunityDetailViewModel(
    community: CommunityModel,
    otherInstance: String = "",
): CommunityDetailViewModel

expect fun getCommunityInfoViewModel(
    community: CommunityModel,
): CommunityInfoViewModel

expect fun getUserDetailViewModel(
    user: UserModel,
    otherInstance: String = "",
): UserDetailViewModel

expect fun getInstanceInfoViewModel(
    url: String,
): InstanceInfoViewModel

expect fun getCreateCommentViewModel(
    postId: Int? = null,
    parentId: Int? = null,
    editedCommentId: Int? = null,
): CreateCommentViewModel

expect fun getCreatePostViewModel(
    communityId: Int?,
    editedPostId: Int?,
): CreatePostViewModel

expect fun getZoomableImageViewModel(): ZoomableImageViewModel

expect fun getInboxChatViewModel(otherUserId: Int): InboxChatViewModel