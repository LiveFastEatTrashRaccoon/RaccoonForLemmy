package com.livefast.eattrash.raccoonforlemmy.unit.communitydetail

import androidx.compose.runtime.Stable
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.PostLayout
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.VoteFormat
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.ActionOnSwipe
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.LanguageModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SortType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel

sealed interface CommunityNotices {
    data object LocalOnlyVisibility : CommunityNotices

    data object BannedUser : CommunityNotices
}

@Stable
interface CommunityDetailMviModel :
    MviModel<CommunityDetailMviModel.Intent, CommunityDetailMviModel.UiState, CommunityDetailMviModel.Effect> {
    sealed interface Intent {
        data object Refresh : Intent

        data object LoadNextPage : Intent

        data class UpVotePost(val id: Long, val feedback: Boolean = false) : Intent

        data class DownVotePost(val id: Long, val feedback: Boolean = false) : Intent

        data class SavePost(val id: Long, val feedback: Boolean = false) : Intent

        data object HapticIndication : Intent

        data object Subscribe : Intent

        data object Unsubscribe : Intent

        data class DeletePost(val id: Long) : Intent

        data class MarkAsRead(val id: Long) : Intent

        data class Hide(val id: Long) : Intent

        data object Block : Intent

        data object BlockInstance : Intent

        data object ClearRead : Intent

        data class StartZombieMode(val index: Int) : Intent

        data object PauseZombieMode : Intent

        data class ModFeaturePost(val id: Long) : Intent

        data class AdminFeaturePost(val id: Long) : Intent

        data class ModLockPost(val id: Long) : Intent

        data class ModToggleModUser(val id: Long) : Intent

        data object ToggleFavorite : Intent

        data class Share(val url: String) : Intent

        data class SetSearch(val value: String) : Intent

        data class ChangeSearching(val value: Boolean) : Intent

        data class WillOpenDetail(val id: Long) : Intent

        data object UnhideCommunity : Intent

        data class SelectPreferredLanguage(val languageId: Long?) : Intent

        data object DeleteCommunity : Intent

        data class RestorePost(val id: Long) : Intent

        data class ToggleRead(val id: Long) : Intent
    }

    data class UiState(
        val community: CommunityModel = CommunityModel(),
        val instance: String = "",
        val isLogged: Boolean = false,
        val refreshing: Boolean = false,
        val initial: Boolean = true,
        val asyncInProgress: Boolean = false,
        val loading: Boolean = false,
        val canFetchMore: Boolean = true,
        val sortType: SortType = SortType.Active,
        val posts: List<PostModel> = emptyList(),
        val blurNsfw: Boolean = true,
        val currentUserId: Long? = null,
        val isAdmin: Boolean = true,
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
        val moderators: List<UserModel> = emptyList(),
        val availableSortTypes: List<SortType> = emptyList(),
        val actionsOnSwipeToStartPosts: List<ActionOnSwipe> = emptyList(),
        val actionsOnSwipeToEndPosts: List<ActionOnSwipe> = emptyList(),
        val searching: Boolean = false,
        val searchText: String = "",
        val fadeReadPosts: Boolean = false,
        val showUnreadComments: Boolean = false,
        val downVoteEnabled: Boolean = true,
        val currentPreferredLanguageId: Long? = null,
        val availableLanguages: List<LanguageModel> = emptyList(),
        val notices: List<CommunityNotices> = emptyList(),
        val botTagColor: Int? = null,
        val meTagColor: Int? = null,
    )

    sealed interface Effect {
        data object Success : Effect

        data class Failure(val message: String?) : Effect

        data class Error(val message: String?) : Effect

        data object BackToTop : Effect

        data class ZombieModeTick(val index: Int) : Effect

        data object Back : Effect

        data class OpenDetail(val post: PostModel) : Effect
    }
}
