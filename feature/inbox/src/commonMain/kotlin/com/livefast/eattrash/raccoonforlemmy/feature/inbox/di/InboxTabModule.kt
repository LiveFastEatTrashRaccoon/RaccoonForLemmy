package com.livefast.eattrash.raccoonforlemmy.feature.inbox.di

import com.livefast.eattrash.raccoonforlemmy.feature.inbox.main.InboxMviModel
import com.livefast.eattrash.raccoonforlemmy.feature.inbox.main.InboxViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

val inboxTabModule =
    DI.Module("InboxTabModule") {
        bind<InboxMviModel> {
            provider {
                InboxViewModel(
                    identityRepository = instance(),
                    userRepository = instance(),
                    coordinator = instance(),
                    settingsRepository = instance(),
                    notificationCenter = instance(),
                )
            }
        }
    }
