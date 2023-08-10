package com.github.diegoberaldin.raccoonforlemmy.core.commonui.userdetail.posts

import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.PostModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.UserModel

interface UserPostsMviModel :
    MviModel<UserPostsMviModel.Intent, UserPostsMviModel.UiState, UserPostsMviModel.Effect> {

    sealed interface Intent {
        object Refresh : Intent
        object LoadNextPage : Intent
        data class UpVotePost(val post: PostModel, val feedback: Boolean = false) : Intent
        data class DownVotePost(val post: PostModel, val feedback: Boolean = false) : Intent
        data class SavePost(val post: PostModel, val feedback: Boolean = false) : Intent
        object HapticIndication : Intent
    }

    data class UiState(
        val refreshing: Boolean = false,
        val loading: Boolean = false,
        val canFetchMore: Boolean = true,
        val posts: List<PostModel> = emptyList(),
        val user: UserModel = UserModel(),
    )

    sealed interface Effect
}
