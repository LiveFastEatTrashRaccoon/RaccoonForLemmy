package com.github.diegoberaldin.raccoonforlemmy.core_commonui.postdetail

import com.github.diegoberaldin.raccoonforlemmy.core_architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.domain_lemmy.data.CommentModel

interface PostDetailScreenMviModel :
    MviModel<PostDetailScreenMviModel.Intent, PostDetailScreenMviModel.UiState, PostDetailScreenMviModel.Effect> {

    sealed interface Intent {
        object Refresh : Intent
        object LoadNextPage : Intent
    }

    data class UiState(
        val refreshing: Boolean = false,
        val loading: Boolean = false,
        val canFetchMore: Boolean = true,
        val comments: List<CommentModel> = emptyList(),
    )

    sealed interface Effect
}
