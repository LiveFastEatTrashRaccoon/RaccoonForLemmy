package com.livefast.eattrash.raccoonforlemmy.unit.manageban

import androidx.compose.runtime.Stable
import cafe.adriel.voyager.core.model.ScreenModel
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.InstanceModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel

@Stable
interface ManageBanMviModel :
    MviModel<ManageBanMviModel.Intent, ManageBanMviModel.UiState, ManageBanMviModel.Effect>,
    ScreenModel {
    sealed interface Intent {
        data class ChangeSection(val section: ManageBanSection) : Intent

        data object Refresh : Intent

        data class UnblockUser(val id: Long) : Intent

        data class UnblockCommunity(val id: Long) : Intent

        data class UnblockInstance(val id: Long) : Intent

        data class SetSearch(val value: String) : Intent

        data class BlockDomain(val value: String) : Intent

        data class UnblockDomain(val value: String) : Intent

        data class AddStopWord(val value: String) : Intent

        data class RemoveStopWord(val value: String) : Intent
    }

    data class UiState(
        val section: ManageBanSection = ManageBanSection.Users,
        val refreshing: Boolean = false,
        val initial: Boolean = true,
        val autoLoadImages: Boolean = true,
        val preferNicknames: Boolean = true,
        val bannedUsers: List<UserModel> = emptyList(),
        val bannedCommunities: List<CommunityModel> = emptyList(),
        val bannedInstances: List<InstanceModel> = emptyList(),
        val blockedDomains: List<String> = emptyList(),
        val stopWords: List<String> = emptyList(),
        val searchText: String = "",
    )

    sealed interface Effect {
        data object BackToTop : Effect

        data object Success : Effect

        data class Failure(val message: String?) : Effect
    }
}
