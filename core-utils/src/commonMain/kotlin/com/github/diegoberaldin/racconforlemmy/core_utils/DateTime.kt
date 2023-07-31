package com.github.diegoberaldin.racconforlemmy.core_utils

expect object DateTime {
    fun getFormattedDate(
        iso8601Timestamp: String,
        format: String,
    ): String
}