package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.utils

interface SiteVersionDataSource {
    suspend fun isAtLeast(
        major: Int,
        minor: Int = 0,
        patch: Int = 0,
        otherInstance: String? = null,
    ): Boolean

    suspend fun shouldUseV4(otherInstance: String? = null): Boolean
}
