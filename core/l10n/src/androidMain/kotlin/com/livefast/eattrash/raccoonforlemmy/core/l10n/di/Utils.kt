package com.livefast.eattrash.raccoonforlemmy.core.l10n.di

import com.livefast.eattrash.raccoonforlemmy.core.l10n.L10nManager
import com.livefast.eattrash.raccoonforlemmy.core.l10n.messages.Locales
import com.livefast.eattrash.raccoonforlemmy.core.l10n.messages.Strings
import org.koin.java.KoinJavaComponent
import java.util.Locale

actual fun getL10nManager(): L10nManager {
    val res: L10nManager by KoinJavaComponent.inject(L10nManager::class.java)
    return res
}

internal actual fun replaceLang(lang: String) {
    val tokens = lang.split("_")
    val country = tokens.getOrNull(1).orEmpty()
    val langCode = tokens.firstOrNull() ?: Locales.EN
    Locale.setDefault(Locale(langCode, country))
}

actual fun getStrings(lang: String): Strings {
    val res: Strings by KoinJavaComponent.inject(Strings::class.java)
    return res
}
