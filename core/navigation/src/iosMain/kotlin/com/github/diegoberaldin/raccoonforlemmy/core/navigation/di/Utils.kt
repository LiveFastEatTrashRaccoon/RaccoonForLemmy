package com.github.diegoberaldin.raccoonforlemmy.core.navigation.di

import com.github.diegoberaldin.raccoonforlemmy.core.navigation.BottomNavItemsRepository
import com.github.diegoberaldin.raccoonforlemmy.core.navigation.DrawerCoordinator
import com.github.diegoberaldin.raccoonforlemmy.core.navigation.NavigationCoordinator
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
