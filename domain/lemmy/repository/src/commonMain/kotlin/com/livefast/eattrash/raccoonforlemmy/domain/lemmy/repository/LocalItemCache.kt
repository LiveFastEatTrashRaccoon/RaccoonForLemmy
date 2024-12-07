package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository

interface LocalItemCache<T> {
    suspend fun put(
        key: Long,
        value: T,
    )

    suspend fun get(key: Long): T?

    suspend fun remove(key: Long)

    suspend fun clear()
}
