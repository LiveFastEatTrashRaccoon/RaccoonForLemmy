package com.livefast.eattrash.raccoonforlemmy.unit.messages.di

import com.livefast.eattrash.raccoonforlemmy.unit.messages.InboxMessagesMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.messages.InboxMessagesViewModel
import org.koin.dsl.module

val inboxMessagesModule =
    module {
        factory<InboxMessagesMviModel> {
            InboxMessagesViewModel(
                identityRepository = get(),
                siteRepository = get(),
                messageRepository = get(),
                coordinator = get(),
                notificationCenter = get(),
                settingsRepository = get(),
            )
        }
    }
