package com.livefast.eattrash.raccoonforlemmy.domain.inbox.notification

internal class DefaultInboxNotificationChecker : InboxNotificationChecker {
    override val isBackgroundCheckSupported = false

    override fun setPeriod(minutes: Long) {
        // NO-OP
    }

    override fun start() {
        // NO-OP
    }

    override fun stop() {
        // NO-OP
    }
}
