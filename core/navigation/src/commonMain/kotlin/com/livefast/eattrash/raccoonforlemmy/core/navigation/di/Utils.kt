package com.livefast.eattrash.raccoonforlemmy.core.navigation.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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

@Composable
fun rememberNavigationCoordinator(): NavigationCoordinator = remember { getNavigationCoordinator() }

fun getDrawerCoordinator(): DrawerCoordinator {
    val res by RootDI.di.instance<DrawerCoordinator>()
    return res
}

@Composable
fun rememberDrawerCoordinator(): DrawerCoordinator = remember { getDrawerCoordinator() }

fun getBottomNavItemsRepository(): BottomNavItemsRepository {
    val res by RootDI.di.instance<BottomNavItemsRepository>()
    return res
}

@Composable
fun rememberBottomNavItemsRepository(): BottomNavItemsRepository = remember { getBottomNavItemsRepository() }

fun getMainRouter(): MainRouter {
    val res by RootDI.di.instance<MainRouter>()
    return res
}

@Composable
fun rememberMainRouter(): MainRouter = remember { getMainRouter() }
