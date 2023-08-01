package com.github.diegoberaldin.racconforlemmy.core_utils

import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarIdentifierGregorian
import platform.Foundation.NSCalendarUnitDay
import platform.Foundation.NSCalendarUnitMonth
import platform.Foundation.NSCalendarUnitYear
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSISO8601DateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.NSTimeZone
import platform.Foundation.autoupdatingCurrentLocale
import platform.Foundation.localTimeZone

actual object DateTime {

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
    ): String {
        val date = getDateFromIso8601Timestamp(iso8601Timestamp) ?: return ""
        val now = NSDate()
        val calendar = NSCalendar(calendarIdentifier = NSCalendarIdentifierGregorian)
        val delta = calendar.components(
            unitFlags = NSCalendarUnitDay.or(NSCalendarUnitMonth).or(NSCalendarUnitYear),
            fromDate = date,
            toDate = now,
            options = 0,
        )
        return when {
            delta.year >= 1 -> buildString {
                append("${delta.year}$yearLabel")
                if (delta.month >= 1) {
                    append(" ${delta.month}$monthLabel")
                }
                if (delta.day >= 1) {
                    append(" ${delta.day}$dayLabel")
                }
            }

            delta.month >= 1 -> buildString {
                append("${delta.month}$monthLabel")
                if (delta.day >= 1) {
                    append(" ${delta.day}$dayLabel")
                }
            }

            else -> buildString {
                append("${delta.day}$dayLabel")
            }
        }
    }

    private fun getDateFromIso8601Timestamp(string: String): NSDate? {
        return NSISO8601DateFormatter().dateFromString(string)
    }
}
