package com.livefast.eattrash.raccoonforlemmy.core.notifications.di

import com.livefast.eattrash.raccoonforlemmy.core.di.RootDI
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenter
import org.kodein.di.instance

fun getNotificationCenter(): NotificationCenter {
    val res by RootDI.di.instance<NotificationCenter>()
    return res
}
