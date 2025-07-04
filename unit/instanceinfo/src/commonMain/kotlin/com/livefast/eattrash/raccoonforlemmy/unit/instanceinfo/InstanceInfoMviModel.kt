package com.livefast.eattrash.raccoonforlemmy.unit.instanceinfo

import androidx.compose.runtime.Stable
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SortType

@Stable
interface InstanceInfoMviModel :
    MviModel<InstanceInfoMviModel.Intent, InstanceInfoMviModel.UiState, InstanceInfoMviModel.Effect> {
    sealed interface Intent {
        data object Refresh : Intent

        data object LoadNextPage : Intent
    }

    data class UiState(
        val title: String = "",
        val description: String = "",
        val canFetchMore: Boolean = true,
        val refreshing: Boolean = false,
        val initial: Boolean = true,
        val autoLoadImages: Boolean = true,
        val preferNicknames: Boolean = true,
        val loading: Boolean = false,
        val sortType: SortType = SortType.Active,
        val communities: List<CommunityModel> = emptyList(),
        val availableSortTypes: List<SortType> = emptyList(),
    )

    sealed interface Effect {
        data object BackToTop : Effect
    }
}
