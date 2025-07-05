package com.livefast.eattrash.raccoonforlemmy.unit.drawer.content

import androidx.compose.runtime.Stable
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.MultiCommunityModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel

@Stable
interface ModalDrawerMviModel :
    MviModel<ModalDrawerMviModel.Intent, ModalDrawerMviModel.UiState, ModalDrawerMviModel.Effect> {
    sealed interface Intent {
        data object Refresh : Intent

        data class SetSearch(val value: String) : Intent

        data class ToggleFavorite(val id: Long) : Intent
    }

    data class UiState(
        val loading: Boolean = false,
        val user: UserModel? = null,
        val autoLoadImages: Boolean = true,
        val preferNicknames: Boolean = true,
        val refreshing: Boolean = false,
        val instance: String? = null,
        val communities: List<CommunityModel> = emptyList(),
        val multiCommunities: List<MultiCommunityModel> = emptyList(),
        val favorites: List<CommunityModel> = emptyList(),
        val searchText: String = "",
        val isFiltering: Boolean = false,
        val enableToggleFavorite: Boolean = false,
        val isSettingsVisible: Boolean = true,
    )

    sealed interface Effect
}
