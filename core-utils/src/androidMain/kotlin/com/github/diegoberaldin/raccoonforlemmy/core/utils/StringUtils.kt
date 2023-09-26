package com.github.diegoberaldin.raccoonforlemmy.core.utils

import android.util.Patterns

actual object StringUtils {
    actual fun String.isValidUrl(): Boolean = Patterns.WEB_URL.matcher(this).matches()
}