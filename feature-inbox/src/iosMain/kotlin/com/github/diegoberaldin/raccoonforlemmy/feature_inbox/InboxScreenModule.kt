package com.github.diegoberaldin.raccoonforlemmy.feature_inbox

import com.github.diegoberaldin.raccoonforlemmy.core_architecture.DefaultMviModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.dsl.module

actual val inboxTabModule = module {
    factory {
        InboxScreenModel(
            mvi = DefaultMviModel(InboxScreenMviModel.UiState())
        )
    }
}

actual fun getInboxScreenModel() = InboxScreenModelHelper.model

object InboxScreenModelHelper : KoinComponent {
    val model: InboxScreenModel by inject()
}