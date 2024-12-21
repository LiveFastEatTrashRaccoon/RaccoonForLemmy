package com.livefast.eattrash.raccoonforlemmy.core.notifications.di

import com.livefast.eattrash.raccoonforlemmy.core.notifications.DefaultNotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenter
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

val notificationsModule =
    DI.Module("NotificationsModule") {
        bind<NotificationCenter> {
            singleton {
                DefaultNotificationCenter()
            }
        }
    }
