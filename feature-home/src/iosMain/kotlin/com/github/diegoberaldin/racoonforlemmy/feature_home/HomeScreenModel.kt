package com.github.diegoberaldin.racoonforlemmy.feature_home

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual fun getHomeScreenModel() = HomeScreenModelHelper().model

class HomeScreenModelHelper : KoinComponent {
    val model: HomeScreenModel by inject()
}