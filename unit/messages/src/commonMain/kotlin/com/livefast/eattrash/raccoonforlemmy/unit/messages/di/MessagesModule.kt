package com.livefast.eattrash.raccoonforlemmy.unit.messages.di

import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.bindViewModel
import com.livefast.eattrash.raccoonforlemmy.unit.messages.InboxMessagesViewModel
import org.kodein.di.DI
import org.kodein.di.instance

val messagesModule =
    DI.Module("MessagesModule") {
        bindViewModel {
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
