package com.github.diegoberaldin.raccoonforlemmy.feature_home

import com.github.diegoberaldin.raccoonforlemmy.core_architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.data.PostModel

interface HomeScreenMviModel :
    MviModel<HomeScreenMviModel.Intent, HomeScreenMviModel.UiState, HomeScreenMviModel.Effect> {

    sealed interface Intent

    data class UiState(
        val loading: Boolean = false,
        val posts: List<PostModel> = emptyList(),
    )

    sealed interface Effect
}