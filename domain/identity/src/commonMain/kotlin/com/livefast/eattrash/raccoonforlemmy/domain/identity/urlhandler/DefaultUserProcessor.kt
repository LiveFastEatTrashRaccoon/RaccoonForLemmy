package com.livefast.eattrash.raccoonforlemmy.domain.identity.urlhandler

import com.livefast.eattrash.raccoonforlemmy.core.commonui.detailopener.api.DetailOpener
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.UserRepository
import kotlinx.coroutines.withTimeoutOrNull

internal class DefaultUserProcessor(
    private val identityRepository: IdentityRepository,
    private val userRepository: UserRepository,
    private val detailOpener: DetailOpener,
    private val urlDecoder: UrlDecoder,
) : UserProcessor {
    override suspend fun process(url: String): Boolean =
        withTimeoutOrNull(2500) {
            val auth = identityRepository.authToken.value
            val resolved =
                userRepository.getResolved(
                    query = url,
                    auth = auth,
                )
            if (resolved != null) {
                detailOpener.openUserDetail(user = resolved)
                true
            } else {
                val user = urlDecoder.getUser(url)
                if (user != null) {
                    detailOpener.openUserDetail(
                        user = user,
                        otherInstance = user.host,
                    )
                    true
                } else {
                    false
                }
            }
        } ?: false
}
