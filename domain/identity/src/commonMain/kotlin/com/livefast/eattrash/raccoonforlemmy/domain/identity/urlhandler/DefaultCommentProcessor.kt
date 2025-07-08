package com.livefast.eattrash.raccoonforlemmy.domain.identity.urlhandler

import com.livefast.eattrash.raccoonforlemmy.core.navigation.MainRouter
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.CommentRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.PostRepository
import kotlinx.coroutines.withTimeoutOrNull

internal class DefaultCommentProcessor(
    private val identityRepository: IdentityRepository,
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
    private val mainRouter: MainRouter,
) : CommentProcessor {
    override suspend fun process(url: String): Boolean = withTimeoutOrNull(2500) {
        val auth = identityRepository.authToken.value
        val resolved =
            commentRepository.getResolved(
                query = url,
                auth = auth,
            )
        val parentId = resolved?.postId
        val post =
            parentId?.let {
                postRepository.get(
                    id = it,
                    auth = auth,
                )
            }
        if (post != null) {
            mainRouter.openPostDetail(
                post = post,
                highlightCommentId = resolved.id,
            )
            true
        } else {
            false
        }
    } ?: false
}
