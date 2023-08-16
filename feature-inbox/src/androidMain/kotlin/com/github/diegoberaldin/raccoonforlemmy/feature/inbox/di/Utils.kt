package com.github.diegoberaldin.raccoonforlemmy.feature.inbox.di

import com.github.diegoberaldin.raccoonforlemmy.feature.inbox.mentions.InboxMentionsViewModel
import com.github.diegoberaldin.raccoonforlemmy.feature.inbox.replies.InboxRepliesViewModel
import com.github.diegoberaldin.raccoonforlemmy.feature.inbox.ui.InboxViewModel
import org.koin.java.KoinJavaComponent

actual fun getInboxViewModel(): InboxViewModel {
    val res: InboxViewModel by KoinJavaComponent.inject(InboxViewModel::class.java)
    return res
}

actual fun getInboxRepliesViewModel(): InboxRepliesViewModel {
    val res: InboxRepliesViewModel by KoinJavaComponent.inject(InboxRepliesViewModel::class.java)
    return res
}

actual fun getInboxMentionsViewModel(): InboxMentionsViewModel {
    val res: InboxMentionsViewModel by KoinJavaComponent.inject(InboxMentionsViewModel::class.java)
    return res
}
