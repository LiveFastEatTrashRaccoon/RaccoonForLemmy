package com.livefast.eattrash.raccoonforlemmy.core.navigation

import androidx.navigation.NavController

interface NavigationAdapter {
    val canPop: Boolean

    fun navigate(destination: Destination, replaceTop: Boolean = false)

    fun pop()

    fun popUntilRoot()
}

class DefaultNavigationAdapter(private val navController: NavController) : NavigationAdapter {
    override val canPop: Boolean get() = navController.currentBackStack.value.size > 1

    override fun navigate(destination: Destination, replaceTop: Boolean) {
        if (replaceTop && canPop) {
            navController.popBackStack()
        }
        navController.navigate(destination)
    }

    override fun pop() {
        if (!canPop) {
            return
        }
        navController.popBackStack()
    }

    override fun popUntilRoot() {
        navController.popBackStack(route = Destination.Main, inclusive = false)
    }
}
