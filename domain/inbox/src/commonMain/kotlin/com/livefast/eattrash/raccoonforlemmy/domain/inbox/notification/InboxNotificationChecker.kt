package com.livefast.eattrash.raccoonforlemmy.domain.inbox.notification

interface InboxNotificationChecker {
    val isBackgroundCheckSupported: Boolean

    fun setPeriod(minutes: Long)

    fun start()

    fun stop()
}
