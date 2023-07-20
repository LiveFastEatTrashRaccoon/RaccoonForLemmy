package com.github.diegoberaldin.raccoonforlemmy.feature_search

import org.koin.java.KoinJavaComponent.inject

actual fun getSearchScreenModel(): SearchScreenModel {
    val res: SearchScreenModel by inject(SearchScreenModel::class.java)
    return res
}