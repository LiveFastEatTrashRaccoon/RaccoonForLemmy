package com.livefast.eattrash.raccoonforlemmy.domain.inbox.di

import com.livefast.eattrash.raccoonforlemmy.domain.inbox.notification.DefaultInboxNotificationChecker
import com.livefast.eattrash.raccoonforlemmy.domain.inbox.notification.InboxNotificationChecker
import org.koin.dsl.module

actual val inboxNativeModule =
    module {
        single<InboxNotificationChecker> {
            DefaultInboxNotificationChecker()
        }
    }
