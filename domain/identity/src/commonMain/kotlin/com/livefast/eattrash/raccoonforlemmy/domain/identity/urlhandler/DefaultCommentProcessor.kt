package com.livefast.eattrash.raccoonforlemmy.domain.identity.urlhandler

import com.livefast.eattrash.raccoonforlemmy.core.commonui.detailopener.api.DetailOpener
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.CommentRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.PostRepository
import kotlinx.coroutines.withTimeoutOrNull
import org.koin.core.annotation.Single

@Single
internal class DefaultCommentProcessor(
    private val identityRepository: IdentityRepository,
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
    private val detailOpener: DetailOpener,
) : CommentProcessor {
    override suspend fun process(url: String): Boolean =
        withTimeoutOrNull(2500) {
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
                detailOpener.openPostDetail(
                    post = post,
                    highlightCommentId = resolved.id,
                )
                true
            } else {
                false
            }
        } ?: false
}
