package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository

import com.livefast.eattrash.raccoonforlemmy.core.utils.cache.LruCache

internal class DefaultLocalItemCache<T> : LocalItemCache<T> {
    private val cache = LruCache<Long, T>(MAX_SIZE)

    override suspend fun put(
        key: Long,
        value: T,
    ) {
        cache.put(key, value)
    }

    override suspend fun get(key: Long): T? = cache.get(key)

    override suspend fun remove(key: Long) {
        cache.remove(key)
    }

    override suspend fun clear() {
        cache.clear()
    }

    companion object {
        private const val MAX_SIZE = 10
    }
}
