package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.usecase

interface GetSiteSupportsMediaListUseCase {
    suspend operator fun invoke(): Boolean
}
