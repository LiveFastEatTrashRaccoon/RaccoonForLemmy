package com.livefast.eattrash.raccoonforlemmy.feature.profile.main

import cafe.adriel.voyager.core.model.screenModelScope
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModel
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.identity.usecase.LogoutUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ProfileMainViewModel(
    private val identityRepository: IdentityRepository,
    private val logout: LogoutUseCase,
) : DefaultMviModel<ProfileMainMviModel.Intent, ProfileMainMviModel.UiState, ProfileMainMviModel.Effect>(
        initialState = ProfileMainMviModel.UiState(),
    ),
    ProfileMainMviModel {
    init {
        screenModelScope.launch {
            identityRepository.isLogged
                .onEach { logged ->
                    updateState { it.copy(logged = logged) }
                    if (logged == true) {
                        val userFromCache = identityRepository.cachedUser
                        updateState {
                            it.copy(user = userFromCache)
                        }
                    }
                }.launchIn(this)
        }
    }

    override fun reduce(intent: ProfileMainMviModel.Intent) {
        when (intent) {
            ProfileMainMviModel.Intent.Logout -> handleLogout()
        }
    }

    private fun handleLogout() {
        screenModelScope.launch {
            logout()
        }
    }
}
