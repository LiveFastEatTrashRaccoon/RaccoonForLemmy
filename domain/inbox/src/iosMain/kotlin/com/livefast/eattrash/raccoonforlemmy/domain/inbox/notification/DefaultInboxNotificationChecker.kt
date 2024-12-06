package com.livefast.eattrash.raccoonforlemmy.domain.inbox.notification

import org.koin.core.annotation.Single

@Single
internal actual class DefaultInboxNotificationChecker : InboxNotificationChecker {
    actual override val isBackgroundCheckSupported = false

    actual override fun setPeriod(minutes: Long) {
        // NO-OP
    }

    actual override fun start() {
        // NO-OP
    }

    actual override fun stop() {
        // NO-OP
    }
}
