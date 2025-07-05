package com.livefast.eattrash.raccoonforlemmy.unit.userinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModelDelegate
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
) : ViewModel(),
    MviModelDelegate<UserInfoMviModel.Intent, UserInfoMviModel.UiState, UserInfoMviModel.Effect>
    by DefaultMviModelDelegate(initialState = UserInfoMviModel.UiState()),
    UserInfoMviModel {
    init {
        viewModelScope.launch {
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
