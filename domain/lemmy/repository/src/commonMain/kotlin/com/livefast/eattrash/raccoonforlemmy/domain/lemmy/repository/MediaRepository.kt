package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository

import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.MediaModel

interface MediaRepository {
    companion object {
        const val DEFAULT_PAGE_SIZE = 20
    }

    suspend fun uploadImage(auth: String, bytes: ByteArray): String?

    suspend fun getAll(auth: String? = null, page: Int, limit: Int = DEFAULT_PAGE_SIZE): List<MediaModel>

    suspend fun delete(auth: String? = null, media: MediaModel)
}
