package com.livefast.eattrash.raccoonforlemmy.feature.profile.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.identity.usecase.LogoutUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ProfileMainViewModel(private val identityRepository: IdentityRepository, private val logout: LogoutUseCase) :
    ViewModel(),
    MviModelDelegate<ProfileMainMviModel.Intent, ProfileMainMviModel.UiState, ProfileMainMviModel.Effect>
    by DefaultMviModelDelegate(
        initialState = ProfileMainMviModel.UiState(),
    ),
    ProfileMainMviModel {
    init {
        viewModelScope.launch {
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
        viewModelScope.launch {
            logout()
        }
    }
}
