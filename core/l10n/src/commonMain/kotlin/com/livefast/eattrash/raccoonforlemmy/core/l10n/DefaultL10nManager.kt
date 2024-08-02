package com.livefast.eattrash.raccoonforlemmy.core.l10n

import cafe.adriel.lyricist.Lyricist
import com.livefast.eattrash.raccoonforlemmy.core.l10n.messages.Locales
import com.livefast.eattrash.raccoonforlemmy.core.l10n.messages.Strings
import com.livefast.eattrash.raccoonforlemmy.core.l10n.messages.localizableStrings

internal class DefaultL10nManager : L10nManager {
    override val lyricist: Lyricist<Strings> =
        Lyricist(
            defaultLanguageTag = Locales.EN,
            translations = localizableStrings,
        )

    override fun changeLanguage(lang: String) {
        lyricist.languageTag = lang
    }
}
