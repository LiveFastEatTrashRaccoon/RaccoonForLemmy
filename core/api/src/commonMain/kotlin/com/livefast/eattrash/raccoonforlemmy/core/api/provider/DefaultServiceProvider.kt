package com.livefast.eattrash.raccoonforlemmy.core.api.provider

import com.livefast.eattrash.raccoonforlemmy.core.api.di.ServiceCreationArgs
import com.livefast.eattrash.raccoonforlemmy.core.api.di.getService
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.AuthServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.CommentServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.CommunityServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.ModlogServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.PostServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.PrivateMessageServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.SearchServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.SiteServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.UserServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.V3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v4.AccountServiceV4
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v4.SiteServiceV4
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v4.V4
import com.livefast.eattrash.raccoonforlemmy.core.utils.debug.AppInfoRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal class DefaultServiceProvider(
    private val factory: HttpClientEngine,
    private val appInfoRepository: AppInfoRepository,
) : ServiceProvider {
    companion object {
        private const val DEFAULT_INSTANCE = "lemmy.world"
    }

    override var currentInstance: String = DEFAULT_INSTANCE

    override lateinit var v3: V3

    override lateinit var v4: V4

    private val baseUrl: String get() = "https://$currentInstance/api"

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
        val serviceArgs = ServiceCreationArgs(baseUrl, client)
        v3 =
            object : V3 {
                override val auth = getService<AuthServiceV3>(serviceArgs)
                override val post = getService<PostServiceV3>(serviceArgs)
                override val community = getService<CommunityServiceV3>(serviceArgs)
                override val user = getService<UserServiceV3>(serviceArgs)
                override val site = getService<SiteServiceV3>(serviceArgs)
                override val comment = getService<CommentServiceV3>(serviceArgs)
                override val search = getService<SearchServiceV3>(serviceArgs)
                override val privateMessages = getService<PrivateMessageServiceV3>(serviceArgs)
                override val modLog = getService<ModlogServiceV3>(serviceArgs)
            }
        v4 =
            object : V4 {
                override val account = getService<AccountServiceV4>(serviceArgs)
                override val site = getService<SiteServiceV4>(serviceArgs)
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
