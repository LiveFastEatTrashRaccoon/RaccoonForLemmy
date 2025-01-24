package com.livefast.eattrash.raccoonforlemmy.unit.postlist

import androidx.compose.runtime.Stable
import cafe.adriel.voyager.core.model.ScreenModel
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.PostLayout
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.VoteFormat
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.ActionOnSwipe
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.ListingType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SortType

@Stable
interface PostListMviModel :
    MviModel<PostListMviModel.Intent, PostListMviModel.UiState, PostListMviModel.Effect>,
    ScreenModel {
    sealed interface Intent {
        data class Refresh(
            val hardReset: Boolean = false,
        ) : Intent

        data object LoadNextPage : Intent

        data class ChangeListing(
            val value: ListingType,
        ) : Intent

        data class UpVotePost(
            val id: Long,
            val feedback: Boolean = false,
        ) : Intent

        data class DownVotePost(
            val id: Long,
            val feedback: Boolean = false,
        ) : Intent

        data class SavePost(
            val id: Long,
            val feedback: Boolean = false,
        ) : Intent

        data object HapticIndication : Intent

        data class DeletePost(
            val id: Long,
        ) : Intent

        data class Share(
            val url: String,
        ) : Intent

        data class MarkAsRead(
            val id: Long,
        ) : Intent

        data class Hide(
            val id: Long,
        ) : Intent

        data object ClearRead : Intent

        data class StartZombieMode(
            val index: Int,
        ) : Intent

        data object PauseZombieMode : Intent

        data class WillOpenDetail(
            val id: Long,
        ) : Intent

        data class ToggleRead(
            val id: Long,
        ) : Intent
    }

    data class UiState(
        val initial: Boolean = true,
        val refreshing: Boolean = false,
        val loading: Boolean = false,
        val canFetchMore: Boolean = true,
        val instance: String = "",
        val isLogged: Boolean = false,
        val listingType: ListingType? = null,
        val sortType: SortType? = null,
        val posts: List<PostModel> = emptyList(),
        val blurNsfw: Boolean = true,
        val currentUserId: Long? = null,
        val swipeActionsEnabled: Boolean = true,
        val doubleTapActionEnabled: Boolean = false,
        val postLayout: PostLayout = PostLayout.Card,
        val fullHeightImages: Boolean = true,
        val fullWidthImages: Boolean = false,
        val voteFormat: VoteFormat = VoteFormat.Aggregated,
        val autoLoadImages: Boolean = true,
        val preferNicknames: Boolean = true,
        val showScores: Boolean = true,
        val zombieModeActive: Boolean = false,
        val availableSortTypes: List<SortType> = emptyList(),
        val actionsOnSwipeToStartPosts: List<ActionOnSwipe> = emptyList(),
        val actionsOnSwipeToEndPosts: List<ActionOnSwipe> = emptyList(),
        val fadeReadPosts: Boolean = false,
        val showUnreadComments: Boolean = false,
        val downVoteEnabled: Boolean = true,
        val botTagColor: Int? = null,
        val meTagColor: Int? = null,
    )

    sealed interface Effect {
        data object BackToTop : Effect

        data class ZombieModeTick(
            val index: Int,
        ) : Effect

        data class OpenDetail(
            val post: PostModel,
        ) : Effect
    }
}
