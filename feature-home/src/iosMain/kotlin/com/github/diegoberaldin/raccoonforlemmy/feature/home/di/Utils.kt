package com.github.diegoberaldin.raccoonforlemmy.feature.home.di

import com.github.diegoberaldin.raccoonforlemmy.feature.home.postlist.PostListMviModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual fun getHomeScreenModel(): PostListMviModel = HomeScreenModelHelper.model

object HomeScreenModelHelper : KoinComponent {
    val model: PostListMviModel by inject()
}
