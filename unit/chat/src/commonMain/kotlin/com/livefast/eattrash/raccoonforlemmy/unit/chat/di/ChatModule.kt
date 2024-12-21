package com.livefast.eattrash.raccoonforlemmy.unit.chat.di

import com.livefast.eattrash.raccoonforlemmy.unit.chat.InboxChatMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.chat.InboxChatViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance

val chatModule =
    DI.Module("ChatModule") {
        bind<InboxChatMviModel> {
            factory { otherUserId: Long ->
                InboxChatViewModel(
                    otherUserId = otherUserId,
                    identityRepository = instance(),
                    siteRepository = instance(),
                    messageRepository = instance(),
                    userRepository = instance(),
                    settingsRepository = instance(),
                    mediaRepository = instance(),
                    notificationCenter = instance(),
                )
            }
        }
    }
