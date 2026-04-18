package com.livefast.eattrash.raccoonforlemmy.core.navigation

import kotlinx.coroutines.flow.StateFlow

interface NavigationAdapter {
    val canPop: StateFlow<Boolean>

    fun navigate(destination: Destination, replaceTop: Boolean = false)

    fun pop()

    fun popUntilRoot()
}
