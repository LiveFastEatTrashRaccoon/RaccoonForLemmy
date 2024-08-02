package com.livefast.eattrash.raccoonforlemmy.core.navigation.di

import com.livefast.eattrash.raccoonforlemmy.core.navigation.BottomNavItemsRepository
import com.livefast.eattrash.raccoonforlemmy.core.navigation.DrawerCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.navigation.NavigationCoordinator

expect fun getNavigationCoordinator(): NavigationCoordinator

expect fun getDrawerCoordinator(): DrawerCoordinator

expect fun getBottomNavItemsRepository(): BottomNavItemsRepository
