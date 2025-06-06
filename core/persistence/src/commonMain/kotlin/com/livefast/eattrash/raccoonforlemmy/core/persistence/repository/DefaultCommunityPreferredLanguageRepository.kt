package com.livefast.eattrash.raccoonforlemmy.core.persistence.repository

import com.livefast.eattrash.raccoonforlemmy.core.preferences.store.TemporaryKeyStore

private const val SETTINGS_KEY = "communityPreferredLanguage"

internal class DefaultCommunityPreferredLanguageRepository(
    private val keyStore: TemporaryKeyStore,
) : CommunityPreferredLanguageRepository {
    override suspend fun get(handle: String): Long? {
        val map = deserializeMap()
        return map[handle]
    }

    override suspend fun save(
        handle: String,
        value: Long?,
    ) {
        val map = deserializeMap()
        if (value != null) {
            map[handle] = value
        } else {
            map.remove(handle)
        }
        val newValue = serializeMap(map)
        keyStore.save(SETTINGS_KEY, newValue)
    }

    private suspend fun deserializeMap(): MutableMap<String, Long> =
        keyStore
            .get(SETTINGS_KEY, listOf())
            .mapNotNull {
                it.split(":").takeIf { e -> e.size == 2 }?.let { e -> e[0] to e[1] }
            }.let { pairs ->
                val res = mutableMapOf<String, Long>()
                for (pair in pairs) {
                    res[pair.first] = pair.second.toLong()
                }
                res
            }

    private fun serializeMap(map: Map<String, Long>): List<String> =
        map.map { e ->
            e.key + ":" + e.value
        }

    override suspend fun clear() {
        keyStore.remove(SETTINGS_KEY)
    }
}
