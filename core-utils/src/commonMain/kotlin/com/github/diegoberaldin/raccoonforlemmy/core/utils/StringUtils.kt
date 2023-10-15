package com.github.diegoberaldin.raccoonforlemmy.core.utils

expect object StringUtils {
    fun String.isValidUrl(): Boolean
    fun Int.toHexDigit(): String
}