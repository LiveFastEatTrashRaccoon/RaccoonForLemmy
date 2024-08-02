package com.livefast.eattrash.raccoonforlemmy.core.api.provider

import com.livefast.eattrash.raccoonforlemmy.core.utils.debug.logDebug
import io.ktor.client.plugins.logging.Logger

internal val defaultLogger =
    object : Logger {
        override fun log(message: String) {
            logDebug(message)
        }
    }
