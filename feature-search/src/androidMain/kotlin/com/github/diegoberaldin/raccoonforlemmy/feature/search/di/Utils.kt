package com.github.diegoberaldin.raccoonforlemmy.feature.search.di

import com.github.diegoberaldin.raccoonforlemmy.feature.search.viewmodel.SearchScreenModel
import org.koin.java.KoinJavaComponent.inject

actual fun getSearchScreenModel(): SearchScreenModel {
    val res: SearchScreenModel by inject(SearchScreenModel::class.java)
    return res
}
