package com.livefast.eattrash.raccoonforlemmy.core.l10n

import com.livefast.eattrash.raccoonforlemmy.core.l10n.di.replaceLang
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.Single

@Single
internal class DefaultL10nManager : L10nManager {
    override val lang = MutableStateFlow(Locales.EN)

    override fun changeLanguage(lang: String) {
        replaceLang(lang)
        this.lang.update { lang }
    }
}
