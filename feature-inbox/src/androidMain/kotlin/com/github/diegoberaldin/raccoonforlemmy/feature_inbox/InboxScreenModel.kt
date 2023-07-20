package com.github.diegoberaldin.raccoonforlemmy.feature_inbox

import org.koin.java.KoinJavaComponent.inject

actual fun getInboxScreenModel(): InboxScreenModel {
    val res: InboxScreenModel by inject(InboxScreenModel::class.java)
    return res
}