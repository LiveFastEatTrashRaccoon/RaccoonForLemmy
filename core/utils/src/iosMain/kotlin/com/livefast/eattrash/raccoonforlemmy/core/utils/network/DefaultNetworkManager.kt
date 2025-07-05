package com.livefast.eattrash.raccoonforlemmy.core.utils.network

import io.ktor.client.HttpClient
import io.ktor.client.request.prepareGet
import io.ktor.http.HttpStatusCode

internal class DefaultNetworkManager : NetworkManager {
    override suspend fun isNetworkAvailable(): Boolean {
        val factory = provideHttpClientEngine()
        val client = HttpClient(factory)
        return client.prepareGet("https://www.google.com").execute { httpResponse ->
            httpResponse.status == HttpStatusCode.OK
        }
    }
}
