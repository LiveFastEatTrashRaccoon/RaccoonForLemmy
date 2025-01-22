package com.livefast.eattrash.raccoonforlemmy.core.utils.datetime

expect fun epochMillis(): Long

expect fun Long.toIso8601Timestamp(): String?

expect fun String.toTimestamp(): Long

expect fun getFormattedDate(
    iso8601Timestamp: String,
    format: String,
): String

expect fun getPrettyDate(
    iso8601Timestamp: String,
    yearLabel: String,
    monthLabel: String,
    dayLabel: String,
    hourLabel: String,
    minuteLabel: String,
    secondLabel: String,
    finePrecision: Boolean = true,
): String
