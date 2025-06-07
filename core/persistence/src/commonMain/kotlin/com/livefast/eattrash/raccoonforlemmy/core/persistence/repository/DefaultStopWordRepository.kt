package com.livefast.eattrash.raccoonforlemmy.core.persistence.repository

import com.livefast.eattrash.raccoonforlemmy.core.preferences.store.TemporaryKeyStore

internal class DefaultStopWordRepository(private val keyStore: TemporaryKeyStore) : StopWordRepository {
    override suspend fun get(accountId: Long?): List<String> {
        val key = getKey(accountId)
        val res = keyStore.get(key, emptyList())
        return res.filter { it.isNotEmpty() }
    }

    override suspend fun update(accountId: Long?, items: List<String>) {
        val key = getKey(accountId)
        keyStore.save(key, items)
    }

    private fun getKey(accountId: Long?): String = buildString {
        append("StopWordRepository")
        if (accountId != null) {
            append(".")
            append(accountId)
        }
        append(".items")
    }
}
