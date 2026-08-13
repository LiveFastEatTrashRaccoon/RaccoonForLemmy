package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository

import com.livefast.eattrash.raccoonforlemmy.core.api.provider.ServiceProvider
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.MediaModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.utils.toAuthHeader
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.utils.toModel
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.CancellationException

class DefaultMediaRepository(private val services: ServiceProvider) : MediaRepository {
    override suspend fun uploadImage(auth: String, bytes: ByteArray): String? = try {
        val url = "https://${services.currentInstance}/pictrs/image"
        val multipart =
            MultiPartFormDataContent(
                formData {
                    append(
                        key = "images[]",
                        value = bytes,
                        headers =
                        Headers.build {
                            append(HttpHeaders.ContentType, "image/*")
                            append(HttpHeaders.ContentDisposition, "filename=image.jpeg")
                        },
                    )
                },
            )
        val images =
            services.v3.post.uploadImage(
                url = url,
                token = "jwt=$auth",
                authHeader = auth.toAuthHeader(),
                content = multipart,
            )
        "$url/${images.files?.firstOrNull()?.file}"
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        null
    }

    override suspend fun getAll(auth: String?, page: Int, limit: Int): List<MediaModel> = try {
        val response =
            services.v3.user.listMedia(
                authHeader = auth.toAuthHeader(),
                page = page,
                limit = limit,
            )
        response.images.map { it.toModel() }
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        emptyList()
    }

    override suspend fun delete(auth: String?, media: MediaModel) {
        val url =
            "https://${services.currentInstance}/pictrs/image/delete/${media.deleteToken}/${media.alias}"
        services.v3.post.deleteImage(
            url = url,
            token = "jwt=$auth",
            authHeader = auth.toAuthHeader(),
        )
    }
}
