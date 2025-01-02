package com.livefast.eattrash.raccoonforlemmy.core.persistence.repository

import com.livefast.eattrash.raccoonforlemmy.core.preferences.store.TemporaryKeyStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

internal class DefaultCommunitySortRepository(
    private val keyStore: TemporaryKeyStore,
    private val serializer: SortSerializer,
) : CommunitySortRepository {
    override suspend fun get(handle: String): Int? =
        withContext(Dispatchers.IO) {
            val map =
                keyStore.get(SETTINGS_KEY, listOf()).let {
                    serializer.deserializeMap(it)
                }
            map[handle]
        }

    override suspend fun save(
        handle: String,
        value: Int,
    ) = withContext(Dispatchers.IO) {
        val map =
            keyStore.get(SETTINGS_KEY, listOf()).let {
                serializer.deserializeMap(it)
            }
        map[handle] = value
        val newValue = serializer.serializeMap(map)
        keyStore.save(SETTINGS_KEY, newValue)
    }

    override suspend fun clear() =
        withContext(Dispatchers.IO) {
            keyStore.remove(SETTINGS_KEY)
        }

    companion object {
        private const val SETTINGS_KEY = "communitySort"
    }
}
