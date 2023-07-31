package com.github.diegoberaldin.raccoonforlemmy.feature_home.di

import com.github.diegoberaldin.raccoonforlemmy.feature_home.viewmodel.HomeScreenModel
import org.koin.java.KoinJavaComponent.inject


actual fun getHomeScreenModel(): HomeScreenModel {
    val res: HomeScreenModel by inject(HomeScreenModel::class.java)
    return res
}