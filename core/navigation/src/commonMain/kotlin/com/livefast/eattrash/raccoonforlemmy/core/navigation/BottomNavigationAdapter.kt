package com.livefast.eattrash.raccoonforlemmy.core.navigation

import kotlinx.coroutines.flow.StateFlow

interface BottomNavigationAdapter {
    val currentSection: StateFlow<TabNavigationSection?>
    fun navigate(section: TabNavigationSection)
}
