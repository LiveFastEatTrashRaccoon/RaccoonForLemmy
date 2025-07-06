package com.livefast.eattrash.raccoonforlemmy.core.navigation

import androidx.navigation.NavController

interface BottomNavigationAdapter {
    fun navigate(section: TabNavigationSection)
}

class DefaultBottomNavigationAdapter(private val navController: NavController) : BottomNavigationAdapter {
    override fun navigate(section: TabNavigationSection) {
        navController.navigate(section)
    }
}
