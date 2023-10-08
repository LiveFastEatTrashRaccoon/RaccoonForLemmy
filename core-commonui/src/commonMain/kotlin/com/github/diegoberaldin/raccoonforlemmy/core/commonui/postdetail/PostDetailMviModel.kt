package com.github.diegoberaldin.raccoonforlemmy.core.commonui.postdetail

import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.PostLayout
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommentModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.PostModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.SortType

interface PostDetailMviModel :
    MviModel<PostDetailMviModel.Intent, PostDetailMviModel.UiState, PostDetailMviModel.Effect> {

    sealed interface Intent {
        data object Refresh : Intent
        data object RefreshPost : Intent
        data object LoadNextPage : Intent
        data class FetchMoreComments(val parentId: Int) : Intent
        data class ChangeSort(val value: SortType) : Intent
        data class UpVotePost(val feedback: Boolean = false) : Intent
        data class DownVotePost(val feedback: Boolean = false) : Intent
        data class SavePost(val post: PostModel, val feedback: Boolean = false) : Intent
        data class UpVoteComment(val index: Int, val feedback: Boolean = false) : Intent
        data class DownVoteComment(val index: Int, val feedback: Boolean = false) :
            Intent

        data class SaveComment(val index: Int, val feedback: Boolean = false) : Intent
        data class DeleteComment(val id: Int) : Intent
        data object DeletePost : Intent
        data object HapticIndication : Intent
        data object SharePost : Intent
    }

    data class UiState(
        val post: PostModel = PostModel(),
        val refreshing: Boolean = false,
        val loading: Boolean = false,
        val initial: Boolean = true,
        val canFetchMore: Boolean = true,
        val sortType: SortType = SortType.New,
        val comments: List<CommentModel> = emptyList(),
        val currentUserId: Int? = null,
        val swipeActionsEnabled: Boolean = true,
        val postLayout: PostLayout = PostLayout.Card,
        val separateUpAndDownVotes: Boolean = false,
        val autoLoadImages: Boolean = true,
    )

    sealed interface Effect {
        data object Close : Effect
        data class ScrollToComment(val index: Int) : Effect
    }
}
