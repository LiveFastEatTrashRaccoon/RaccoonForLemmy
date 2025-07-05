package com.livefast.eattrash.raccoonforlemmy.unit.selectcommunity

import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommunityModel

interface SelectCommunityMviModel :
    MviModel<SelectCommunityMviModel.Intent, SelectCommunityMviModel.UiState, SelectCommunityMviModel.Effect> {
    sealed interface Intent {
        data class SetSearch(val value: String) : Intent

        data object LoadNextPage : Intent
    }

    data class UiState(
        val initial: Boolean = true,
        val communities: List<CommunityModel> = emptyList(),
        val searchText: String = "",
        val autoLoadImages: Boolean = true,
        val preferNicknames: Boolean = true,
        val loading: Boolean = false,
        val refreshing: Boolean = false,
        val canFetchMore: Boolean = true,
    )

    sealed interface Effect
}
