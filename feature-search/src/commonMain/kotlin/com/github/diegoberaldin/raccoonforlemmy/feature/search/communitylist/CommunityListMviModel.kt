package com.github.diegoberaldin.raccoonforlemmy.feature.search.communitylist

import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.ListingType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.SortType

interface CommunityListMviModel :
    MviModel<CommunityListMviModel.Intent, CommunityListMviModel.UiState, CommunityListMviModel.Effect> {
    sealed interface Intent {
        object Refresh : Intent
        object LoadNextPage : Intent
        data class SetSearch(val value: String) : Intent
        data class SetListingType(val value: ListingType) : Intent
        data class SetSortType(val value: SortType) : Intent
    }

    data class UiState(
        val refreshing: Boolean = false,
        val loading: Boolean = false,
        val canFetchMore: Boolean = true,
        val isLogged: Boolean = false,
        val instance: String = "",
        val searchText: String = "",
        val listingType: ListingType = ListingType.Local,
        val sortType: SortType = SortType.Active,
        val communities: List<CommunityModel> = emptyList(),
    )

    sealed interface Effect
}
