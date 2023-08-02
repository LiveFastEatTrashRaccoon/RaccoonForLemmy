package com.github.diegoberaldin.racconforlemmy.core.utils

import java.time.LocalDateTime
import java.time.Period
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

actual object DateTime {
    actual fun getFormattedDate(
        iso8601Timestamp: String,
        format: String,
    ): String {
        val date = getDateFromIso8601Timestamp(iso8601Timestamp)
        val formatter = DateTimeFormatter.ofPattern(format)
        return date.format(formatter)
    }

    actual fun getPrettyDate(
        iso8601Timestamp: String,
        yearLabel: String,
        monthLabel: String,
        dayLabel: String,
    ): String {
        val now = LocalDateTime.now().toLocalDate()
        val date = getDateFromIso8601Timestamp(iso8601Timestamp).toLocalDate()
        val delta = Period.between(date, now)
        return when {
            delta.years >= 1 -> buildString {
                append("${delta.years}$yearLabel")
                if (delta.months >= 1) {
                    append(" ${delta.months}$monthLabel")
                }
                if (delta.days >= 1) {
                    append(" ${delta.days}$dayLabel")
                }
            }

            delta.months >= 1 -> buildString {
                append("${delta.months}$monthLabel")
                if (delta.days >= 1) {
                    append(" ${delta.days}$dayLabel")
                }
            }

            else -> buildString {
                append("${delta.days}$dayLabel")
            }
        }
    }

    private fun getDateFromIso8601Timestamp(string: String): ZonedDateTime {
        return ZonedDateTime.parse(string)
    }
}
