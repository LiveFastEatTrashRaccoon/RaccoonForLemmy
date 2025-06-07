package com.livefast.eattrash.raccoonforlemmy.unit.explore

import androidx.compose.runtime.Stable
import cafe.adriel.voyager.core.model.ScreenModel
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.PostLayout
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.VoteFormat
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.ActionOnSwipe
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.ListingType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SearchResult
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SearchResultType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SortType

@Stable
interface ExploreMviModel :
    MviModel<ExploreMviModel.Intent, ExploreMviModel.UiState, ExploreMviModel.Effect>,
    ScreenModel {
    sealed interface Intent {
        data object Refresh : Intent

        data object LoadNextPage : Intent

        data class SetSearch(val value: String) : Intent

        data object HapticIndication : Intent

        data class UpVotePost(val id: Long, val feedback: Boolean = false) : Intent

        data class DownVotePost(val id: Long, val feedback: Boolean = false) : Intent

        data class SavePost(val id: Long, val feedback: Boolean = false) : Intent

        data class UpVoteComment(val id: Long, val feedback: Boolean = false) : Intent

        data class DownVoteComment(val id: Long, val feedback: Boolean = false) : Intent

        data class SaveComment(val id: Long, val feedback: Boolean = false) : Intent

        data class ToggleSubscription(val communityId: Long) : Intent
    }

    data class UiState(
        val refreshing: Boolean = false,
        val loading: Boolean = false,
        val initial: Boolean = true,
        val canFetchMore: Boolean = true,
        val isLogged: Boolean = false,
        val swipeActionsEnabled: Boolean = false,
        val doubleTapActionEnabled: Boolean = false,
        val blurNsfw: Boolean = true,
        val instance: String = "",
        val searchText: String = "",
        val listingType: ListingType = ListingType.Local,
        val sortType: SortType = SortType.Active,
        val results: List<SearchResult> = emptyList(),
        val resultType: SearchResultType = SearchResultType.Communities,
        val postLayout: PostLayout = PostLayout.Card,
        val fullHeightImages: Boolean = true,
        val fullWidthImages: Boolean = false,
        val voteFormat: VoteFormat = VoteFormat.Aggregated,
        val autoLoadImages: Boolean = true,
        val preferNicknames: Boolean = true,
        val showScores: Boolean = true,
        val downVoteEnabled: Boolean = true,
        val currentUserId: Long? = null,
        val availableSortTypes: List<SortType> = emptyList(),
        val actionsOnSwipeToStartPosts: List<ActionOnSwipe> = emptyList(),
        val actionsOnSwipeToEndPosts: List<ActionOnSwipe> = emptyList(),
        val actionsOnSwipeToStartComments: List<ActionOnSwipe> = emptyList(),
        val actionsOnSwipeToEndComments: List<ActionOnSwipe> = emptyList(),
        val botTagColor: Int? = null,
        val meTagColor: Int? = null,
    )

    sealed interface Effect {
        data object BackToTop : Effect

        data object OperationFailure : Effect

        data object OpenSearch : Effect
    }
}
