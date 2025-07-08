package com.livefast.eattrash.raccoonforlemmy.core.navigation

import androidx.compose.runtime.Stable
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommentModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel

@Stable
interface MainRouter {
    fun openAccountSettings()

    fun openAcknowledgements()

    fun openAdvancedSettings()

    fun openBanUser(userId: Long, communityId: Long, newValue: Boolean, postId: Long? = null, commentId: Long? = null)

    fun openBookmarks()

    fun openChat(otherUserId: Long)

    fun openColorAndFont()

    fun openCommunityDetail(community: CommunityModel, otherInstance: String = "")

    fun openConfigureContentView()

    fun openConfigureNavBar()

    fun openConfigureSwipeActions()

    fun openCreatePost(
        draftId: Long? = null,
        editedPost: PostModel? = null,
        crossPost: PostModel? = null,
        communityId: Long? = null,
        initialText: String? = null,
        initialTitle: String? = null,
        initialUrl: String? = null,
        initialNsfw: Boolean? = null,
        forceCommunitySelection: Boolean = false,
    )

    fun openDrafts()

    fun openEditMultiCommunity(id: Long? = null)

    fun openEditCommunity(id: Long? = null)

    fun openExplore(otherInstance: String)

    fun openModlog(communityId: Long? = null)

    fun openReports(communityId: Long? = null)

    fun openHidden()

    fun openImage(url: String, source: String = "", isVideo: Boolean = false)

    fun openInstanceInfo(url: String)

    fun openLicences()

    fun openLogin()

    fun openManageBans()

    fun openManageSubscriptions()

    fun openMediaList()

    fun openModerateWithReason(actionId: Int, contentId: Long)

    fun openModeratedContents()

    fun openMultiCommunity(id: Long)

    fun openPostDetail(
        post: PostModel,
        otherInstance: String = "",
        highlightCommentId: Long? = null,
        isMod: Boolean = false,
    )

    fun openReply(
        draftId: Long? = null,
        originalPost: PostModel,
        originalComment: CommentModel? = null,
        editedComment: CommentModel? = null,
        initialText: String? = null,
    )

    fun openSettings()

    fun openUserDetail(user: UserModel, otherInstance: String = "")

    fun openUserTagDetail(id: Long)

    fun openUserTags()

    fun openVotes()

    fun openWebInternal(url: String)
}
