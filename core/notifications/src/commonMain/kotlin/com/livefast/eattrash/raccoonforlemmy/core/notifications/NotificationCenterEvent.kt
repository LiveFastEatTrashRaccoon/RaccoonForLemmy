package com.livefast.eattrash.raccoonforlemmy.core.notifications

import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.MultiCommunityModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommentModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.ListingType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel

sealed interface NotificationCenterEvent {
    data class ChangeFeedType(val value: ListingType) : NotificationCenterEvent

    data object Logout : NotificationCenterEvent

    data object PostCreated : NotificationCenterEvent

    data object CommentCreated : NotificationCenterEvent

    data class PostUpdated(val model: PostModel) : NotificationCenterEvent

    data class CommentUpdated(val model: CommentModel) : NotificationCenterEvent

    data class MultiCommunityCreated(val model: MultiCommunityModel) : NotificationCenterEvent

    data class PostRemoved(val model: PostModel) : NotificationCenterEvent

    data class UserBannedPost(val postId: Long, val user: UserModel) : NotificationCenterEvent

    data class UserBannedComment(val commentId: Long, val user: UserModel) : NotificationCenterEvent

    data class InstanceSelected(val value: String) : NotificationCenterEvent

    data object DraftDeleted : NotificationCenterEvent

    data object ResetHome : NotificationCenterEvent

    data object ResetExplore : NotificationCenterEvent

    data object ResetInbox : NotificationCenterEvent

    data class CommunitySubscriptionChanged(val value: CommunityModel) : NotificationCenterEvent

    data object FavoritesUpdated : NotificationCenterEvent

    data object OpenSearchInExplore : NotificationCenterEvent
}
