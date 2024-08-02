package com.livefast.eattrash.raccoonforlemmy.core.notifications.di

import com.livefast.eattrash.raccoonforlemmy.core.notifications.DefaultNotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenter
import org.koin.dsl.module

val coreNotificationModule =
    module {
        single<NotificationCenter> {
            DefaultNotificationCenter()
        }
    }
