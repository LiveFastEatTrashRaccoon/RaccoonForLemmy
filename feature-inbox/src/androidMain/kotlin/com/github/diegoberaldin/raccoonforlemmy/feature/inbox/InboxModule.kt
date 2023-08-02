package com.github.diegoberaldin.raccoonforlemmy.feature.inbox

import com.github.diegoberaldin.raccoonforlemmy.core.architecture.DefaultMviModel
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.inject

actual val inboxTabModule = module {
    factory {
        InboxScreenModel(
            mvi = DefaultMviModel(InboxScreenMviModel.UiState()),
        )
    }
}

actual fun getInboxScreenModel(): InboxScreenModel {
    val res: InboxScreenModel by inject(InboxScreenModel::class.java)
    return res
}
