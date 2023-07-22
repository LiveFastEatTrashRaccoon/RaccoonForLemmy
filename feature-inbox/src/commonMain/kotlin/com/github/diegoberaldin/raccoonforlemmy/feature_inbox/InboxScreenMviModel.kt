package com.github.diegoberaldin.raccoonforlemmy.feature_inbox

import com.github.diegoberaldin.raccoonforlemmy.core_architecture.MviModel

interface InboxScreenMviModel :
    MviModel<InboxScreenMviModel.Intent, InboxScreenMviModel.UiState, InboxScreenMviModel.Effect> {

    sealed interface Intent

    data class UiState(val loading: Boolean = false)

    sealed interface Effect
}