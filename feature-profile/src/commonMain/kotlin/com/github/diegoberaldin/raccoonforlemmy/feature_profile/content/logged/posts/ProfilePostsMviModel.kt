package com.github.diegoberaldin.raccoonforlemmy.feature_profile.content.logged.posts

import com.github.diegoberaldin.raccoonforlemmy.core_architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.domain_lemmy.data.PostModel

interface ProfilePostsMviModel :
    MviModel<ProfilePostsMviModel.Intent, ProfilePostsMviModel.UiState, ProfilePostsMviModel.Effect> {

    sealed interface Intent {
        object Refresh : Intent
        object LoadNextPage : Intent
    }

    data class UiState(
        val refreshing: Boolean = false,
        val loading: Boolean = false,
        val canFetchMore: Boolean = true,
        val posts: List<PostModel> = emptyList(),
    )

    sealed interface Effect
}
