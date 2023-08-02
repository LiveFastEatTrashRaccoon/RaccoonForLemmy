package com.github.diegoberaldin.raccoonforlemmy.feature.search

import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel

interface SearchScreenMviModel :
    MviModel<SearchScreenMviModel.Intent, SearchScreenMviModel.UiState, SearchScreenMviModel.Effect> {
    sealed interface Intent

    data class UiState(val loading: Boolean = false)

    sealed interface Effect
}
