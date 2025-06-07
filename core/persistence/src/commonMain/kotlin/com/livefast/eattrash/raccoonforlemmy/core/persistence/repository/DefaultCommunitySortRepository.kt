package com.livefast.eattrash.raccoonforlemmy.core.persistence.repository

import com.livefast.eattrash.raccoonforlemmy.core.preferences.store.TemporaryKeyStore

internal class DefaultCommunitySortRepository(
    private val keyStore: TemporaryKeyStore,
    private val serializer: SortSerializer,
) : CommunitySortRepository {
    override suspend fun get(handle: String): Int? {
        val map =
            keyStore.get(SETTINGS_KEY, listOf()).let {
                serializer.deserializeMap(it)
            }
        return map[handle]
    }

    override suspend fun save(handle: String, value: Int) {
        val map =
            keyStore.get(SETTINGS_KEY, listOf()).let {
                serializer.deserializeMap(it)
            }
        map[handle] = value
        val newValue = serializer.serializeMap(map)
        keyStore.save(SETTINGS_KEY, newValue)
    }

    override suspend fun clear() {
        keyStore.remove(SETTINGS_KEY)
    }

    companion object {
        private const val SETTINGS_KEY = "communitySort"
    }
}
