package com.livefast.eattrash.raccoonforlemmy.feature.inbox.di

import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.bindViewModel
import com.livefast.eattrash.raccoonforlemmy.feature.inbox.main.InboxViewModel
import org.kodein.di.DI
import org.kodein.di.instance

val inboxTabModule =
    DI.Module("InboxTabModule") {
        bindViewModel {
            InboxViewModel(
                identityRepository = instance(),
                userRepository = instance(),
                coordinator = instance(),
                settingsRepository = instance(),
                notificationCenter = instance(),
            )
        }
    }
