package com.livefast.eattrash.raccoonforlemmy.core.preferences.appconfig

import com.livefast.eattrash.raccoonforlemmy.core.preferences.store.TemporaryKeyStore

internal class LocalAppConfigDataSource(private val keyStore: TemporaryKeyStore) : AppConfigDataSource {
    override suspend fun get(): AppConfig = AppConfig(
        alternateMarkdownRenderingSettingsItemEnabled =
        read(
            name = "alternateMarkdownRenderingSettingsItemEnabled",
            default = false,
        ),
    )

    override suspend fun update(value: AppConfig) {
        write(
            name = "alternateMarkdownRenderingSettingsItemEnabled",
            value = value.alternateMarkdownRenderingSettingsItemEnabled,
        )
    }

    private suspend fun read(name: String, default: Boolean): Boolean = keyStore.get(getKey(name), default)

    private suspend fun write(name: String, value: Boolean) {
        keyStore.save(getKey(name), value)
    }

    private suspend fun read(name: String, default: String): String = keyStore.get(getKey(name), default)

    private suspend fun write(name: String, value: String) {
        keyStore.save(getKey(name), value)
    }

    private fun getKey(segment: String) = "$PREFIX.$segment"

    companion object {
        private const val PREFIX = "AppConfig"
    }
}
