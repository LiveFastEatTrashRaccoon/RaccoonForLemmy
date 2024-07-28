package com.github.diegoberaldin.raccoonforlemmy.core.persistence.repository

interface StopWordRepository {
    suspend fun get(accountId: Long?): List<String>

    suspend fun update(
        accountId: Long?,
        items: List<String>,
    )
}
