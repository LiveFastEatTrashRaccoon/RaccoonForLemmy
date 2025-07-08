package com.livefast.eattrash.raccoonforlemmy.core.navigation

import kotlinx.serialization.Serializable

sealed interface Destination {
    @Serializable
    data object AccountSettings : Destination

    @Serializable
    data object Acknowledgements : Destination

    @Serializable
    data object AdvancedSettings : Destination

    @Serializable
    data class BanUser(
        val userId: Long,
        val communityId: Long,
        val newValue: Boolean,
        val postId: Long? = null,
        val commentId: Long? = null,
    ) : Destination

    @Serializable
    data class Chat(val otherUserId: Long) : Destination

    @Serializable
    data object ColorAndFont : Destination

    @Serializable
    data class CommunityDetail(val id: Long, val otherInstance: String = "") : Destination

    @Serializable
    data class CommunityInfo(val id: Long, val name: String = "", val otherInstance: String = "") : Destination

    @Serializable
    data object ConfigureContentView : Destination

    @Serializable
    data object ConfigureNavBar : Destination

    @Serializable
    data object ConfigureSwipeActions : Destination

    @Serializable
    data class CreateComment(
        val draftId: Long? = null,
        val originalPostId: Long? = null,
        val originalCommentId: Long? = null,
        val editedCommentId: Long? = null,
        val initialText: String? = null,
    ) : Destination

    @Serializable
    data class CreatePost(
        val draftId: Long? = null,
        val editedPostId: Long? = null,
        val crossPostId: Long? = null,
        val communityId: Long? = null,
        val initialText: String? = null,
        val initialTitle: String? = null,
        val initialUrl: String? = null,
        val initialNsfw: Boolean? = null,
        val forceCommunitySelection: Boolean = false,
    ) : Destination

    @Serializable
    data object Drafts : Destination

    @Serializable
    data class EditCommunity(val id: Long? = null) : Destination

    @Serializable
    data class Explore(val otherInstance: String) : Destination

    @Serializable
    data class FilteredContents(val type: Int) : Destination

    @Serializable
    data class InstanceInfo(val url: String) : Destination

    @Serializable
    data object Licences : Destination

    @Serializable
    data object Login : Destination

    @Serializable
    data object Main : Destination

    @Serializable
    data object ManageBans : Destination

    @Serializable
    data object ManageSubscriptions : Destination

    @Serializable
    data object MediaList : Destination

    @Serializable
    data class ModerateWithReason(val actionId: Int, val contentId: Long) : Destination

    @Serializable
    data class Modlog(val communityId: Long? = null) : Destination

    @Serializable
    data class MultiCommunity(val id: Long) : Destination

    @Serializable
    data class MultiCommunityEditor(val id: Long? = null) : Destination

    @Serializable
    data class PostDetail(
        val id: Long,
        val otherInstance: String = "",
        val highlightCommentId: Long? = null,
        val isMod: Boolean = false,
    ) : Destination

    @Serializable
    data class ReportList(val communityId: Long? = null) : Destination

    @Serializable
    data object Settings : Destination

    @Serializable
    data class UserDetail(val id: Long, val otherInstance: String = "") : Destination

    @Serializable
    data class UserTagDetail(val id: Long) : Destination

    @Serializable
    data object UserTags : Destination

    @Serializable
    data class WebInternal(val url: String) : Destination

    @Serializable
    data class ZoomableImage(val url: String, val source: String = "", val isVideo: Boolean = false) : Destination
}
