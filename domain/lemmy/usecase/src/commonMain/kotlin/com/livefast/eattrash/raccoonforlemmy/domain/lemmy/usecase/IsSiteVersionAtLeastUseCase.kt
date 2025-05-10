package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.usecase

interface IsSiteVersionAtLeastUseCase {
    suspend fun execute(
        major: Int,
        minor: Int = 0,
        patch: Int = 0,
        otherInstance: String? = null,
    ): Boolean
}
