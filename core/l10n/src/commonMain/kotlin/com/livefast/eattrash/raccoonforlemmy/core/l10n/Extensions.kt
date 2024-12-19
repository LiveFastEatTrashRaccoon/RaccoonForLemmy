package com.livefast.eattrash.raccoonforlemmy.core.l10n

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.LayoutDirection

@Composable
fun String.toLanguageName() =
    when (this) {
        Locales.DE -> LocalStrings.current.languageDe
        Locales.GA -> LocalStrings.current.languageGa
        Locales.ES -> LocalStrings.current.languageEs
        Locales.FI -> LocalStrings.current.languageFi
        Locales.FR -> LocalStrings.current.languageFr
        Locales.IT -> LocalStrings.current.languageIt
        Locales.PL -> LocalStrings.current.languagePl
        Locales.PT -> LocalStrings.current.languagePt
        Locales.PT_BR -> LocalStrings.current.languagePtBr
        Locales.UA -> LocalStrings.current.languageUa
        Locales.ZH_CN -> LocalStrings.current.languageZhCn
        Locales.ZH_HK -> LocalStrings.current.languageZhHk
        Locales.ZH_TW -> LocalStrings.current.languageZhTw
        Locales.EN -> LocalStrings.current.languageEn
        else -> ""
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
