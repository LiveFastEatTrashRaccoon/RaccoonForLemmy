package com.livefast.eattrash.raccoonforlemmy.core.persistence.repository

import com.livefast.eattrash.raccoonforlemmy.core.preferences.store.TemporaryKeyStore

private const val CUSTOM_INSTANCES_KEY = "customInstances"
private val DEFAULT_INSTANCES =
    listOf(
        "discuss.tchncs.de",
        "feddit.it",
        "feddit.org",
        "infosec.pub",
        "lemm.ee",
        "lemmy.blahaj.zone",
        "lemmy.cafe",
        "lemmy.dbzer0.com",
        "lemmy.ml",
        "lemmy.sdf.org",
        "lemmy.world",
        "lemmy.zip",
        "mander.xyz",
        "programming.dev",
        "reddthat.com",
        "sh.itjust.works",
        "sopuli.xyz",
    )

internal class DefaultInstanceSelectionRepository(private val keyStore: TemporaryKeyStore) :
    InstanceSelectionRepository {
    override suspend fun getAll(): List<String> {
        val isVersion1 = keyStore.get(CUSTOM_INSTANCES_KEY, listOf()).contains("feddit.de")
        if (!keyStore.containsKey(CUSTOM_INSTANCES_KEY) || isVersion1) {
            keyStore.save(key = CUSTOM_INSTANCES_KEY, value = DEFAULT_INSTANCES)
        }
        return keyStore.get(key = CUSTOM_INSTANCES_KEY, default = DEFAULT_INSTANCES)
    }

    override suspend fun add(value: String) {
        val oldInstances =
            keyStore.get(
                key = CUSTOM_INSTANCES_KEY,
                default = DEFAULT_INSTANCES,
            )
        val instances =
            buildList {
                if (!oldInstances.contains(value)) {
                    add(value)
                }
                addAll(oldInstances)
            }
        keyStore.save(key = CUSTOM_INSTANCES_KEY, value = instances)
    }

    override suspend fun updateAll(values: List<String>) {
        keyStore.save(key = CUSTOM_INSTANCES_KEY, value = values)
    }

    override suspend fun remove(value: String) {
        val instances =
            keyStore.get(
                key = CUSTOM_INSTANCES_KEY,
                default = DEFAULT_INSTANCES,
            ) - value
        keyStore.save(key = CUSTOM_INSTANCES_KEY, value = instances)
    }
}
