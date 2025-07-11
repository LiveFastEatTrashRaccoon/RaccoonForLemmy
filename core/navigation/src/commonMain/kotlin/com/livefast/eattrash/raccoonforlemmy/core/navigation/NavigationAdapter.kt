package com.livefast.eattrash.raccoonforlemmy.core.navigation

interface NavigationAdapter {
    val canPop: Boolean

    fun navigate(destination: Destination, replaceTop: Boolean = false)

    fun pop()

    fun popUntilRoot()
}
