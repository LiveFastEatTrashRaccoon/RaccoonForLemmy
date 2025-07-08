package com.livefast.eattrash.raccoonforlemmy.domain.identity.urlhandler

import com.livefast.eattrash.raccoonforlemmy.core.navigation.MainRouter
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.PostRepository
import kotlinx.coroutines.withTimeoutOrNull

internal class DefaultPostProcessor(
    private val identityRepository: IdentityRepository,
    private val postRepository: PostRepository,
    private val mainRouter: MainRouter,
    private val urlDecoder: UrlDecoder,
) : PostProcessor {
    override suspend fun process(url: String): Boolean = withTimeoutOrNull(2500) {
        val auth = identityRepository.authToken.value
        val resolved =
            postRepository.getResolved(
                query = url,
                auth = auth,
            )

        if (resolved != null) {
            mainRouter.openPostDetail(resolved)
            true
        } else {
            val (post, instance) = urlDecoder.getPost(url)
            if (post != null) {
                mainRouter.openPostDetail(
                    post = post,
                    otherInstance = instance.orEmpty(),
                )
                true
            } else {
                false
            }
        }
    } ?: false
}
