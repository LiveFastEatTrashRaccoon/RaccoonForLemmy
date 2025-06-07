package com.livefast.eattrash.raccoonforlemmy.core.persistence.repository

interface UserSortRepository {
    suspend fun getForPosts(handle: String): Int?

    suspend fun getForComments(handle: String): Int?

    suspend fun saveForPosts(handle: String, value: Int)

    suspend fun saveForComments(handle: String, value: Int)

    suspend fun clear()
}
