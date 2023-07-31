package com.github.diegoberaldin.raccoonforlemmy.feature_profile.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import com.github.diegoberaldin.raccoonforlemmy.core_architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.core_architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.domain_identity.repository.IdentityRepository
import com.github.diegoberaldin.raccoonforlemmy.domain_post.repository.SiteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ProfileScreenModel(
    private val mvi: DefaultMviModel<ProfileScreenMviModel.Intent, ProfileScreenMviModel.UiState, ProfileScreenMviModel.Effect>,
    private val identityRepository: IdentityRepository,
    private val siteRepository: SiteRepository,
) : ScreenModel,
    MviModel<ProfileScreenMviModel.Intent, ProfileScreenMviModel.UiState, ProfileScreenMviModel.Effect> by mvi {

    override fun onStarted() {
        mvi.onStarted()
        identityRepository.authToken.onEach { token ->
            if (token == null) {
                mvi.updateState { it.copy(currentUser = null) }
            } else {
                updateUser(token)
            }
        }.launchIn(mvi.scope)
    }

    override fun reduce(intent: ProfileScreenMviModel.Intent) {
        when (intent) {
            ProfileScreenMviModel.Intent.Logout -> identityRepository.clearToken()
        }
    }

    private fun updateUser(token: String) {
        mvi.scope.launch(Dispatchers.IO) {
            val user = siteRepository.getCurrentUser(
                auth = token
            )
            mvi.updateState {
                it.copy(
                    currentUser = user,
                )
            }
        }
    }
}
