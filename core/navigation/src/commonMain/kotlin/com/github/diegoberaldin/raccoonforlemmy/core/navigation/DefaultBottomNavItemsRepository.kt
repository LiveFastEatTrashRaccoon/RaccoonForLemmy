package com.github.diegoberaldin.raccoonforlemmy.core.navigation

import com.github.diegoberaldin.raccoonforlemmy.core.preferences.TemporaryKeyStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

internal class DefaultBottomNavItemsRepository(
    private val keyStore: TemporaryKeyStore,
) : BottomNavItemsRepository {
    override suspend fun get(accountId: Long?): List<TabNavigationSection> =
        withContext(Dispatchers.IO) {
            val key = getKey(accountId)
            val itemIds = keyStore.get(key, emptyList())
            val res = itemIds.mapNotNull { it.toTabNavigationSection() }.takeUnless { it.isEmpty() }
            res ?: BottomNavItemsRepository.DEFAULT_ITEMS
        }

    override suspend fun update(
        accountId: Long?,
        items: List<TabNavigationSection>,
    ) = withContext(Dispatchers.IO) {
        val key = getKey(accountId)
        val itemIds = items.map { it.toTabNavigationId() }
        keyStore.save(key, itemIds)
    }

    private fun getKey(accountId: Long?): String =
        buildString {
            append("BottomNavItemsRepository")
            if (accountId != null) {
                append(".")
                append(accountId)
            }
            append(".items")
        }
}

private fun String.toTabNavigationSection(): TabNavigationSection? =
    when (this) {
        "0" -> TabNavigationSection.Home
        "1" -> TabNavigationSection.Explore
        "2" -> TabNavigationSection.Inbox
        "3" -> TabNavigationSection.Profile
        "4" -> TabNavigationSection.Settings
        "5" -> TabNavigationSection.Bookmarks
        else -> null
    }

private fun TabNavigationSection.toTabNavigationId(): String =
    when (this) {
        TabNavigationSection.Home -> "0"
        TabNavigationSection.Explore -> "1"
        TabNavigationSection.Inbox -> "2"
        TabNavigationSection.Profile -> "3"
        TabNavigationSection.Settings -> "4"
        TabNavigationSection.Bookmarks -> "5"
    }
