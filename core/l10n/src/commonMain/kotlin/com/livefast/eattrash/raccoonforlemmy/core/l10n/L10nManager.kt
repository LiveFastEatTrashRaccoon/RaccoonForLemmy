package com.livefast.eattrash.raccoonforlemmy.core.l10n

import cafe.adriel.lyricist.Lyricist
import com.livefast.eattrash.raccoonforlemmy.core.l10n.messages.Strings

interface L10nManager {
    val currentValues: Strings

    val lyricist: Lyricist<Strings>

    fun changeLanguage(lang: String)
}
