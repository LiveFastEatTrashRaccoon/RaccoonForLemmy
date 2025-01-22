package com.livefast.eattrash.raccoonforlemmy.core.persistence.repository

import com.livefast.eattrash.raccoonforlemmy.core.preferences.store.TemporaryKeyStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

internal class DefaultPostLastSeenDateRepository(
    private val keyStore: TemporaryKeyStore,
    private val serializer: LongToLongMapSerializer,
) : PostLastSeenDateRepository {
    override suspend fun get(postId: Long): Long? =
        withContext(Dispatchers.IO) {
            val map =
                keyStore.get(SETTINGS_KEY, listOf()).let {
                    serializer.deserializeMap(it)
                }
            map[postId]
        }

    override suspend fun save(
        postId: Long,
        timestamp: Long,
    ) = withContext(Dispatchers.IO) {
        val map =
            keyStore.get(SETTINGS_KEY, listOf()).let {
                serializer.deserializeMap(it)
            }
        map[postId] = timestamp
        val newValue = serializer.serializeMap(map)
        keyStore.save(SETTINGS_KEY, newValue)
    }

    override suspend fun clear() =
        withContext(Dispatchers.IO) {
            keyStore.remove(SETTINGS_KEY)
        }

    companion object {
        private const val SETTINGS_KEY = "postLastSeenDate"
    }
}
