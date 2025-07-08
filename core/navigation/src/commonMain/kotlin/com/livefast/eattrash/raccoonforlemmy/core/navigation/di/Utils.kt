package com.livefast.eattrash.raccoonforlemmy.core.navigation.di

import com.livefast.eattrash.raccoonforlemmy.core.di.RootDI
import com.livefast.eattrash.raccoonforlemmy.core.navigation.BottomNavItemsRepository
import com.livefast.eattrash.raccoonforlemmy.core.navigation.DrawerCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.navigation.MainRouter
import com.livefast.eattrash.raccoonforlemmy.core.navigation.NavigationCoordinator
import org.kodein.di.instance

fun getNavigationCoordinator(): NavigationCoordinator {
    val res by RootDI.di.instance<NavigationCoordinator>()
    return res
}

fun getDrawerCoordinator(): DrawerCoordinator {
    val res by RootDI.di.instance<DrawerCoordinator>()
    return res
}

fun getBottomNavItemsRepository(): BottomNavItemsRepository {
    val res by RootDI.di.instance<BottomNavItemsRepository>()
    return res
}

fun getMainRouter(): MainRouter {
    val res by RootDI.di.instance<MainRouter>()
    return res
}
