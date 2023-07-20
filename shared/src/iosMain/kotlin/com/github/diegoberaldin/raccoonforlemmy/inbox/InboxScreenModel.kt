package com.github.diegoberaldin.raccoonforlemmy.inbox

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual fun getInboxScreenModel() = InboxScreenModelHelper().model

class InboxScreenModelHelper : KoinComponent {
    val model: InboxScreenModel by inject()
}