package com.livefast.eattrash.raccoonforlemmy.feature.profile.notlogged

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ProfileNotLoggedViewModel(private val identityRepository: IdentityRepository) :
    ViewModel(),
    MviModelDelegate<ProfileNotLoggedMviModel.Intent, ProfileNotLoggedMviModel.State, ProfileNotLoggedMviModel.Effect>
    by DefaultMviModelDelegate(initialState = ProfileNotLoggedMviModel.State()),
    ProfileNotLoggedMviModel {
    init {
        viewModelScope.launch {
            identityRepository.isLogged
                .onEach { logged ->
                    val auth = identityRepository.authToken.value
                    updateState { it.copy(authError = !auth.isNullOrEmpty() && logged == false) }
                }.launchIn(this)
        }
    }

    override fun reduce(intent: ProfileNotLoggedMviModel.Intent) {
        when (intent) {
            ProfileNotLoggedMviModel.Intent.Retry ->
                viewModelScope.launch {
                    identityRepository.refreshLoggedState()
                }
        }
    }
}
