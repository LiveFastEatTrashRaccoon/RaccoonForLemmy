package com.github.diegoberaldin.raccoonforlemmy.feature.inbox.di

import com.github.diegoberaldin.raccoonforlemmy.feature.inbox.mentions.InboxMentionsViewModel
import com.github.diegoberaldin.raccoonforlemmy.feature.inbox.replies.InboxRepliesViewModel
import com.github.diegoberaldin.raccoonforlemmy.feature.inbox.ui.InboxViewModel

expect fun getInboxViewModel(): InboxViewModel

expect fun getInboxRepliesViewModel(): InboxRepliesViewModel

expect fun getInboxMentionsViewModel(): InboxMentionsViewModel
