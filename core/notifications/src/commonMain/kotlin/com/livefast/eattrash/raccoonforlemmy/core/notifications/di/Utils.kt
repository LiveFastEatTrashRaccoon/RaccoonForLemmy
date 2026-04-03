package com.livefast.eattrash.raccoonforlemmy.core.notifications.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.livefast.eattrash.raccoonforlemmy.core.di.RootDI
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenter
import org.kodein.di.instance

fun getNotificationCenter(): NotificationCenter {
    val res by RootDI.di.instance<NotificationCenter>()
    return res
}

@Composable
fun rememberNotificationCenter(): NotificationCenter = remember { getNotificationCenter() }
