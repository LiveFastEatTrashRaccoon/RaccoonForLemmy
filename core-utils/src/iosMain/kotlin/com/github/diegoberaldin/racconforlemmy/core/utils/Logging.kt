package com.github.diegoberaldin.racconforlemmy.core.utils

import platform.Foundation.NSLog

actual object Log {
    actual fun d(message: String) {
        NSLog(message)
    }
}
