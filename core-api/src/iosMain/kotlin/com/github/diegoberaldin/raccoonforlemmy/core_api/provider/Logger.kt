package com.github.diegoberaldin.raccoonforlemmy.core_api.provider

import io.ktor.client.plugins.logging.Logger
import platform.Foundation.NSLog

internal actual val defaultLogger: Logger = object : Logger {
    override fun log(message: String) {
        NSLog(message)
    }
}