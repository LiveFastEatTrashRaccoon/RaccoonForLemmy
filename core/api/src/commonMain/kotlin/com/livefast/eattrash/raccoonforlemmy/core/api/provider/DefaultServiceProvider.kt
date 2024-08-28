package com.livefast.eattrash.raccoonforlemmy.core.api.provider

import com.livefast.eattrash.raccoonforlemmy.core.api.service.AuthService
import com.livefast.eattrash.raccoonforlemmy.core.api.service.CommentService
import com.livefast.eattrash.raccoonforlemmy.core.api.service.CommunityService
import com.livefast.eattrash.raccoonforlemmy.core.api.service.ModlogService
import com.livefast.eattrash.raccoonforlemmy.core.api.service.PostService
import com.livefast.eattrash.raccoonforlemmy.core.api.service.PrivateMessageService
import com.livefast.eattrash.raccoonforlemmy.core.api.service.SearchService
import com.livefast.eattrash.raccoonforlemmy.core.api.service.SiteService
import com.livefast.eattrash.raccoonforlemmy.core.api.service.UserService
import com.livefast.eattrash.raccoonforlemmy.core.api.service.createAuthService
import com.livefast.eattrash.raccoonforlemmy.core.api.service.createCommentService
import com.livefast.eattrash.raccoonforlemmy.core.api.service.createCommunityService
import com.livefast.eattrash.raccoonforlemmy.core.api.service.createModlogService
import com.livefast.eattrash.raccoonforlemmy.core.api.service.createPostService
import com.livefast.eattrash.raccoonforlemmy.core.api.service.createPrivateMessageService
import com.livefast.eattrash.raccoonforlemmy.core.api.service.createSearchService
import com.livefast.eattrash.raccoonforlemmy.core.api.service.createSiteService
import com.livefast.eattrash.raccoonforlemmy.core.api.service.createUserService
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
) : ServiceProvider {
    companion object {
        private const val DEFAULT_INSTANCE = "lemmy.world"
        private const val VERSION = "v3"
        private const val ENABLE_LOGGING = false
    }

    override var currentInstance: String = DEFAULT_INSTANCE

    override lateinit var auth: AuthService

    override lateinit var post: PostService

    override lateinit var community: CommunityService

    override lateinit var user: UserService

    override lateinit var site: SiteService

    override lateinit var comment: CommentService

    override lateinit var search: SearchService

    override lateinit var privateMessages: PrivateMessageService

    override lateinit var modLog: ModlogService

    private val baseUrl: String get() = "https://$currentInstance/api/$VERSION/"

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
                if (ENABLE_LOGGING) {
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
        auth = ktorfit.createAuthService()
        post = ktorfit.createPostService()
        community = ktorfit.createCommunityService()
        user = ktorfit.createUserService()
        site = ktorfit.createSiteService()
        comment = ktorfit.createCommentService()
        search = ktorfit.createSearchService()
        privateMessages = ktorfit.createPrivateMessageService()
        modLog = ktorfit.createModlogService()
    }
}
