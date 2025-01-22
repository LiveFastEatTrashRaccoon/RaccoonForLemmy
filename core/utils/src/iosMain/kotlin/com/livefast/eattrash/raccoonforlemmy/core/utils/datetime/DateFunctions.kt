package com.livefast.eattrash.raccoonforlemmy.core.utils.datetime

import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarIdentifierGregorian
import platform.Foundation.NSCalendarUnitDay
import platform.Foundation.NSCalendarUnitHour
import platform.Foundation.NSCalendarUnitMinute
import platform.Foundation.NSCalendarUnitMonth
import platform.Foundation.NSCalendarUnitSecond
import platform.Foundation.NSCalendarUnitYear
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSISO8601DateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.NSTimeZone
import platform.Foundation.autoupdatingCurrentLocale
import platform.Foundation.localTimeZone
import platform.Foundation.timeIntervalSince1970
import kotlin.math.roundToLong

actual fun epochMillis(): Long = (NSDate().timeIntervalSince1970 * 1000).toLong()

actual fun Long.toIso8601Timestamp(): String? {
    val dateFormatter = NSDateFormatter()
    dateFormatter.locale = NSLocale.autoupdatingCurrentLocale
    dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'"
    dateFormatter.calendar = NSCalendar(calendarIdentifier = NSCalendarIdentifierGregorian)
    val date = NSDate(timeIntervalSinceReferenceDate = (this.toDouble() / 1000))
    return dateFormatter.stringFromDate(date)
}

actual fun String.toTimestamp(): Long {
    val date = getDateFromIso8601Timestamp(this)
    return date?.timeIntervalSince1970?.let { (it * 1000).roundToLong() } ?: 0
}

actual fun getFormattedDate(
    iso8601Timestamp: String,
    format: String,
): String {
    val date = getDateFromIso8601Timestamp(iso8601Timestamp) ?: return ""

    val dateFormatter = NSDateFormatter()
    dateFormatter.timeZone = NSTimeZone.localTimeZone
    dateFormatter.locale = NSLocale.autoupdatingCurrentLocale
    dateFormatter.dateFormat = format
    return dateFormatter.stringFromDate(date)
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
    val date = getDateFromIso8601Timestamp(iso8601Timestamp) ?: return ""
    val now = NSDate()
    val calendar = NSCalendar(calendarIdentifier = NSCalendarIdentifierGregorian)
    val delta =
        calendar.components(
            unitFlags =
                NSCalendarUnitSecond
                    .or(NSCalendarUnitMinute)
                    .or(NSCalendarUnitHour)
                    .or(NSCalendarUnitDay)
                    .or(NSCalendarUnitMonth)
                    .or(NSCalendarUnitYear),
            fromDate = date,
            toDate = now,
            options = 0u,
        )
    return when {
        delta.year >= 1 ->
            buildString {
                append("${delta.year}$yearLabel")
                if (finePrecision) {
                    if (delta.month > 0) {
                        append(" ${delta.month}$monthLabel")
                    }
                    if (delta.day > 0) {
                        append(" ${delta.day}$dayLabel")
                    }
                }
            }

        delta.month >= 1 ->
            buildString {
                append("${delta.month}$monthLabel")
                if (finePrecision) {
                    if (delta.day > 0) {
                        append(" ${delta.day}$dayLabel")
                    }
                }
            }

        delta.day >= 1 ->
            buildString {
                append("${delta.day}$dayLabel")
                if (finePrecision) {
                    if (delta.hour > 0 || delta.minute > 0) {
                        append(" ${delta.hour}$hourLabel")
                    }
                    // minutes and seconds are intentionally omitted
                }
            }

        delta.hour >= 1 ->
            buildString {
                append(" ${delta.hour}$hourLabel")
                // minutes and seconds are intentionally omitted
            }

        delta.minute >= 1 ->
            buildString {
                append(" ${delta.minute}$minuteLabel")
                // seconds are intentionally omitted
            }

        else ->
            buildString {
                append(" ${delta.second}$secondLabel")
            }
    }
}

private fun getDateFromIso8601Timestamp(string: String): NSDate? = NSISO8601DateFormatter().dateFromString(string)
