package com.github.diegoberaldin.raccoonforlemmy.core.commonui.postdetail

import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommentModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.PostModel

interface PostDetailScreenMviModel :
    MviModel<PostDetailScreenMviModel.Intent, PostDetailScreenMviModel.UiState, PostDetailScreenMviModel.Effect> {

    sealed interface Intent {
        object Refresh : Intent
        object LoadNextPage : Intent
        data class UpVotePost(val value: Boolean, val post: PostModel) : Intent
        data class DownVotePost(val value: Boolean, val post: PostModel) : Intent
        data class SavePost(val value: Boolean, val post: PostModel) : Intent
        data class UpVoteComment(val value: Boolean, val comment: CommentModel) : Intent
        data class DownVoteComment(val value: Boolean, val comment: CommentModel) : Intent
        data class SaveComment(val value: Boolean, val comment: CommentModel) : Intent
    }

    data class UiState(
        val post: PostModel = PostModel(),
        val refreshing: Boolean = false,
        val loading: Boolean = false,
        val canFetchMore: Boolean = true,
        val comments: List<CommentModel> = emptyList(),
    )

    sealed interface Effect
}
