package com.github.diegoberaldin.raccoonforlemmy.home

import org.koin.java.KoinJavaComponent.inject

actual fun getHomeScreenModel(): HomeScreenModel {
    val res: HomeScreenModel by inject(HomeScreenModel::class.java)
    return res
}