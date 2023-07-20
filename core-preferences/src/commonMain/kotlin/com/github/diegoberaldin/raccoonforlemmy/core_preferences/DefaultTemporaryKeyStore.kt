package com.github.diegoberaldin.raccoonforlemmy.core_preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withTimeoutOrNull
import okio.Path.Companion.toPath

internal expect fun getKeyStoreFilePath(): String

internal class DefaultTemporaryKeyStore : TemporaryKeyStore {

    companion object {
        internal const val FILE_NAME = "main.preferences_pb"
    }

    private val scope = CoroutineScope(Dispatchers.Default)
    private val dataStore: DataStore<Preferences> = createDataStore()

    private fun createDataStore(): DataStore<Preferences> =
        PreferenceDataStoreFactory.createWithPath(
            corruptionHandler = null,
            migrations = emptyList(),
            scope = scope,
            produceFile = { getKeyStoreFilePath().toPath() }
        )

    override suspend fun containsKey(key: String): Boolean = withTimeoutOrNull(500) {
        dataStore.data.map {
            it.asMap().keys.any { k -> k.name == key }
        }.first()
    } ?: false

    override suspend fun save(key: String, value: Boolean) {
        dataStore.edit {
            it[booleanPreferencesKey(key)] = value
        }
    }

    override suspend fun get(key: String, default: Boolean) = withTimeoutOrNull(500) {
        dataStore.data.map {
            it[booleanPreferencesKey(key)]
        }.first()
    } ?: default

    override suspend fun save(key: String, value: String) {
        dataStore.edit {
            it[stringPreferencesKey(key)] = value
        }
    }

    override suspend fun get(key: String, default: String): String = withTimeoutOrNull(500) {
        dataStore.data.map {
            it[stringPreferencesKey(key)]
        }.first()
    } ?: default

    override suspend fun save(key: String, value: Int) {
        dataStore.edit {
            it[intPreferencesKey(key)] = value
        }
    }

    override suspend fun get(key: String, default: Int): Int = withTimeoutOrNull(500) {
        dataStore.data.map {
            it[intPreferencesKey(key)]
        }.first()
    } ?: default

    override suspend fun save(key: String, value: Float) {
        dataStore.edit {
            it[floatPreferencesKey(key)] = value
        }
    }

    override suspend fun get(key: String, default: Float): Float = withTimeoutOrNull(500) {
        dataStore.data.map {
            it[floatPreferencesKey(key)]
        }.first()
    } ?: default

    override suspend fun save(key: String, value: Double) {
        dataStore.edit {
            it[doublePreferencesKey(key)] = value
        }
    }

    override suspend fun get(key: String, default: Double): Double = withTimeoutOrNull(500) {
        dataStore.data.map {
            it[doublePreferencesKey(key)]
        }.first()
    } ?: default
}
