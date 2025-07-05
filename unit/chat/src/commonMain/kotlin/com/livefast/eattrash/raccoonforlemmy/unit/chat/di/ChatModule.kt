package com.livefast.eattrash.raccoonforlemmy.unit.chat.di

import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.ViewModelCreationArgs
import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.bindViewModelWithArgs
import com.livefast.eattrash.raccoonforlemmy.unit.chat.InboxChatViewModel
import org.kodein.di.DI
import org.kodein.di.instance

data class InboxChatMviModelParams(val otherUserId: Long) : ViewModelCreationArgs

val chatModule =
    DI.Module("ChatModule") {
        bindViewModelWithArgs { params: InboxChatMviModelParams ->
            InboxChatViewModel(
                otherUserId = params.otherUserId,
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
