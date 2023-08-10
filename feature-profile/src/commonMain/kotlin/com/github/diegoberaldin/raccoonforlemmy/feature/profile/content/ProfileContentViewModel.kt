package com.github.diegoberaldin.raccoonforlemmy.feature.profile.content

import cafe.adriel.voyager.core.model.ScreenModel
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.SiteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ProfileContentViewModel(
    private val mvi: DefaultMviModel<ProfileContentMviModel.Intent, ProfileContentMviModel.UiState, ProfileContentMviModel.Effect>,
    private val identityRepository: IdentityRepository,
    private val siteRepository: SiteRepository,
) : ScreenModel,
    MviModel<ProfileContentMviModel.Intent, ProfileContentMviModel.UiState, ProfileContentMviModel.Effect> by mvi {

    override fun onStarted() {
        mvi.onStarted()

        identityRepository.authToken.onEach { token ->
            if (token == null) {
                mvi.updateState {
                    it.copy(
                        initial = false,
                        currentUser = null,
                    )
                }
            } else {
                updateUser(token)
            }
        }.launchIn(mvi.scope)
    }

    override fun reduce(intent: ProfileContentMviModel.Intent) {
        when (intent) {
            ProfileContentMviModel.Intent.Logout -> identityRepository.clearToken()
        }
    }

    private fun updateUser(token: String) {
        mvi.scope.launch(Dispatchers.IO) {
            val user = siteRepository.getCurrentUser(
                auth = token,
            )
            mvi.updateState {
                it.copy(
                    initial = false,
                    currentUser = user,
                )
            }
        }
    }
}
