package com.livefast.eattrash.raccoonforlemmy.unit.myaccount

import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.PostLayout
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.VoteFormat
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModel
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.ProfileLoggedSection
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommentModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel

interface ProfileLoggedMviModel :
    MviModel<ProfileLoggedMviModel.Intent, ProfileLoggedMviModel.UiState, ProfileLoggedMviModel.Effect> {
    sealed interface Intent {
        data class ChangeSection(val section: ProfileLoggedSection) : Intent

        data object Refresh : Intent

        data object LoadNextPage : Intent

        data class DeletePost(val id: Long) : Intent

        data class DeleteComment(val id: Long) : Intent

        data class Share(val url: String) : Intent

        data class UpVotePost(val id: Long, val feedback: Boolean = false) : Intent

        data class DownVotePost(val id: Long, val feedback: Boolean = false) : Intent

        data class SavePost(val id: Long, val feedback: Boolean = false) : Intent

        data class UpVoteComment(val id: Long, val feedback: Boolean = false) : Intent

        data class DownVoteComment(val id: Long, val feedback: Boolean = false) : Intent

        data class SaveComment(val id: Long, val feedback: Boolean = false) : Intent

        data class WillOpenDetail(val postId: Long, val commentId: Long? = null) : Intent

        data class RestorePost(val id: Long) : Intent

        data class RestoreComment(val id: Long) : Intent
    }

    data class UiState(
        val user: UserModel? = null,
        val instance: String = "",
        val section: ProfileLoggedSection = ProfileLoggedSection.Posts,
        val refreshing: Boolean = false,
        val loading: Boolean = false,
        val initial: Boolean = true,
        val canFetchMore: Boolean = true,
        val posts: List<PostModel> = emptyList(),
        val comments: List<CommentModel> = emptyList(),
        val postLayout: PostLayout = PostLayout.Card,
        val fullHeightImages: Boolean = true,
        val fullWidthImages: Boolean = false,
        val voteFormat: VoteFormat = VoteFormat.Aggregated,
        val autoLoadImages: Boolean = true,
        val preferNicknames: Boolean = true,
        val showScores: Boolean = true,
        val showUnreadComments: Boolean = false,
        val downVoteEnabled: Boolean = true,
        val isModerator: Boolean = false,
    )

    sealed interface Effect {
        data class OpenDetail(val postId: Long, val commentId: Long? = null) : Effect
    }
}
