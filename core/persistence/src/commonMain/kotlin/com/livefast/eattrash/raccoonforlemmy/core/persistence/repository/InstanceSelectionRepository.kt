package com.livefast.eattrash.raccoonforlemmy.core.persistence.repository

interface InstanceSelectionRepository {
    suspend fun getAll(): List<String>

    suspend fun add(value: String)

    suspend fun remove(value: String)

    suspend fun updateAll(values: List<String>)
}
