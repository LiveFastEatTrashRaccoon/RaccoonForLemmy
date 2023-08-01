package com.github.diegoberaldin.raccoonforlemmy.feature_home.viewmodel

import com.github.diegoberaldin.raccoonforlemmy.core_architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.domain_lemmy.data.ListingType
import com.github.diegoberaldin.raccoonforlemmy.domain_lemmy.data.PostModel
import com.github.diegoberaldin.raccoonforlemmy.domain_lemmy.data.SortType

interface HomeScreenMviModel :
    MviModel<HomeScreenMviModel.Intent, HomeScreenMviModel.UiState, HomeScreenMviModel.Effect> {

    sealed interface Intent {
        object Refresh : Intent
        object LoadNextPage : Intent
        data class ChangeSort(val value: SortType) : Intent
        data class ChangeListing(val value: ListingType) : Intent
    }

    data class UiState(
        val refreshing: Boolean = false,
        val loading: Boolean = false,
        val canFetchMore: Boolean = true,
        val instance: String = "",
        val isLogged: Boolean = false,
        val listingType: ListingType = ListingType.Local,
        val sortType: SortType = SortType.Active,
        val posts: List<PostModel> = emptyList(),
    )

    sealed interface Effect
}