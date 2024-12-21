package com.livefast.eattrash.raccoonforlemmy.core.persistence.repository

import com.livefast.eattrash.raccoonforlemmy.core.preferences.store.TemporaryKeyStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

internal class DefaultDomainBlocklistRepository(
    private val keyStore: TemporaryKeyStore,
) : DomainBlocklistRepository {
    override suspend fun get(accountId: Long?): List<String> =
        withContext(Dispatchers.IO) {
            val key = getKey(accountId)
            val res = keyStore.get(key, emptyList())
            res.filter { it.isNotEmpty() }
        }

    override suspend fun update(
        accountId: Long?,
        items: List<String>,
    ) = withContext(Dispatchers.IO) {
        val key = getKey(accountId)
        keyStore.save(key, items)
    }

    private fun getKey(accountId: Long?): String =
        buildString {
            append("DomainBlocklistRepository")
            if (accountId != null) {
                append(".")
                append(accountId)
            }
            append(".items")
        }
}
