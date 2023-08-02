package com.github.diegoberaldin.raccoonforlemmy.feature.inbox

import cafe.adriel.voyager.core.model.ScreenModel
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel

class InboxScreenModel(
    private val mvi: DefaultMviModel<InboxScreenMviModel.Intent, InboxScreenMviModel.UiState, InboxScreenMviModel.Effect>,
) : ScreenModel,
    MviModel<InboxScreenMviModel.Intent, InboxScreenMviModel.UiState, InboxScreenMviModel.Effect> by mvi
