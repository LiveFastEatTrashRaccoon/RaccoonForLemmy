package com.github.diegoberaldin.raccoonforlemmy.resources

import kotlinx.coroutines.flow.MutableStateFlow

class DefaultLanguageRepository : LanguageRepository {
    override val currentLanguage = MutableStateFlow("")

    override fun changeLanguage(lang: String) {
        currentLanguage.value = lang
    }
}