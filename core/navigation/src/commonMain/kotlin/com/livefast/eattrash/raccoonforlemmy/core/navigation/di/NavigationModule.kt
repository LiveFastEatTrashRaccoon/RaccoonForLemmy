package com.livefast.eattrash.raccoonforlemmy.core.navigation.di

import com.livefast.eattrash.raccoonforlemmy.core.navigation.BottomNavItemsRepository
import com.livefast.eattrash.raccoonforlemmy.core.navigation.DefaultBottomNavItemsRepository
import com.livefast.eattrash.raccoonforlemmy.core.navigation.DefaultDrawerCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.navigation.DefaultNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.navigation.DrawerCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.navigation.NavigationCoordinator
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

val navigationModule =
    DI.Module("NavigationModule") {
        bind<DrawerCoordinator> {
            singleton {
                DefaultDrawerCoordinator()
            }
        }
        bind<BottomNavItemsRepository> {
            singleton {
                DefaultBottomNavItemsRepository(keyStore = instance())
            }
        }
        bind<NavigationCoordinator> {
            singleton {
                DefaultNavigationCoordinator()
            }
        }
    }
