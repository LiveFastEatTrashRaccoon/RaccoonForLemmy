package com.github.diegoberaldin.raccoonforlemmy.core.commonui.di

import com.github.diegoberaldin.raccoonforlemmy.core.commonui.chat.InboxChatViewModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.communityInfo.CommunityInfoViewModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.communitydetail.CommunityDetailViewModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.createcomment.CreateCommentViewModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.createpost.CreatePostViewModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.drawer.DrawerCoordinator
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.drawer.ModalDrawerViewModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.image.ZoomableImageViewModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.instanceinfo.InstanceInfoViewModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.navigation.NavigationCoordinator
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.postdetail.PostDetailViewModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.saveditems.SavedItemsViewModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.userdetail.UserDetailViewModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.PostModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.UserModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

actual fun getNavigationCoordinator() = CommonUiViewModelHelper.navigationCoordinator

actual fun getDrawerCoordinator() = CommonUiViewModelHelper.drawerCoordinator

actual fun getPostDetailViewModel(
    post: PostModel,
    otherInstance: String,
    highlightCommentId: Int?,
): PostDetailViewModel =
    CommonUiViewModelHelper.getPostDetailModel(post, otherInstance, highlightCommentId)

actual fun getCommunityDetailViewModel(
    community: CommunityModel,
    otherInstance: String,
): CommunityDetailViewModel =
    CommonUiViewModelHelper.getCommunityDetailModel(community, otherInstance)

actual fun getCommunityInfoViewModel(community: CommunityModel): CommunityInfoViewModel =
    CommonUiViewModelHelper.getCommunityInfoModel(community)

actual fun getInstanceInfoViewModel(url: String): InstanceInfoViewModel =
    CommonUiViewModelHelper.getInstanceInfoModel(url)

actual fun getUserDetailViewModel(user: UserModel, otherInstance: String): UserDetailViewModel =
    CommonUiViewModelHelper.getUserDetailModel(user, otherInstance)

actual fun getCreateCommentViewModel(
    postId: Int?,
    parentId: Int?,
    editedCommentId: Int?,
): CreateCommentViewModel =
    CommonUiViewModelHelper.getCreateCommentModel(postId, parentId, editedCommentId)

actual fun getCreatePostViewModel(
    communityId: Int?,
    editedPostId: Int?,
): CreatePostViewModel =
    CommonUiViewModelHelper.getCreatePostModel(communityId, editedPostId)

actual fun getZoomableImageViewModel(): ZoomableImageViewModel =
    CommonUiViewModelHelper.zoomableImageModel

actual fun getInboxChatViewModel(otherUserId: Int) =
    CommonUiViewModelHelper.getChatViewModel(otherUserId)

actual fun getSavedItemsViewModel(): SavedItemsViewModel =
    CommonUiViewModelHelper.savedItemsViewModel

actual fun getModalDrawerViewModel() = CommonUiViewModelHelper.modalDrawerViewModel

object CommonUiViewModelHelper : KoinComponent {

    val navigationCoordinator: NavigationCoordinator by inject()
    val drawerCoordinator: DrawerCoordinator by inject()
    val zoomableImageModel: ZoomableImageViewModel by inject()
    val savedItemsViewModel: SavedItemsViewModel by inject()
    val modalDrawerViewModel: ModalDrawerViewModel by inject()

    fun getPostDetailModel(
        post: PostModel,
        otherInstance: String,
        highlightCommentId: Int?,
    ): PostDetailViewModel {
        val model: PostDetailViewModel by inject(
            parameters = { parametersOf(post, otherInstance, highlightCommentId) },
        )
        return model
    }

    fun getCommunityDetailModel(
        community: CommunityModel,
        otherInstance: String,
    ): CommunityDetailViewModel {
        val model: CommunityDetailViewModel by inject(
            parameters = { parametersOf(community, otherInstance) },
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

    fun getUserDetailModel(user: UserModel, otherInstance: String): UserDetailViewModel {
        val model: UserDetailViewModel by inject(
            parameters = { parametersOf(user, otherInstance) },
        )
        return model
    }

    fun getCreateCommentModel(
        postId: Int?,
        parentId: Int?,
        editedCommentId: Int?,
    ): CreateCommentViewModel {
        val model: CreateCommentViewModel by inject(
            parameters = { parametersOf(postId, parentId, editedCommentId) }
        )
        return model
    }

    fun getCreatePostModel(communityId: Int?, editedPostId: Int?): CreatePostViewModel {
        val model: CreatePostViewModel by inject(
            parameters = { parametersOf(communityId, editedPostId) }
        )
        return model
    }

    fun getChatViewModel(otherUserId: Int): InboxChatViewModel {
        val model: InboxChatViewModel by inject(
            parameters = { parametersOf(otherUserId) }
        )
        return model
    }
}
