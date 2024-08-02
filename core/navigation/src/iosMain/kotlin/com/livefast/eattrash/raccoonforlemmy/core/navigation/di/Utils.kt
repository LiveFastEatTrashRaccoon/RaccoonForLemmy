package com.livefast.eattrash.raccoonforlemmy.core.navigation.di

import com.livefast.eattrash.raccoonforlemmy.core.navigation.BottomNavItemsRepository
import com.livefast.eattrash.raccoonforlemmy.core.navigation.DrawerCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.navigation.NavigationCoordinator
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual fun getNavigationCoordinator() = CoreNavigationHelper.navigationCoordinator

actual fun getDrawerCoordinator() = CoreNavigationHelper.drawerCoordinator

actual fun getBottomNavItemsRepository() = CoreNavigationHelper.bottomNavItemsRepository

object CoreNavigationHelper : KoinComponent {
    val navigationCoordinator: NavigationCoordinator by inject()
    val drawerCoordinator: DrawerCoordinator by inject()
    val bottomNavItemsRepository: BottomNavItemsRepository by inject()
}
