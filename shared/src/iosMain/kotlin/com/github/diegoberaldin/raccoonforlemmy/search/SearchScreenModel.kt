package com.github.diegoberaldin.raccoonforlemmy.search

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual fun getSearchScreenModel() = SearchScreenModelHelper().model

class SearchScreenModelHelper : KoinComponent {
    val model: SearchScreenModel by inject()
}