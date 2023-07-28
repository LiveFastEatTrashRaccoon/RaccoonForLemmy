package com.github.diegoberaldin.racconforlemmy.core_utils

import platform.Foundation.NSLog

actual object Log {
    actual fun d(message: String) {
        NSLog(message)
    }
}