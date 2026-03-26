package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository

interface PostCategorizer {
    suspend fun categorize(postHeadline: String): String?
}
