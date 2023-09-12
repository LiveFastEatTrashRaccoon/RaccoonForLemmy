package com.github.diegoberaldin.raccoonforlemmy.feature.inbox.di

import com.github.diegoberaldin.raccoonforlemmy.feature.inbox.main.InboxViewModel
import com.github.diegoberaldin.raccoonforlemmy.feature.inbox.mentions.InboxMentionsViewModel
import com.github.diegoberaldin.raccoonforlemmy.feature.inbox.replies.InboxRepliesViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual fun getInboxViewModel() = InboxScreenModelHelper.model

actual fun getInboxRepliesViewModel() = InboxScreenModelHelper.repliesModel

actual fun getInboxMentionsViewModel() = InboxScreenModelHelper.mentionsModel

object InboxScreenModelHelper : KoinComponent {
    val model: InboxViewModel by inject()
    val repliesModel: InboxRepliesViewModel by inject()
    val mentionsModel: InboxMentionsViewModel by inject()
}
