package com.github.diegoberaldin.racconforlemmy.core.utils

import android.util.Patterns

actual object StringUtils {
    actual fun String.isValidUrl(): Boolean = Patterns.WEB_URL.matcher(this).matches()
}