package com.livefast.eattrash.raccoonforlemmy.core.preferences.appconfig

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.request
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

internal class RemoteAppConfigDataSource(engine: HttpClientEngine, private val json: Json) : AppConfigDataSource {
    private val client: HttpClient =
        HttpClient(engine) {
            install(HttpTimeout) {
                requestTimeoutMillis = 600_000
                connectTimeoutMillis = 30_000
                socketTimeoutMillis = 30_000
            }
        }

    override suspend fun get(): AppConfig = runCatching {
        val response = client.request(JSON_URL)
        val text = response.bodyAsText()
        val dto = json.decodeFromString<AppConfigDto>(text)
        dto.toModel()
    }.also {
        it.exceptionOrNull()?.also { e ->
            e.printStackTrace()
        }
    }.getOrDefault(AppConfig())

    override suspend fun update(value: AppConfig): Unit =
        throw UnsupportedOperationException("Remote config can not be updated from the app!")

    companion object {
        private const val JSON_URL =
            "https://raw.githubusercontent.com/LiveFastEatTrashRaccoon/RaccoonForLemmy/master/docs/app_config.json"
    }
}

@Serializable
private data class AppConfigDto(val alternateMarkdownRenderingSettingsItemEnabled: Boolean = false)

private fun AppConfigDto.toModel() = AppConfig(
    alternateMarkdownRenderingSettingsItemEnabled = alternateMarkdownRenderingSettingsItemEnabled,
)
