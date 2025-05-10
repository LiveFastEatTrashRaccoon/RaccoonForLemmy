package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.usecase

interface GetSiteSupportsHiddenPostsUseCase {
    suspend operator fun invoke(): Boolean
}
