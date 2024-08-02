package com.livefast.eattrash.raccoonforlemmy.core.navigation.di

import com.livefast.eattrash.raccoonforlemmy.core.navigation.BottomNavItemsRepository
import com.livefast.eattrash.raccoonforlemmy.core.navigation.DefaultBottomNavItemsRepository
import com.livefast.eattrash.raccoonforlemmy.core.navigation.DefaultDrawerCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.navigation.DefaultNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.navigation.DrawerCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.navigation.NavigationCoordinator
import org.koin.dsl.module

val coreNavigationModule =
    module {
        single<NavigationCoordinator> {
            DefaultNavigationCoordinator()
        }
        single<DrawerCoordinator> {
            DefaultDrawerCoordinator()
        }
        single<BottomNavItemsRepository> {
            DefaultBottomNavItemsRepository(
                keyStore = get(),
            )
        }
    }
