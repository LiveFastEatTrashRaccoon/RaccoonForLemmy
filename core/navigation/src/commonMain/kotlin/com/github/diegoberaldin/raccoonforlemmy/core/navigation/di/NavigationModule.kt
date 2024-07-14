package com.github.diegoberaldin.raccoonforlemmy.core.navigation.di

import com.github.diegoberaldin.raccoonforlemmy.core.navigation.BottomNavItemsRepository
import com.github.diegoberaldin.raccoonforlemmy.core.navigation.DefaultBottomNavItemsRepository
import com.github.diegoberaldin.raccoonforlemmy.core.navigation.DefaultDrawerCoordinator
import com.github.diegoberaldin.raccoonforlemmy.core.navigation.DefaultNavigationCoordinator
import com.github.diegoberaldin.raccoonforlemmy.core.navigation.DrawerCoordinator
import com.github.diegoberaldin.raccoonforlemmy.core.navigation.NavigationCoordinator
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
