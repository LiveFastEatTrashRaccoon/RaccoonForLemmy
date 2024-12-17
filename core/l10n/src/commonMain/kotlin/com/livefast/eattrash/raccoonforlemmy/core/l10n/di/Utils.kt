package com.livefast.eattrash.raccoonforlemmy.core.l10n.di

import com.livefast.eattrash.raccoonforlemmy.core.l10n.L10nManager
import com.livefast.eattrash.raccoonforlemmy.core.l10n.Strings

expect fun getL10nManager(): L10nManager

internal expect fun replaceLang(lang: String)

expect fun getStrings(lang: String): Strings
