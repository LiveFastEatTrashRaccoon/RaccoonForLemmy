package com.github.diegoberaldin.raccoonforlemmy.feature.inbox.main

import cafe.adriel.voyager.core.model.ScreenModel
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

class InboxViewModel(
    private val mvi: DefaultMviModel<InboxMviModel.Intent, InboxMviModel.UiState, InboxMviModel.Effect>,
    private val identityRepository: IdentityRepository,
    private val userRepository: UserRepository,
) : ScreenModel,
    MviModel<InboxMviModel.Intent, InboxMviModel.UiState, InboxMviModel.Effect> by mvi {

    override fun reduce(intent: InboxMviModel.Intent) {
        when (intent) {
            is InboxMviModel.Intent.ChangeSection -> mvi.updateState {
                it.copy(section = intent.value)
            }

            is InboxMviModel.Intent.ChangeUnreadOnly -> mvi.updateState {
                it.copy(unreadOnly = intent.unread)
            }

            InboxMviModel.Intent.ReadAll -> {
                markAllRead()
            }
        }
    }

    private fun markAllRead() {
        mvi.scope.launch(Dispatchers.IO) {
            val auth = identityRepository.authToken.value
            userRepository.readAll(auth)
            mvi.emitEffect(InboxMviModel.Effect.Refresh)
        }
    }
}
