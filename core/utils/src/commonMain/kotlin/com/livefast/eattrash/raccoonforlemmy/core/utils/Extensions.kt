package com.livefast.eattrash.raccoonforlemmy.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import com.livefast.eattrash.raccoonforlemmy.core.l10n.messages.Locales
import io.ktor.utils.io.core.toByteArray
import org.kotlincrypto.hash.md.MD5
import kotlin.math.round

@Composable
fun String.toLanguageName() =
    when (this) {
        Locales.DE -> "Deutsch"
        Locales.GA -> "Gaeilge"
        Locales.ES -> "Español"
        Locales.FI -> "Suomi"
        Locales.FR -> "Français"
        Locales.IT -> "Italiano"
        Locales.PL -> "Polski"
        Locales.PT -> "Português"
        Locales.PT_BR -> "Português (Brazil)"
        Locales.UA -> "Українська"
        Locales.ZH_CN -> "中文"
        Locales.ZH_TW -> "正體中文"
        Locales.ZH_HK -> "廣東話"
        else -> "English"
    }

@Composable
fun String.toLanguageFlag(): String =
    when (this) {
        Locales.DE -> "🇩🇪"
        Locales.EN -> "🇬🇧"
        Locales.ES -> "🇪🇸"
        Locales.GA -> "🇮🇪"
        Locales.FI -> "🇫🇮"
        Locales.FR -> "🇫🇷"
        Locales.IT -> "🇮🇹"
        Locales.PL -> "🇵🇱"
        Locales.PT -> "🇵🇹"
        Locales.PT_BR -> "🇧🇷"
        Locales.UA -> "🇺🇦"
        Locales.ZH_CN -> "🇨🇳"
        Locales.ZH_TW -> "🇹🇼"
        Locales.ZH_HK -> "🇭🇰"
        else -> ""
    }

fun String.toLanguageDirection(): LayoutDirection =
    when (this) {
        "ar" -> LayoutDirection.Rtl
        else -> LayoutDirection.Ltr
    }

@Composable
fun Dp.toLocalPixel(): Float =
    with(LocalDensity.current) {
        value * density
    }

@Composable
fun Float.toLocalDp(): Dp =
    with(LocalDensity.current) {
        this@toLocalDp.toDp()
    }

fun Int.getPrettyNumber(
    millionLabel: String,
    thousandLabel: String,
): String {
    val value = this
    return when {
        value > 1_000_000 ->
            buildString {
                val rounded = round((value / 1_000_000.0) * 10) / 10
                if (rounded % 1 <= 0) {
                    append(rounded.toInt())
                } else {
                    append(rounded)
                }
                append(millionLabel)
            }

        value > 1_000 ->
            buildString {
                val rounded = round((value / 1_000.0) * 10) / 10
                if (rounded % 1 <= 0) {
                    append(rounded.toInt())
                } else {
                    append(rounded)
                }
                append(thousandLabel)
            }

        else ->
            buildString {
                append(value)
            }
    }
}

@OptIn(ExperimentalStdlibApi::class)
fun String.md5(): String {
    val digest = MD5()
    val bytes = digest.digest(toByteArray())
    return bytes.joinToString {
        it.toHexString()
    }
}

fun Int.toInboxUnreadOnly(): Boolean = this == 0

fun Boolean.toInboxDefaultType(): Int = if (this) 0 else 1

val String.looksLikeAnImage: Boolean
    get() {
        val extensions = listOf(".jpeg", ".jpg", ".png", ".webp", ".gif")
        return extensions.any { this.endsWith(it) }
    }

val String.looksLikeAVideo: Boolean
    get() {
        val extensions = listOf(".mp4", ".mov", ".webm", ".avi")
        return extensions.any { this.endsWith(it) }
    }

val String.isRedGifs: Boolean
    get() = contains("redgifs.com")

fun String?.ellipsize(
    length: Int = 100,
    ellipsis: String = "…",
): String {
    if (isNullOrEmpty() || length == 0) {
        return ""
    }
    if (this.length < length) {
        return this
    }
    return take(length - 1) + ellipsis
}
