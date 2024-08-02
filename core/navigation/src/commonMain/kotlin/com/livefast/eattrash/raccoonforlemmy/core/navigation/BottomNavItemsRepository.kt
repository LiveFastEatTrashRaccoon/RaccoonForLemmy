package com.livefast.eattrash.raccoonforlemmy.core.navigation

interface BottomNavItemsRepository {
    suspend fun get(accountId: Long?): List<TabNavigationSection>

    suspend fun update(
        accountId: Long?,
        items: List<TabNavigationSection>,
    )

    companion object {
        val DEFAULT_ITEMS =
            listOf(
                TabNavigationSection.Home,
                TabNavigationSection.Explore,
                TabNavigationSection.Inbox,
                TabNavigationSection.Profile,
            )
    }
}
