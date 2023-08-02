package com.github.diegoberaldin.raccoonforlemmy.core.api.provider

import com.github.diegoberaldin.racconforlemmy.core.utils.Log
import io.ktor.client.plugins.logging.Logger

internal val defaultLogger = object : Logger {
    override fun log(message: String) {
        Log.d(message)
    }
}
