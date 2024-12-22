package com.livefast.eattrash.raccoonforlemmy.core.l10n.di

import com.livefast.eattrash.raccoonforlemmy.core.di.RootDI
import com.livefast.eattrash.raccoonforlemmy.core.l10n.L10nManager
import com.livefast.eattrash.raccoonforlemmy.core.l10n.Strings
import org.kodein.di.instance

fun getL10nManager(): L10nManager {
    val res by RootDI.di.instance<L10nManager>()
    return res
}

fun getStrings(lang: String): Strings {
    val res by RootDI.di.instance<String, Strings>(arg = lang)
    return res
}
