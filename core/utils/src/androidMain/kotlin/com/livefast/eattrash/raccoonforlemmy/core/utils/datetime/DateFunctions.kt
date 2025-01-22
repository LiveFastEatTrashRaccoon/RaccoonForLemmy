package com.livefast.eattrash.raccoonforlemmy.core.utils.datetime

import java.time.Duration
import java.time.LocalDateTime
import java.time.Period
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.TimeZone
import kotlin.time.toKotlinDuration

actual fun epochMillis(): Long = System.currentTimeMillis()

private fun getDateTimeFormatter(pattern: String) =
    DateTimeFormatter
        .ofPattern(pattern)
        .withLocale(Locale.US)
        .withZone(TimeZone.getTimeZone("UTC").toZoneId())

private val safeFormatter = getDateTimeFormatter("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")

actual fun Long.toIso8601Timestamp(): String? {
    val date = LocalDateTime.ofEpochSecond(this, 0, ZoneOffset.UTC)
    return safeFormatter.format(date)
}

actual fun String.toTimestamp(): Long {
    val date = getDateFromIso8601Timestamp(this)
    return date.toInstant().toEpochMilli()
}

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
    hourLabel: String,
    minuteLabel: String,
    secondLabel: String,
    finePrecision: Boolean,
): String {
    val now = ZonedDateTime.now()
    val date = getDateFromIso8601Timestamp(iso8601Timestamp)
    val period = Period.between(date.toLocalDate(), now.toLocalDate())
    val duration = Duration.between(date.toInstant(), now.toInstant()).toKotlinDuration()

    val years = period.years
    val months = period.months
    val days = period.days
    val hours = duration.inWholeHours % 24
    val minutes = duration.inWholeMinutes % 60
    val seconds = duration.inWholeSeconds % 60

    return when {
        years >= 1 ->
            buildString {
                append("${years}$yearLabel")
                if (finePrecision) {
                    if (months > 0) {
                        append(" ${months}$monthLabel")
                    }
                    if (days > 0) {
                        append(" ${days}$dayLabel")
                    }
                }
            }

        months >= 1 ->
            buildString {
                append("${months}$monthLabel")
                if (finePrecision) {
                    if (days > 0) {
                        append(" ${days}$dayLabel")
                    }
                }
            }

        days >= 1 ->
            buildString {
                append("${days}$dayLabel")
                if (finePrecision) {
                    if (hours > 0 || minutes > 0) {
                        append(" ${hours}$hourLabel")
                    }
                    // minutes and seconds are intentionally omitted
                }
            }

        hours >= 1 ->
            buildString {
                append(" ${hours}$hourLabel")
                // minutes and seconds are intentionally omitted
            }

        minutes >= 1 ->
            buildString {
                append(" ${minutes}$minuteLabel")
                // seconds are intentionally omitted
            }

        else ->
            buildString {
                append(" ${seconds}$secondLabel")
            }
    }
}

private fun getDateFromIso8601Timestamp(string: String): ZonedDateTime = ZonedDateTime.parse(string)
