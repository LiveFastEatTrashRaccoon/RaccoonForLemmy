package com.livefast.eattrash.raccoonforlemmy.core.l10n

import kotlinx.coroutines.flow.StateFlow

interface L10nManager {
    val lang: StateFlow<String>

    fun changeLanguage(lang: String)
}
