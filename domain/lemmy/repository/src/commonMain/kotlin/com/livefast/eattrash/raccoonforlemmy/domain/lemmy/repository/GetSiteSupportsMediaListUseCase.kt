package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository

interface GetSiteSupportsMediaListUseCase {
    suspend operator fun invoke(): Boolean
}
