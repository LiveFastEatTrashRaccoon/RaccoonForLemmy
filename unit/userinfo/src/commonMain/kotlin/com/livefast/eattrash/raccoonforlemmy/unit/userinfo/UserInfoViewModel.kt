package com.livefast.eattrash.raccoonforlemmy.unit.userinfo

import cafe.adriel.voyager.core.model.screenModelScope
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.LemmyItemCache
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.SiteRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.UserRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class UserInfoViewModel(
    private val userId: Long,
    private val username: String,
    private val otherInstance: String,
    private val userRepository: UserRepository,
    private val settingsRepository: SettingsRepository,
    private val itemCache: LemmyItemCache,
    private val siteRepository: SiteRepository,
) : DefaultMviModel<UserInfoMviModel.Intent, UserInfoMviModel.UiState, UserInfoMviModel.Effect>(
    initialState = UserInfoMviModel.UiState(),
),
    UserInfoMviModel {
    init {
        screenModelScope.launch {
            val user = itemCache.getUser(userId) ?: UserModel()
            updateState {
                it.copy(user = user)
            }
            settingsRepository.currentSettings
                .onEach {
                    updateState { it.copy(autoLoadImages = it.autoLoadImages) }
                }.launchIn(this)

            if (uiState.value.moderatedCommunities.isEmpty()) {
                val updatedUser =
                    userRepository.get(
                        id = user.id,
                        username = username,
                        otherInstance = otherInstance,
                    )
                if (updatedUser != null) {
                    updateState {
                        it.copy(user = updatedUser)
                    }
                }
                val communities = userRepository.getModeratedCommunities(id = user.id)
                val admins = siteRepository.getAdmins(otherInstance = otherInstance)
                updateState {
                    it.copy(
                        moderatedCommunities = communities,
                        isAdmin = admins.any { a -> a.id == user.id },
                    )
                }
            }
        }
    }
}
