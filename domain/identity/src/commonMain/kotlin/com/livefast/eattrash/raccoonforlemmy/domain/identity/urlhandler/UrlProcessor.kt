package com.livefast.eattrash.raccoonforlemmy.domain.identity.urlhandler

interface UrlProcessor {
    suspend fun process(url: String): Boolean
}
