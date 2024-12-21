package com.livefast.eattrash.raccoonforlemmy.core.l10n

import platform.Foundation.NSUserDefaults

internal actual fun replaceLang(lang: String) {
    NSUserDefaults.standardUserDefaults.setObject(arrayListOf(lang), "AppleLanguages")
}
