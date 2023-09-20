package com.github.diegoberaldin.racconforlemmy.core.utils

import platform.Foundation.NSURL


actual object StringUtils {
    actual fun String.isValidUrl(): Boolean = NSURL.URLWithString(this) != null
}