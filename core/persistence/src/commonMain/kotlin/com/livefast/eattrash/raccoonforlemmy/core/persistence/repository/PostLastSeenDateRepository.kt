package com.livefast.eattrash.raccoonforlemmy.core.persistence.repository

interface PostLastSeenDateRepository {
    suspend fun get(postId: Long): Long?

    suspend fun save(postId: Long, timestamp: Long)

    suspend fun clear()
}
