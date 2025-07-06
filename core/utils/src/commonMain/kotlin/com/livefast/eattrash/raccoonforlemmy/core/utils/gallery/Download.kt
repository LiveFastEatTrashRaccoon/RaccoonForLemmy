package com.livefast.eattrash.raccoonforlemmy.core.utils.gallery

import com.livefast.eattrash.raccoonforlemmy.core.utils.network.provideHttpClientEngine
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.prepareGet
import io.ktor.http.contentLength
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readRemaining
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.io.readByteArray

suspend fun GalleryHelper.download(url: String): ByteArray =
    withContext(Dispatchers.IO) {
        val factory = provideHttpClientEngine()
        val client = HttpClient(factory)
        client.prepareGet(url).execute { httpResponse ->
            val channel: ByteReadChannel = httpResponse.body()
            var result = byteArrayOf()
            while (!channel.isClosedForRead) {
                val packet = channel.readRemaining(4096)
                while (!packet.exhausted()) {
                    val bytes = packet.readByteArray()
                    result += bytes
                    println("Received ${result.size} bytes / ${httpResponse.contentLength()}")
                }
            }
            result
        }
    }
