package com.github.diegoberaldin.raccoonforlemmy.core.utils

import platform.Foundation.NSLog

actual object Log {
    actual fun d(message: String) {
        NSLog(message)
    }
}
