package com.github.diegoberaldin.raccoonforlemmy.core.preferences.appconfig

import com.github.diegoberaldin.raccoonforlemmy.core.preferences.TemporaryKeyStore

internal class LocalAppConfigDataSource(
    private val keyStore: TemporaryKeyStore,
) : AppConfigDataSource {
    override suspend fun get(): AppConfig =
        AppConfig(
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

    private fun read(
        name: String,
        default: Boolean,
    ): Boolean = keyStore[getKey(name), default]

    private fun write(
        name: String,
        value: Boolean,
    ) {
        keyStore.save(getKey(name), value)
    }

    private fun read(
        name: String,
        default: String,
    ): String = keyStore[getKey(name), default]

    private fun write(
        name: String,
        value: String,
    ) {
        keyStore.save(getKey(name), value)
    }

    private fun getKey(segment: String) = "$PREFIX.$segment"

    companion object {
        private const val PREFIX = "AppConfig"
    }
}
