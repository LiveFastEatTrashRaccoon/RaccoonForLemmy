package com.livefast.eattrash.raccoonforlemmy.core.utils.datetime

import androidx.compose.runtime.Composable
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import kotlin.math.round
import kotlin.time.Duration

@Composable
fun String.prettifyDate(): String =
    let {
        when {
            it.isEmpty() -> it
            !it.endsWith("Z") -> {
                getPrettyDate(
                    iso8601Timestamp = it + "Z",
                    yearLabel = LocalStrings.current.profileYearShort,
                    monthLabel = LocalStrings.current.profileMonthShort,
                    dayLabel = LocalStrings.current.profileDayShort,
                    hourLabel = LocalStrings.current.postHourShort,
                    minuteLabel = LocalStrings.current.postMinuteShort,
                    secondLabel = LocalStrings.current.postSecondShort,
                )
            }

            else -> {
                getPrettyDate(
                    iso8601Timestamp = it,
                    yearLabel = LocalStrings.current.profileYearShort,
                    monthLabel = LocalStrings.current.profileMonthShort,
                    dayLabel = LocalStrings.current.profileDayShort,
                    hourLabel = LocalStrings.current.postHourShort,
                    minuteLabel = LocalStrings.current.postMinuteShort,
                    secondLabel = LocalStrings.current.postSecondShort,
                )
            }
        }
    }

fun Duration.getPrettyDuration(
    secondsLabel: String,
    minutesLabel: String,
    hoursLabel: String,
    daysLabel: String,
    finePrecision: Boolean = true,
): String =
    when {
        inWholeDays > 0 ->
            buildString {
                append(inWholeDays)
                append(daysLabel)
                val remainderHours = inWholeHours % 24
                val remainderMinutes = inWholeMinutes % 60
                val remainderSeconds = inWholeSeconds % 60
                if (remainderHours > 0) {
                    append(" ")
                    append(remainderHours)
                    append(hoursLabel)
                }
                if (finePrecision) {
                    if (remainderMinutes > 0) {
                        append(" ")
                        append(remainderMinutes)
                        append(minutesLabel)
                    }
                    if (remainderSeconds > 0) {
                        append(" ")
                        append(remainderSeconds)
                        append(secondsLabel)
                    }
                }
            }

        inWholeHours > 0 ->
            buildString {
                append(inWholeHours)
                append(hoursLabel)
                val remainderMinutes = inWholeMinutes % 60
                val remainderSeconds = inWholeSeconds % 60
                if (remainderMinutes > 0) {
                    append(" ")
                    append(remainderMinutes)
                    append(minutesLabel)
                }
                if (finePrecision) {
                    if (remainderSeconds > 0) {
                        append(" ")
                        append(remainderSeconds)
                        append(secondsLabel)
                    }
                }
            }

        inWholeMinutes > 0 ->
            buildString {
                append(inWholeMinutes)
                append(minutesLabel)
                val remainderSeconds = inWholeSeconds % 60
                if (remainderSeconds > 0) {
                    append(" ")
                    append(remainderSeconds)
                    append(secondsLabel)
                }
            }

        else ->
            buildString {
                val rounded = round((inWholeMilliseconds / 1000.0) * 10.0) / 10.0
                if (rounded % 1 <= 0) {
                    append(rounded.toInt())
                } else {
                    append(rounded)
                }
                append(secondsLabel)
            }
    }
