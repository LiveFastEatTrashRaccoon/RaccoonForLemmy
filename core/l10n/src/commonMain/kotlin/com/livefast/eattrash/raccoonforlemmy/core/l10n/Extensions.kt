package com.livefast.eattrash.raccoonforlemmy.core.l10n

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.LayoutDirection

@Composable
fun String.toLanguageName() =
    when (this) {
        Locales.DE -> LocalStrings.current.languageDe
        Locales.EN -> LocalStrings.current.languageEn
        Locales.ES -> LocalStrings.current.languageEs
        Locales.FI -> LocalStrings.current.languageFi
        Locales.FR -> LocalStrings.current.languageFr
        Locales.GA -> LocalStrings.current.languageGa
        Locales.IT -> LocalStrings.current.languageIt
        Locales.LV -> LocalStrings.current.languageLv
        Locales.PL -> LocalStrings.current.languagePl
        Locales.PT -> LocalStrings.current.languagePt
        Locales.PT_BR -> LocalStrings.current.languagePtBr
        Locales.TA -> LocalStrings.current.languageTa
        Locales.UK -> LocalStrings.current.languageUk
        Locales.ZH_CN -> LocalStrings.current.languageZhCn
        Locales.ZH_HK -> LocalStrings.current.languageZhHk
        Locales.ZH_TW -> LocalStrings.current.languageZhTw
        else -> ""
    }

@Composable
fun String.toLanguageFlag(): String =
    when (this) {
        Locales.DE -> "🇩🇪"
        Locales.EN -> "🇬🇧"
        Locales.ES -> "🇪🇸"
        Locales.FI -> "🇫🇮"
        Locales.FR -> "🇫🇷"
        Locales.GA -> "🇮🇪"
        Locales.IT -> "🇮🇹"
        Locales.LV -> "🇱🇻"
        Locales.PL -> "🇵🇱"
        Locales.PT -> "🇵🇹"
        Locales.PT_BR -> "🇧🇷"
        Locales.TA -> "🇮🇳"
        Locales.UK -> "🇺🇦"
        Locales.ZH_CN -> "🇨🇳"
        Locales.ZH_HK -> "🇭🇰"
        Locales.ZH_TW -> "🇹🇼"
        else -> ""
    }

internal fun String.toLanguageDirection(): LayoutDirection =
    when (this) {
        "ar" -> LayoutDirection.Rtl
        else -> LayoutDirection.Ltr
    }
