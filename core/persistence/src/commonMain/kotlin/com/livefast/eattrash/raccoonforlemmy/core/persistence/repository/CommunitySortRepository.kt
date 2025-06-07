package com.livefast.eattrash.raccoonforlemmy.core.persistence.repository

interface CommunitySortRepository {
    suspend fun get(handle: String): Int?

    suspend fun save(handle: String, value: Int)

    suspend fun clear()
}
