package com.livefast.eattrash.raccoonforlemmy.unit.userdetail

import androidx.compose.runtime.Stable
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.PostLayout
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.VoteFormat
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModel
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.UserDetailSection
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.ActionOnSwipe
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.UserTagModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommentModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SortType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel

@Stable
interface UserDetailMviModel :
    MviModel<UserDetailMviModel.Intent, UserDetailMviModel.UiState, UserDetailMviModel.Effect> {
    sealed interface Intent {
        data object Refresh : Intent

        data object LoadNextPage : Intent

        data class ChangeSection(val section: UserDetailSection) : Intent

        data class UpVotePost(val id: Long, val feedback: Boolean = false) : Intent

        data class DownVotePost(val id: Long, val feedback: Boolean = false) : Intent

        data class SavePost(val id: Long, val feedback: Boolean = false) : Intent

        data class UpVoteComment(val id: Long, val feedback: Boolean = false) : Intent

        data class DownVoteComment(val id: Long, val feedback: Boolean = false) : Intent

        data class SaveComment(val id: Long, val feedback: Boolean = false) : Intent

        data object HapticIndication : Intent

        data class Share(val url: String) : Intent

        data object Block : Intent

        data object BlockInstance : Intent

        data class WillOpenDetail(val postId: Long, val commentId: Long? = null) : Intent

        data class UpdateTags(val ids: List<Long>) : Intent

        data class AddUserTag(val name: String, val color: Int? = null) : Intent
    }

    data class UiState(
        val isLogged: Boolean = false,
        val instance: String = "",
        val currentUserId: Long? = null,
        val isAdmin: Boolean = false,
        val section: UserDetailSection = UserDetailSection.Posts,
        val postSortType: SortType = SortType.Active,
        val commentSortType: SortType = SortType.Active,
        val refreshing: Boolean = false,
        val loading: Boolean = false,
        val initial: Boolean = true,
        val asyncInProgress: Boolean = false,
        val canFetchMore: Boolean = true,
        val posts: List<PostModel> = emptyList(),
        val comments: List<CommentModel> = emptyList(),
        val user: UserModel = UserModel(),
        val blurNsfw: Boolean = true,
        val swipeActionsEnabled: Boolean = true,
        val doubleTapActionEnabled: Boolean = true,
        val postLayout: PostLayout = PostLayout.Card,
        val fullHeightImages: Boolean = true,
        val fullWidthImages: Boolean = false,
        val voteFormat: VoteFormat = VoteFormat.Aggregated,
        val autoLoadImages: Boolean = true,
        val preferNicknames: Boolean = true,
        val showScores: Boolean = true,
        val downVoteEnabled: Boolean = true,
        val availableSortTypes: List<SortType> = emptyList(),
        val actionsOnSwipeToStartPosts: List<ActionOnSwipe> = emptyList(),
        val actionsOnSwipeToEndPosts: List<ActionOnSwipe> = emptyList(),
        val actionsOnSwipeToStartComments: List<ActionOnSwipe> = emptyList(),
        val actionsOnSwipeToEndComments: List<ActionOnSwipe> = emptyList(),
        val currentUserTagIds: List<Long> = emptyList(),
        val availableUserTags: List<UserTagModel> = emptyList(),
    )

    sealed interface Effect {
        data object Success : Effect

        data class Error(val message: String?) : Effect

        data object BackToTop : Effect

        data class OpenDetail(val postId: Long, val commentId: Long? = null) : Effect
    }
}
