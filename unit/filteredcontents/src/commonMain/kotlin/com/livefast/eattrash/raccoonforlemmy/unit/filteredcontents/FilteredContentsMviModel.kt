package com.livefast.eattrash.raccoonforlemmy.unit.filteredcontents

import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.PostLayout
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.VoteFormat
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.ActionOnSwipe
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommentModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel

sealed interface FilteredContentsType {
    data object Votes : FilteredContentsType

    data object Moderated : FilteredContentsType

    data object Bookmarks : FilteredContentsType

    data object Hidden : FilteredContentsType
}

fun FilteredContentsType.toInt(): Int = when (this) {
    FilteredContentsType.Moderated -> 0
    FilteredContentsType.Votes -> 1
    FilteredContentsType.Bookmarks -> 2
    FilteredContentsType.Hidden -> 3
}

fun Int.toFilteredContentsType(): FilteredContentsType = when (this) {
    3 -> FilteredContentsType.Hidden
    2 -> FilteredContentsType.Bookmarks
    1 -> FilteredContentsType.Votes
    else -> FilteredContentsType.Moderated
}

sealed interface FilteredContentsSection {
    data object Posts : FilteredContentsSection

    data object Comments : FilteredContentsSection
}

interface FilteredContentsMviModel :
    MviModel<FilteredContentsMviModel.Intent, FilteredContentsMviModel.State, FilteredContentsMviModel.Effect> {
    sealed interface Intent {
        data class ChangeSection(val value: FilteredContentsSection) : Intent

        data object Refresh : Intent

        data object LoadNextPage : Intent

        data class UpVotePost(val id: Long, val feedback: Boolean = false) : Intent

        data class DownVotePost(val id: Long, val feedback: Boolean = false) : Intent

        data class SavePost(val id: Long, val feedback: Boolean = false) : Intent

        data class UpVoteComment(val commentId: Long, val feedback: Boolean = false) : Intent

        data class DownVoteComment(val commentId: Long, val feedback: Boolean = false) : Intent

        data class SaveComment(val commentId: Long, val feedback: Boolean = false) : Intent

        data object HapticIndication : Intent

        data class ModFeaturePost(val id: Long) : Intent

        data class AdminFeaturePost(val id: Long) : Intent

        data class ModLockPost(val id: Long) : Intent

        data class ModDistinguishComment(val commentId: Long) : Intent

        data class WillOpenDetail(val postId: Long, val commentId: Long? = null) : Intent

        data class ChangedLikedType(val value: Boolean) : Intent
    }

    data class State(
        val contentsType: FilteredContentsType = FilteredContentsType.Votes,
        val liked: Boolean = true,
        val initial: Boolean = true,
        val isAdmin: Boolean = false,
        val loading: Boolean = false,
        val refreshing: Boolean = true,
        val canFetchMore: Boolean = true,
        val autoLoadImages: Boolean = true,
        val preferNicknames: Boolean = true,
        val swipeActionsEnabled: Boolean = true,
        val postLayout: PostLayout = PostLayout.Card,
        val fullHeightImages: Boolean = true,
        val fullWidthImages: Boolean = false,
        val voteFormat: VoteFormat = VoteFormat.Aggregated,
        val section: FilteredContentsSection = FilteredContentsSection.Posts,
        val posts: List<PostModel> = emptyList(),
        val comments: List<CommentModel> = emptyList(),
        val fadeReadPosts: Boolean = false,
        val showUnreadComments: Boolean = false,
        val downVoteEnabled: Boolean = true,
        val isPostOnly: Boolean = false,
        val currentUserId: Long? = null,
        val actionsOnSwipeToStartPosts: List<ActionOnSwipe> = emptyList(),
        val actionsOnSwipeToEndPosts: List<ActionOnSwipe> = emptyList(),
        val actionsOnSwipeToStartComments: List<ActionOnSwipe> = emptyList(),
        val actionsOnSwipeToEndComments: List<ActionOnSwipe> = emptyList(),
        val botTagColor: Int? = null,
        val meTagColor: Int? = null,
    )

    sealed interface Effect {
        data object BackToTop : Effect

        data class OpenDetail(val postId: Long, val commentId: Long? = null) : Effect
    }
}
