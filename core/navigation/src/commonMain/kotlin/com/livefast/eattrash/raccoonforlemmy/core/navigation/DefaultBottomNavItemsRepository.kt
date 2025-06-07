package com.livefast.eattrash.raccoonforlemmy.core.navigation

import com.livefast.eattrash.raccoonforlemmy.core.preferences.store.TemporaryKeyStore

internal class DefaultBottomNavItemsRepository(private val keyStore: TemporaryKeyStore) : BottomNavItemsRepository {
    override suspend fun get(accountId: Long?): List<TabNavigationSection> {
        val key = getKey(accountId)
        val itemIds = keyStore.get(key, emptyList()).mapNotNull { it.toIntOrNull() }
        val res = itemIds.mapNotNull { it.toTabNavigationSection() }.takeUnless { it.isEmpty() }
        return res ?: BottomNavItemsRepository.DEFAULT_ITEMS
    }

    override suspend fun update(accountId: Long?, items: List<TabNavigationSection>) {
        val key = getKey(accountId)
        val itemIds = items.map { it.toInt().toString() }
        keyStore.save(key, itemIds)
    }

    private fun getKey(accountId: Long?): String = buildString {
        append("BottomNavItemsRepository")
        if (accountId != null) {
            append(".")
            append(accountId)
        }
        append(".items")
    }
}
