package com.livefast.eattrash.raccoonforlemmy.core.navigation.di

import com.livefast.eattrash.raccoonforlemmy.core.navigation.BottomNavItemsRepository
import com.livefast.eattrash.raccoonforlemmy.core.navigation.DrawerCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.navigation.NavigationCoordinator
import org.koin.java.KoinJavaComponent.inject

actual fun getNavigationCoordinator(): NavigationCoordinator {
    val res: NavigationCoordinator by inject(NavigationCoordinator::class.java)
    return res
}

actual fun getDrawerCoordinator(): DrawerCoordinator {
    val res: DrawerCoordinator by inject(DrawerCoordinator::class.java)
    return res
}

actual fun getBottomNavItemsRepository(): BottomNavItemsRepository {
    val res: BottomNavItemsRepository by inject(BottomNavItemsRepository::class.java)
    return res
}
