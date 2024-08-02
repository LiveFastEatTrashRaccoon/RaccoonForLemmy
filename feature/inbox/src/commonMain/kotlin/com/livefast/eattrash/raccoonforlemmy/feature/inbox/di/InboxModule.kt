package com.livefast.eattrash.raccoonforlemmy.feature.inbox.di

import com.livefast.eattrash.raccoonforlemmy.feature.inbox.main.InboxMviModel
import com.livefast.eattrash.raccoonforlemmy.feature.inbox.main.InboxViewModel
import com.livefast.eattrash.raccoonforlemmy.unit.mentions.di.inboxMentionsModule
import com.livefast.eattrash.raccoonforlemmy.unit.messages.di.inboxMessagesModule
import com.livefast.eattrash.raccoonforlemmy.unit.replies.di.inboxRepliesModule
import org.koin.dsl.module

val inboxTabModule =
    module {
        includes(
            inboxRepliesModule,
            inboxMessagesModule,
            inboxMentionsModule,
        )
        factory<InboxMviModel> {
            InboxViewModel(
                identityRepository = get(),
                userRepository = get(),
                coordinator = get(),
                settingsRepository = get(),
                notificationCenter = get(),
            )
        }
    }
