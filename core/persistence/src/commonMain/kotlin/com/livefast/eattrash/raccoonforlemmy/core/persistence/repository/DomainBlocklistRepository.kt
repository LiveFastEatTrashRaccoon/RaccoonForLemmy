package com.livefast.eattrash.raccoonforlemmy.core.persistence.repository

interface DomainBlocklistRepository {
    suspend fun get(accountId: Long?): List<String>

    suspend fun update(
        accountId: Long?,
        items: List<String>,
    )
}
