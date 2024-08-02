package com.livefast.eattrash.raccoonforlemmy.unit.chat.di

import com.livefast.eattrash.raccoonforlemmy.unit.chat.InboxChatMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.chat.InboxChatViewModel
import org.koin.dsl.module

val chatModule =
    module {
        factory<InboxChatMviModel> { params ->
            InboxChatViewModel(
                otherUserId = params[0],
                identityRepository = get(),
                siteRepository = get(),
                userRepository = get(),
                messageRepository = get(),
                mediaRepository = get(),
                notificationCenter = get(),
                settingsRepository = get(),
            )
        }
    }
