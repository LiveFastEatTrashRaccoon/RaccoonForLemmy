package com.livefast.eattrash.raccoonforlemmy.core.l10n.di

import com.livefast.eattrash.raccoonforlemmy.core.l10n.L10nManager
import com.livefast.eattrash.raccoonforlemmy.core.l10n.Strings
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import platform.Foundation.NSUserDefaults

actual fun getL10nManager(): L10nManager = CoreL10nKoinHelper.l10nManager

internal actual fun replaceLang(lang: String) {
    NSUserDefaults.standardUserDefaults.setObject(arrayListOf(lang), "AppleLanguages")
}

actual fun getStrings(lang: String): Strings = CoreL10nKoinHelper.strings

internal object CoreL10nKoinHelper : KoinComponent {
    val l10nManager: L10nManager by inject()
    val strings: Strings by inject()
}
