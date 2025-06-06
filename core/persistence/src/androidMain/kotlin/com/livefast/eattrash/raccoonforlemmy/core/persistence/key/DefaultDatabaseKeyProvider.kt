package com.livefast.eattrash.raccoonforlemmy.core.persistence.key

import android.util.Base64
import com.livefast.eattrash.raccoonforlemmy.core.preferences.store.TemporaryKeyStore
import kotlinx.coroutines.runBlocking
import java.security.SecureRandom

internal class DefaultDatabaseKeyProvider(
    private val keyStore: TemporaryKeyStore,
) : DatabaseKeyProvider {
    override fun getKey(): ByteArray {
        val savedKey = retrieveStoreKey()
        return if (savedKey.isEmpty()) {
            val key = generateKey()
            val keyString = encodeToString(key)
            storeKey(keyString)
            key
        } else {
            val res = decodeFromString(savedKey)
            if (res.isNotEmpty()) {
                res
            } else {
                // regenerates the key
                val key = generateKey()
                val keyString = encodeToString(key)
                storeKey(keyString)
                key
            }
        }
    }

    override fun removeKey() = runBlocking {
        keyStore.remove(DATABASE_KEY)
    }

    private fun retrieveStoreKey(): String = runBlocking {
        keyStore.get(DATABASE_KEY, "")
    }

    private fun storeKey(key: String) = runBlocking {
        keyStore.save(DATABASE_KEY, key)
    }

    private fun generateKey(): ByteArray {
        val key = ByteArray(64)
        SecureRandom().nextBytes(key)
        return key
    }

    private fun encodeToString(key: ByteArray): String = Base64.encodeToString(key, Base64.DEFAULT)

    private fun decodeFromString(key: String): ByteArray = Base64.decode(key, Base64.DEFAULT)

    companion object {
        private const val DATABASE_KEY = "database_key"
    }
}
