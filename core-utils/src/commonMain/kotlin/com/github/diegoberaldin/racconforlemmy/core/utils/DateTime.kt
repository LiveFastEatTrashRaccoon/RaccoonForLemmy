package com.github.diegoberaldin.racconforlemmy.core.utils

expect object DateTime {
    fun getFormattedDate(
        iso8601Timestamp: String,
        format: String,
    ): String

    fun getPrettyDate(
        iso8601Timestamp: String,
        yearLabel: String,
        monthLabel: String,
        dayLabel: String,
    ): String
}
