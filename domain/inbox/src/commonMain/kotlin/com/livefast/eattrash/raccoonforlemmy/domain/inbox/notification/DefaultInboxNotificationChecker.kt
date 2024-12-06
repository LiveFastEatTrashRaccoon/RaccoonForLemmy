package com.livefast.eattrash.raccoonforlemmy.domain.inbox.notification

import org.koin.core.annotation.Single

@Single
internal expect class DefaultInboxNotificationChecker : InboxNotificationChecker {
    override val isBackgroundCheckSupported: Boolean

    override fun setPeriod(minutes: Long)

    override fun start()

    override fun stop()
}
