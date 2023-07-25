package com.github.diegoberaldin.raccoonforlemmy.core_api.provider

import android.util.Log
import io.ktor.client.plugins.logging.Logger

internal actual val defaultLogger: Logger = object : Logger {
    override fun log(message: String) {
        Log.d("com.github.diegoberaldin.raccoonforlemmy", message)
    }
}