package com.livefast.eattrash.raccoonforlemmy.core.navigation

import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class DefaultBottomNavigationAdapter(
    private val navController: NavController,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
) : BottomNavigationAdapter {
    override val currentSection = MutableStateFlow<TabNavigationSection?>(null)

    private val scope = CoroutineScope(SupervisorJob() + dispatcher)

    init {
        navController.currentBackStackEntryFlow.onEach { entry ->
            val destination = entry.destination
            currentSection.update { old ->
                when {
                    destination.hasRoute<TabNavigationSection.Bookmarks>() -> TabNavigationSection.Bookmarks
                    destination.hasRoute<TabNavigationSection.Explore>() -> TabNavigationSection.Explore
                    destination.hasRoute<TabNavigationSection.Home>() -> TabNavigationSection.Home
                    destination.hasRoute<TabNavigationSection.Inbox>() -> TabNavigationSection.Inbox
                    destination.hasRoute<TabNavigationSection.Profile>() -> TabNavigationSection.Profile
                    destination.hasRoute<TabNavigationSection.Settings>() -> TabNavigationSection.Settings
                    else -> TabNavigationSection.Home
                }
            }
        }.launchIn(scope)
    }

    override fun navigate(section: TabNavigationSection) {
        navController.navigate(section)
    }
}
