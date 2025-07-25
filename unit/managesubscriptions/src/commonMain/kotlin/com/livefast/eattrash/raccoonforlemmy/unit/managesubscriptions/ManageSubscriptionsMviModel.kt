package com.livefast.eattrash.raccoonforlemmy.unit.managesubscriptions

import androidx.compose.runtime.Stable
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.MultiCommunityModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.livefast.eattrash.raccoonforlemmy.unit.managesubscriptions.ManageSubscriptionsMviModel.Effect
import com.livefast.eattrash.raccoonforlemmy.unit.managesubscriptions.ManageSubscriptionsMviModel.Intent
import com.livefast.eattrash.raccoonforlemmy.unit.managesubscriptions.ManageSubscriptionsMviModel.UiState

@Stable
interface ManageSubscriptionsMviModel : MviModel<Intent, UiState, Effect> {
    sealed interface Intent {
        data object Refresh : Intent

        data object HapticIndication : Intent

        data class Unsubscribe(val id: Long) : Intent

        data class DeleteMultiCommunity(val id: Long) : Intent

        data class ToggleFavorite(val id: Long) : Intent

        data class SetSearch(val value: String) : Intent

        data object LoadNextPage : Intent
    }

    data class UiState(
        val initial: Boolean = true,
        val refreshing: Boolean = false,
        val multiCommunities: List<MultiCommunityModel> = emptyList(),
        val communities: List<CommunityModel> = emptyList(),
        val autoLoadImages: Boolean = true,
        val preferNicknames: Boolean = true,
        val searchText: String = "",
        val canFetchMore: Boolean = true,
        val loading: Boolean = false,
    )

    sealed interface Effect {
        data object BackToTop : Effect

        data object Success : Effect
    }
}
