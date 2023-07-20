package com.github.diegoberaldin.racoonforlemmy.feature_home

import org.koin.java.KoinJavaComponent.inject

actual fun getHomeScreenModel(): HomeScreenModel {
    val res: HomeScreenModel by inject(HomeScreenModel::class.java)
    return res
}