package com.github.diegoberaldin.raccoonforlemmy.core.commonui.di

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalTextToolbar
import androidx.compose.ui.platform.TextToolbar
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.DefaultImagePreloadManager
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.chat.InboxChatMviModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.communityInfo.CommunityInfoMviModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.communitydetail.CommunityDetailMviModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.FabNestedScrollConnection
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.createcomment.CreateCommentMviModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.createpost.CreatePostMviModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.drawer.DrawerCoordinator
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.drawer.ModalDrawerMviModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.image.ImagePreloadManager
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.image.ZoomableImageMviModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.instanceinfo.InstanceInfoMviModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.navigation.NavigationCoordinator
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.postdetail.PostDetailMviModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.report.CreateReportMviModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.saveditems.SavedItemsMviModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.selectcommunity.SelectCommunityMviModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.userdetail.UserDetailMviModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.PostModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.UserModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

actual val imagePreloadModule = module {
    single<ImagePreloadManager> {
        DefaultImagePreloadManager()
    }
}

actual fun getNavigationCoordinator() = CommonUiViewModelHelper.navigationCoordinator

actual fun getDrawerCoordinator() = CommonUiViewModelHelper.drawerCoordinator

actual fun getFabNestedScrollConnection(): FabNestedScrollConnection =
    CommonUiViewModelHelper.fabNestedScrollConnection

actual fun getPostDetailViewModel(
    post: PostModel,
    otherInstance: String,
    highlightCommentId: Int?,
): PostDetailMviModel =
    CommonUiViewModelHelper.getPostDetailModel(post, otherInstance, highlightCommentId)

actual fun getCommunityDetailViewModel(
    community: CommunityModel,
    otherInstance: String,
): CommunityDetailMviModel =
    CommonUiViewModelHelper.getCommunityDetailModel(community, otherInstance)

actual fun getCommunityInfoViewModel(community: CommunityModel): CommunityInfoMviModel =
    CommonUiViewModelHelper.getCommunityInfoModel(community)

actual fun getInstanceInfoViewModel(url: String): InstanceInfoMviModel =
    CommonUiViewModelHelper.getInstanceInfoModel(url)

actual fun getUserDetailViewModel(user: UserModel, otherInstance: String): UserDetailMviModel =
    CommonUiViewModelHelper.getUserDetailModel(user, otherInstance)

actual fun getCreateCommentViewModel(
    postId: Int?,
    parentId: Int?,
    editedCommentId: Int?,
): CreateCommentMviModel =
    CommonUiViewModelHelper.getCreateCommentModel(postId, parentId, editedCommentId)

actual fun getCreatePostViewModel(
    editedPostId: Int?,
): CreatePostMviModel =
    CommonUiViewModelHelper.getCreatePostModel(editedPostId)

actual fun getZoomableImageViewModel(): ZoomableImageMviModel =
    CommonUiViewModelHelper.zoomableImageModel

actual fun getInboxChatViewModel(otherUserId: Int): InboxChatMviModel =
    CommonUiViewModelHelper.getChatViewModel(otherUserId)

actual fun getSavedItemsViewModel(): SavedItemsMviModel =
    CommonUiViewModelHelper.savedItemsViewModel

actual fun getModalDrawerViewModel(): ModalDrawerMviModel =
    CommonUiViewModelHelper.modalDrawerViewModel

actual fun getCreateReportViewModel(
    postId: Int?,
    commentId: Int?,
): CreateReportMviModel = CommonUiViewModelHelper.getCreateReportModel(postId, commentId)

actual fun getSelectCommunityViewModel(): SelectCommunityMviModel =
    CommonUiViewModelHelper.selectCommunityViewModel

object CommonUiViewModelHelper : KoinComponent {

    val navigationCoordinator: NavigationCoordinator by inject()
    val drawerCoordinator: DrawerCoordinator by inject()
    val fabNestedScrollConnection: FabNestedScrollConnection by inject()
    val zoomableImageModel: ZoomableImageMviModel by inject()
    val savedItemsViewModel: SavedItemsMviModel by inject()
    val modalDrawerViewModel: ModalDrawerMviModel by inject()
    val selectCommunityViewModel: SelectCommunityMviModel by inject()

    fun getPostDetailModel(
        post: PostModel,
        otherInstance: String,
        highlightCommentId: Int?,
    ): PostDetailMviModel {
        val model: PostDetailMviModel by inject(
            parameters = { parametersOf(post, otherInstance, highlightCommentId) },
        )
        return model
    }

    fun getCommunityDetailModel(
        community: CommunityModel,
        otherInstance: String,
    ): CommunityDetailMviModel {
        val model: CommunityDetailMviModel by inject(
            parameters = { parametersOf(community, otherInstance) },
        )
        return model
    }

    fun getCommunityInfoModel(community: CommunityModel): CommunityInfoMviModel {
        val model: CommunityInfoMviModel by inject(
            parameters = { parametersOf(community) },
        )
        return model
    }

    fun getInstanceInfoModel(url: String): InstanceInfoMviModel {
        val model: InstanceInfoMviModel by inject(
            parameters = { parametersOf(url) },
        )
        return model
    }

    fun getUserDetailModel(user: UserModel, otherInstance: String): UserDetailMviModel {
        val model: UserDetailMviModel by inject(
            parameters = { parametersOf(user, otherInstance) },
        )
        return model
    }

    fun getCreateCommentModel(
        postId: Int?,
        parentId: Int?,
        editedCommentId: Int?,
    ): CreateCommentMviModel {
        val model: CreateCommentMviModel by inject(
            parameters = { parametersOf(postId, parentId, editedCommentId) }
        )
        return model
    }

    fun getCreatePostModel(editedPostId: Int?): CreatePostMviModel {
        val model: CreatePostMviModel by inject(
            parameters = { parametersOf(editedPostId) }
        )
        return model
    }

    fun getChatViewModel(otherUserId: Int): InboxChatMviModel {
        val model: InboxChatMviModel by inject(
            parameters = { parametersOf(otherUserId) }
        )
        return model
    }

    fun getCreateReportModel(
        postId: Int?,
        commentId: Int?,
    ): CreateReportMviModel {
        val model: CreateReportMviModel by inject(
            parameters = { parametersOf(postId, commentId) }
        )
        return model
    }
}

@Composable
actual fun getCustomTextToolbar(
    onShare: () -> Unit,
    onQuote: () -> Unit,
): TextToolbar {
    return LocalTextToolbar.current
}
