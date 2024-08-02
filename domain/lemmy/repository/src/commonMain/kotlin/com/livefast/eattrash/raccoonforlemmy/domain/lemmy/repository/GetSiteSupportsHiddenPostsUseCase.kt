package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository

interface GetSiteSupportsHiddenPostsUseCase {
    suspend operator fun invoke(): Boolean
}
