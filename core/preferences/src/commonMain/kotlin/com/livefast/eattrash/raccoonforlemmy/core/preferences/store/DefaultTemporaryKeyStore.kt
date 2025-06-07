package com.livefast.eattrash.raccoonforlemmy.core.preferences.store

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

internal class DefaultTemporaryKeyStore(private val settings: Settings) : TemporaryKeyStore {
    override suspend fun containsKey(key: String): Boolean = settings.keys.contains(key)

    override suspend fun save(key: String, value: Boolean) {
        withContext(Dispatchers.IO) {
            settings[key] = value
        }
    }

    override suspend fun get(key: String, default: Boolean): Boolean = withContext(Dispatchers.IO) {
        settings[key, default]
    }

    override suspend fun save(key: String, value: String) {
        withContext(Dispatchers.IO) {
            settings[key] = value
        }
    }

    override suspend fun get(key: String, default: String): String = withContext(Dispatchers.IO) {
        settings[key, default]
    }

    override suspend fun save(key: String, value: Int) {
        withContext(Dispatchers.IO) {
            settings[key] = value
        }
    }

    override suspend fun get(key: String, default: Int): Int = withContext(Dispatchers.IO) {
        settings[key, default]
    }

    override suspend fun save(key: String, value: Float) {
        withContext(Dispatchers.IO) {
            settings[key] = value
        }
    }

    override suspend fun get(key: String, default: Float): Float = withContext(Dispatchers.IO) {
        settings[key, default]
    }

    override suspend fun save(key: String, value: Double) {
        withContext(Dispatchers.IO) {
            settings[key] = value
        }
    }

    override suspend fun get(key: String, default: Double): Double = withContext(Dispatchers.IO) {
        settings[key, default]
    }

    override suspend fun save(key: String, value: Long) {
        withContext(Dispatchers.IO) {
            settings[key] = value
        }
    }

    override suspend fun get(key: String, default: Long): Long = withContext(Dispatchers.IO) {
        settings[key, default]
    }

    override suspend fun get(key: String, default: List<String>, delimiter: String): List<String> =
        withContext(Dispatchers.IO) {
            if (!settings.hasKey(key)) {
                return@withContext default
            }
            val joined = settings[key, ""]
            joined.split(delimiter)
        }

    override suspend fun save(key: String, value: List<String>, delimiter: String) {
        withContext(Dispatchers.IO) {
            settings[key] = value.joinToString(delimiter)
        }
    }

    override suspend fun remove(key: String) {
        withContext(Dispatchers.IO) {
            settings.remove(key)
        }
    }

    override suspend fun removeAll() {
        withContext(Dispatchers.IO) {
            settings.clear()
        }
    }
}
