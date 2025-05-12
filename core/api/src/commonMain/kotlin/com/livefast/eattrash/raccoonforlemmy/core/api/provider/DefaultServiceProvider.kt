package com.livefast.eattrash.raccoonforlemmy.core.api.provider

import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.V3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.createAuthServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.createCommentServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.createCommunityServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.createModlogServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.createPostServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.createPrivateMessageServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.createSearchServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.createSiteServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.createUserServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v4.V4
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v4.createSiteServiceV4
import com.livefast.eattrash.raccoonforlemmy.core.utils.debug.AppInfoRepository
import com.livefast.eattrash.raccoonforlemmy.core.utils.network.provideHttpClientEngineFactory
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal class DefaultServiceProvider(
    private val factory: HttpClientEngineFactory<*> = provideHttpClientEngineFactory(),
    private val appInfoRepository: AppInfoRepository,
) : ServiceProvider {
    companion object {
        private const val DEFAULT_INSTANCE = "lemmy.world"
    }

    override var currentInstance: String = DEFAULT_INSTANCE

    override lateinit var v3: V3

    override lateinit var v4: V4

    private val baseUrl: String get() = "https://$currentInstance/api/"

    private val loggingEnabled: Boolean get() = appInfoRepository.geInfo().isDebug

    init {
        reinitialize()
    }

    override fun changeInstance(value: String) {
        if (currentInstance != value) {
            currentInstance = value
            reinitialize()
        }
    }

    private fun reinitialize() {
        val client =
            HttpClient(factory) {
                defaultRequest {
                    url {
                        host = currentInstance
                    }
                }
                install(HttpTimeout) {
                    requestTimeoutMillis = 600_000
                    connectTimeoutMillis = 30_000
                    socketTimeoutMillis = 30_000
                }
                if (loggingEnabled) {
                    install(Logging) {
                        logger = defaultLogger
                        level = LogLevel.ALL
                    }
                }
                install(ContentNegotiation) {
                    json(
                        Json {
                            isLenient = true
                            ignoreUnknownKeys = true
                        },
                    )
                }
            }
        val ktorfit =
            Ktorfit
                .Builder()
                .baseUrl(baseUrl)
                .httpClient(client)
                .build()
        v3 =
            object : V3 {
                override val auth = ktorfit.createAuthServiceV3()
                override val post = ktorfit.createPostServiceV3()
                override val community = ktorfit.createCommunityServiceV3()
                override val user = ktorfit.createUserServiceV3()
                override val site = ktorfit.createSiteServiceV3()
                override val comment = ktorfit.createCommentServiceV3()
                override val search = ktorfit.createSearchServiceV3()
                override val privateMessages = ktorfit.createPrivateMessageServiceV3()
                override val modLog = ktorfit.createModlogServiceV3()
            }
        v4 =
            object : V4 {
                override val site = ktorfit.createSiteServiceV4()
            }
    }

    override suspend fun getApiVersion(): String {
        val site =
            runCatching {
                v4.site.get()
            }.getOrNull() ?: runCatching {
                v3.site.get()
            }.getOrNull()
        return site?.version.orEmpty()
    }
}
