package com.github.diegoberaldin.raccoonforlemmy.core.commonui.userdetail.comments

import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommentModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.SortType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.UserModel

interface UserCommentsMviModel :
    MviModel<UserCommentsMviModel.Intent, UserCommentsMviModel.UiState, UserCommentsMviModel.Effect> {

    sealed interface Intent {
        object Refresh : Intent
        object LoadNextPage : Intent
        data class ChangeSort(val value: SortType) : Intent
        data class UpVoteComment(val comment: CommentModel, val feedback: Boolean = false) : Intent
        data class DownVoteComment(val comment: CommentModel, val feedback: Boolean = false) :
            Intent

        data class SaveComment(val comment: CommentModel, val feedback: Boolean = false) : Intent
        object HapticIndication : Intent
    }

    data class UiState(
        val refreshing: Boolean = false,
        val loading: Boolean = false,
        val canFetchMore: Boolean = true,
        val comments: List<CommentModel> = emptyList(),
        val user: UserModel = UserModel(),
        val sortType: SortType = SortType.Active,
    )

    sealed interface Effect
}
