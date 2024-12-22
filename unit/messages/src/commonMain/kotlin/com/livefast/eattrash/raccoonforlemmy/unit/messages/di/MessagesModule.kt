package com.livefast.eattrash.raccoonforlemmy.unit.messages.di

import com.livefast.eattrash.raccoonforlemmy.unit.messages.InboxMessagesMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.messages.InboxMessagesViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

val messagesModule =
    DI.Module("MessagesModule") {
        bind<InboxMessagesMviModel> {
            provider {
                InboxMessagesViewModel(
                    identityRepository = instance(),
                    siteRepository = instance(),
                    messageRepository = instance(),
                    settingsRepository = instance(),
                    coordinator = instance(),
                    notificationCenter = instance(),
            )
        }
    }
}
