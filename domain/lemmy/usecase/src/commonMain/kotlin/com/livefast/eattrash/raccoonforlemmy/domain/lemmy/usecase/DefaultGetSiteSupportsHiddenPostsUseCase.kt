package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.usecase

import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.utils.SiteVersionDataSource

internal class DefaultGetSiteSupportsHiddenPostsUseCase(private val siteVersionDataSource: SiteVersionDataSource) :
    GetSiteSupportsHiddenPostsUseCase {
    companion object {
        const val THRESHOLD_MAJOR = 0
        const val THRESHOLD_MINOR = 19
        const val THRESHOLD_PATCH = 4
    }

    override suspend fun invoke(): Boolean = siteVersionDataSource.isAtLeast(
        major = THRESHOLD_MAJOR,
        minor = THRESHOLD_MINOR,
        patch = THRESHOLD_PATCH,
    )
}
