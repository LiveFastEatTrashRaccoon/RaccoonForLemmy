package com.github.diegoberaldin.raccoonforlemmy.feature_profile.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import com.github.diegoberaldin.raccoonforlemmy.core_architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.core_architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.domain_identity.repository.IdentityRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ProfileScreenModel(
    private val mvi: DefaultMviModel<ProfileScreenMviModel.Intent, ProfileScreenMviModel.UiState, ProfileScreenMviModel.Effect>,
    private val identityRepository: IdentityRepository,
) : ScreenModel,
    MviModel<ProfileScreenMviModel.Intent, ProfileScreenMviModel.UiState, ProfileScreenMviModel.Effect> by mvi {

    override fun onStarted() {
        mvi.onStarted()
        identityRepository.authToken.onEach {
            val isLogged = !it.isNullOrEmpty()
            mvi.updateState { it.copy(isLogged = isLogged) }
        }.launchIn(mvi.scope)
    }
}
