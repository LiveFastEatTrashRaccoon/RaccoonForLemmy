package com.github.diegoberaldin.raccoonforlemmy.feature.search.communitylist

import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommunityModel

interface CommunityListMviModel :
    MviModel<CommunityListMviModel.Intent, CommunityListMviModel.UiState, CommunityListMviModel.Effect> {
    sealed interface Intent {
        object Refresh : Intent
        object LoadNextPage : Intent
        object SearchFired : Intent
        data class SetSearch(val value: String) : Intent
        data class SetSubscribedOnly(val value: Boolean) : Intent
    }

    data class UiState(
        val refreshing: Boolean = false,
        val loading: Boolean = false,
        val canFetchMore: Boolean = true,
        val isLogged: Boolean = false,
        val instance: String = "",
        val searchText: String = "",
        val subscribedOnly: Boolean = true,
        val communities: List<CommunityModel> = emptyList(),
    )

    sealed interface Effect
}
