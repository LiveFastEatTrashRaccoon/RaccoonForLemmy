package com.github.diegoberaldin.raccoonforlemmy.core_api.provider

import com.github.diegoberaldin.raccoonforlemmy.core_api.service.AuthService
import com.github.diegoberaldin.raccoonforlemmy.core_api.service.CommunityService
import com.github.diegoberaldin.raccoonforlemmy.core_api.service.PostService
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal class DefaultServiceProvider : ServiceProvider {

    companion object {
        private const val DEFAULT_INSTANCE = "lemmy.world"
        private const val VERSION = "v3"
    }

    override var currentInstance: String = DEFAULT_INSTANCE
        private set

    override lateinit var post: PostService
        private set

    override lateinit var community: CommunityService
        private set

    override lateinit var auth: AuthService
        private set

    private val baseUrl: String get() = "https://$currentInstance/api/$VERSION/"
    private val client = HttpClient {
        defaultRequest {
            url {
                host = currentInstance
            }
        }
        install(Logging) {
            logger = defaultLogger
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(Json { isLenient = true; ignoreUnknownKeys = true })
        }
    }


    init {
        reinitialize()
    }

    override fun changeInstance(value: String) {
        currentInstance = value
        reinitialize()
    }

    private fun reinitialize() {
        val ktorfit = Ktorfit.Builder()
            .baseUrl(baseUrl)
            .httpClient(client)
            .build()
        post = ktorfit.create()
        community = ktorfit.create()
        auth = ktorfit.create()
    }
}