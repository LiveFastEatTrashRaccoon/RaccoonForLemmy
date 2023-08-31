package com.github.diegoberaldin.raccoonforlemmy.feature.inbox.main

import cafe.adriel.voyager.core.model.ScreenModel
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel

class InboxViewModel(
    private val mvi: DefaultMviModel<InboxMviModel.Intent, InboxMviModel.UiState, InboxMviModel.Effect>,
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
        }
    }
}
