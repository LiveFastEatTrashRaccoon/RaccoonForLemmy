package com.livefast.eattrash.raccoonforlemmy.domain.inbox.di

import com.livefast.eattrash.raccoonforlemmy.domain.inbox.notification.DefaultInboxNotificationChecker
import com.livefast.eattrash.raccoonforlemmy.domain.inbox.notification.InboxNotificationChecker
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

internal actual val nativeInboxModule =
    DI.Module("NativeInboxModule") {
        bind<InboxNotificationChecker> {
            singleton {
                DefaultInboxNotificationChecker(
                    context = instance(),
                )
            }
        }
    }
