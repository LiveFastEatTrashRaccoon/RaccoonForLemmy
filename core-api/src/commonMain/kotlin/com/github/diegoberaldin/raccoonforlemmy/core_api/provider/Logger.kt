package com.github.diegoberaldin.raccoonforlemmy.core_api.provider

import com.github.diegoberaldin.racconforlemmy.core_utils.Log
import io.ktor.client.plugins.logging.Logger

internal val defaultLogger = object : Logger {
    override fun log(message: String) {
        Log.d(message)
    }
}
