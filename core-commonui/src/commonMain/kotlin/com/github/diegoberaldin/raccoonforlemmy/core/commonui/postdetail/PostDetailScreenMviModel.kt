package com.github.diegoberaldin.raccoonforlemmy.core.commonui.postdetail

import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommentModel

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
