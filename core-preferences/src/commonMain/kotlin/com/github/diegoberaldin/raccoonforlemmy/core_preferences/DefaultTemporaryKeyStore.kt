package com.github.diegoberaldin.raccoonforlemmy.core_preferences

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set

internal class DefaultTemporaryKeyStore(
    private val settings: Settings,
) : TemporaryKeyStore {

    override suspend fun containsKey(key: String): Boolean = settings.keys.contains(key)

    override suspend fun save(key: String, value: Boolean) {
        settings[key] = value
    }

    override suspend fun get(key: String, default: Boolean): Boolean = settings[key, default]

    override suspend fun save(key: String, value: String) {
        settings[key] = value
    }

    override suspend fun get(key: String, default: String): String = settings[key, default]

    override suspend fun save(key: String, value: Int) {
        settings[key] = value
    }

    override suspend fun get(key: String, default: Int): Int = settings[key, default]

    override suspend fun save(key: String, value: Float) {
        settings[key] = value
    }

    override suspend fun get(key: String, default: Float): Float = settings[key, default]

    override suspend fun save(key: String, value: Double) {
        settings[key] = value
    }

    override suspend fun get(key: String, default: Double): Double = settings[key, default]
}
