package com.livefast.eattrash.raccoonforlemmy.unit.communityinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.CommunityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.LemmyItemCache
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class CommunityInfoViewModel(
    private val communityId: Long,
    private val communityName: String,
    private val otherInstance: String,
    private val communityRepository: CommunityRepository,
    private val settingsRepository: SettingsRepository,
    private val itemCache: LemmyItemCache,
) : ViewModel(),
    MviModelDelegate<CommunityInfoMviModel.Intent, CommunityInfoMviModel.UiState, CommunityInfoMviModel.Effect>
    by DefaultMviModelDelegate(initialState = CommunityInfoMviModel.UiState()),
    CommunityInfoMviModel {
    init {
        viewModelScope.launch {
            if (uiState.value.community.id == 0L) {
                val community = itemCache.getCommunity(communityId) ?: CommunityModel()
                updateState { it.copy(community = community) }
            }
            settingsRepository.currentSettings
                .onEach {
                    updateState { it.copy(autoLoadImages = it.autoLoadImages) }
                }.launchIn(this)

            if (uiState.value.moderators.isEmpty()) {
                val community =
                    communityRepository.get(
                        id = communityId,
                        name = communityName,
                        instance = otherInstance,
                    )
                if (community != null) {
                    updateState { it.copy(community = community) }
                }
                val moderators =
                    communityRepository.getModerators(
                        id = communityId,
                    )
                updateState { it.copy(moderators = moderators) }
            }
        }
    }
}
