package com.github.diegoberaldin.raccoonforlemmy.resources

import kotlinx.coroutines.flow.StateFlow

interface LanguageRepository {

    val currentLanguage: StateFlow<String>

    fun changeLanguage(lang: String)
}
