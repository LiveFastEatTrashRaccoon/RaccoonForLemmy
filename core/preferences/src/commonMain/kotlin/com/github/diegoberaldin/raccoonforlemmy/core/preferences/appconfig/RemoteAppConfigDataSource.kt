package com.github.diegoberaldin.raccoonforlemmy.core.preferences.appconfig

import com.github.diegoberaldin.raccoonforlemmy.core.utils.network.provideHttpClientEngineFactory
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.request
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

internal class RemoteAppConfigDataSource(
    factory: HttpClientEngineFactory<*> = provideHttpClientEngineFactory(),
) : AppConfigDataSource {
    private val client: HttpClient =
        HttpClient(factory) {
            install(HttpTimeout) {
                requestTimeoutMillis = 600_000
                connectTimeoutMillis = 30_000
                socketTimeoutMillis = 30_000
            }
        }

    override suspend fun get(): AppConfig =
        runCatching {
            client.request(JSON_URL).run {
                val text = bodyAsText()
                val dto = Json.decodeFromString<AppConfigDto>(text)
                dto.toModel()
            }
        }.also {
            it.exceptionOrNull()?.also { e ->
                e.printStackTrace()
            }
        }.getOrDefault(AppConfig())

    override suspend fun update(value: AppConfig): Unit =
        throw UnsupportedOperationException("Remote config can not be updated from the app!")

    companion object {
        private const val JSON_URL =
            "https://diegoberaldin.github.io/RaccoonForLemmy/app_config.json"
    }
}

@Serializable
private data class AppConfigDto(
    val alternateMarkdownRenderingSettingsItemEnabled: Boolean = false,
)

private fun AppConfigDto.toModel() =
    AppConfig(
        alternateMarkdownRenderingSettingsItemEnabled = alternateMarkdownRenderingSettingsItemEnabled,
    )
