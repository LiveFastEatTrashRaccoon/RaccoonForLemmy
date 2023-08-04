package com.github.diegoberaldin.raccoonforlemmy.feature.search.di

import com.github.diegoberaldin.raccoonforlemmy.feature.search.viewmodel.SearchScreenModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual fun getSearchScreenModel() = SearchScreenModelHelper.model

object SearchScreenModelHelper : KoinComponent {
    val model: SearchScreenModel by inject()
}
