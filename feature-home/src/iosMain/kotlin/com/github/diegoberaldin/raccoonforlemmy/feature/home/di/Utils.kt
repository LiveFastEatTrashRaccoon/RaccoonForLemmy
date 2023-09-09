package com.github.diegoberaldin.raccoonforlemmy.feature.home.di

import com.github.diegoberaldin.raccoonforlemmy.feature.home.postlist.BottomNavBarCoordinator
import com.github.diegoberaldin.raccoonforlemmy.feature.home.postlist.PostListViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual fun getHomeScreenModel() = HomeScreenModelHelper.model

actual fun getBottomNavCoordinator() = HomeScreenModelHelper.bottomNavBarCoordinator


object HomeScreenModelHelper : KoinComponent {
    val model: PostListViewModel by inject()
    val bottomNavBarCoordinator: BottomNavBarCoordinator by inject()
}
