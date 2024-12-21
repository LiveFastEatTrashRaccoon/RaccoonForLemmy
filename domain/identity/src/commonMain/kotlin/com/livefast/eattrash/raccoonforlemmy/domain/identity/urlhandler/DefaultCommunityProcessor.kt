package com.livefast.eattrash.raccoonforlemmy.domain.identity.urlhandler

import com.livefast.eattrash.raccoonforlemmy.core.commonui.detailopener.api.DetailOpener
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.CommunityRepository
import kotlinx.coroutines.withTimeoutOrNull

internal class DefaultCommunityProcessor(
    private val identityRepository: IdentityRepository,
    private val communityRepository: CommunityRepository,
    private val detailOpener: DetailOpener,
    private val urlDecoder: UrlDecoder,
) : CommunityProcessor {
    override suspend fun process(url: String): Boolean =
        withTimeoutOrNull(2500) {
            val auth = identityRepository.authToken.value
            val resolved =
                communityRepository.getResolved(
                    query = url,
                    auth = auth,
                )

            if (resolved != null) {
                detailOpener.openCommunityDetail(community = resolved)
                true
            } else {
                val community = urlDecoder.getCommunity(url)
                if (community != null) {
                    detailOpener.openCommunityDetail(
                        community = community,
                        otherInstance = community.host,
                    )
                    true
                } else {
                    false
                }
            }
        } ?: false
}
