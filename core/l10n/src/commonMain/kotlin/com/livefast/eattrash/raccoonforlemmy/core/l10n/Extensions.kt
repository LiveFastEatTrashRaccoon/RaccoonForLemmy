package com.livefast.eattrash.raccoonforlemmy.core.l10n

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.LayoutDirection

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

internal fun String.toLanguageDirection(): LayoutDirection =
    when (this) {
        "ar" -> LayoutDirection.Rtl
        else -> LayoutDirection.Ltr
    }
