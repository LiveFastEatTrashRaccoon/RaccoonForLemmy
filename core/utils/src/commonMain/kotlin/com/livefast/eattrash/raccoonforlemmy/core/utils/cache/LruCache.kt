package com.livefast.eattrash.raccoonforlemmy.core.utils.cache

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class LruCache<K, V>(
    private val capacity: Int,
) {
    private val mutex = Mutex()
    private val keysSortedByLastAccess = mutableListOf<K>()
    private val map = mutableMapOf<K, V>()

    suspend fun containsKey(key: K) =
        mutex.withLock {
            keysSortedByLastAccess.contains(key)
        }

    suspend fun put(
        key: K,
        value: V,
    ) = mutex.withLock {
        if (keysSortedByLastAccess.contains(key)) {
            keysSortedByLastAccess.remove(key)
        } else if (keysSortedByLastAccess.size >= capacity) {
            keysSortedByLastAccess.lastOrNull()?.also { lastKey ->
                keysSortedByLastAccess.remove(lastKey)
                map.remove(lastKey)
            }
        }
        map[key] = value
        keysSortedByLastAccess.add(0, key)
    }

    suspend fun get(key: K): V? =
        mutex.withLock {
            if (keysSortedByLastAccess.contains(key)) {
                keysSortedByLastAccess.remove(key)
                keysSortedByLastAccess.add(0, key)
            }
            return map[key]
        }

    suspend fun remove(key: K) =
        mutex.withLock {
            map.remove(key)
        }

    suspend fun clear() =
        mutex.withLock {
            map.clear()
        }
}
