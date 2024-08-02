package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data

import com.livefast.eattrash.raccoonforlemmy.core.utils.looksLikeAnImage

data class PostReportModel(
    val id: Long = 0,
    val creator: UserModel? = null,
    val postId: Long = 0,
    val reason: String? = null,
    val originalTitle: String? = null,
    val originalText: String? = null,
    val originalUrl: String? = null,
    val thumbnailUrl: String? = null,
    val resolved: Boolean = false,
    val resolver: UserModel? = null,
    val publishDate: String? = null,
    val updateDate: String? = null,
)

val PostReportModel.imageUrl: String
    get() =
        originalUrl?.takeIf { it.looksLikeAnImage }?.takeIf { it.isNotEmpty() } ?: run {
            thumbnailUrl
        }.orEmpty()
