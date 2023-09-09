package com.github.diegoberaldin.raccoonforlemmy.feature.home.di

import com.github.diegoberaldin.raccoonforlemmy.feature.home.postlist.BottomNavBarCoordinator
import com.github.diegoberaldin.raccoonforlemmy.feature.home.postlist.PostListViewModel
import org.koin.java.KoinJavaComponent.inject

actual fun getHomeScreenModel(): PostListViewModel {
    val res: PostListViewModel by inject(PostListViewModel::class.java)
    return res
}

actual fun getBottomNavCoordinator(): BottomNavBarCoordinator {
    val res: BottomNavBarCoordinator by inject(BottomNavBarCoordinator::class.java)
    return res
}
