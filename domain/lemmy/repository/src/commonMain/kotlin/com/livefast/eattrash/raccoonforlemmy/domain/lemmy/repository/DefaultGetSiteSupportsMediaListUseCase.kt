package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository

import org.koin.core.annotation.Single

@Single
internal class DefaultGetSiteSupportsMediaListUseCase(
    private val isSiteVersionAtLeastUseCase: IsSiteVersionAtLeastUseCase,
) : GetSiteSupportsMediaListUseCase {
    companion object {
        const val THRESHOLD_MAJOR = 0
        const val THRESHOLD_MINOR = 19
        const val THRESHOLD_PATCH = 4
    }

    override suspend fun invoke(): Boolean =
        isSiteVersionAtLeastUseCase.execute(
            major = THRESHOLD_MAJOR,
            minor = THRESHOLD_MINOR,
            patch = THRESHOLD_PATCH,
        )
}
