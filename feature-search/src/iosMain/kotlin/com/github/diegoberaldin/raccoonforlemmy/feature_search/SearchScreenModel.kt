package com.github.diegoberaldin.raccoonforlemmy.feature_search

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual fun getSearchScreenModel() = SearchScreenModelHelper.model

object SearchScreenModelHelper : KoinComponent {
    val model: SearchScreenModel by inject()
}