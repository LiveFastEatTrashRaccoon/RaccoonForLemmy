package com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository

import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.CreateCommentLikeForm
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.SaveCommentForm
import com.github.diegoberaldin.raccoonforlemmy.core.api.provider.ServiceProvider
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommentModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.ListingType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.SortType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.utils.toDto
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.utils.toModel

class CommentRepository(
    private val services: ServiceProvider,
) {
    companion object {
        const val DEFAULT_PAGE_SIZE = 20
    }

    suspend fun getAll(
        postId: Int,
        auth: String? = null,
        page: Int,
        limit: Int = PostsRepository.DEFAULT_PAGE_SIZE,
        type: ListingType = ListingType.All,
        sort: SortType = SortType.Active,
    ): List<CommentModel> {
        val response = services.comment.getAll(
            auth = auth,
            postId = postId,
            page = page,
            limit = limit,
            type = type.toDto(),
            sort = sort.toDto(),
        )
        val dto = response.body()?.comments ?: emptyList()
        return dto.map { it.toModel() }
    }

    suspend fun upVote(comment: CommentModel, auth: String, voted: Boolean): CommentModel {
        val data = CreateCommentLikeForm(
            commentId = comment.id,
            score = if (voted) 1 else 0,
            auth = auth,
        )
        services.comment.like(data)
        return comment.copy(
            myVote = if (voted) 1 else 0,
            score = when {
                voted && comment.myVote < 0 -> comment.score + 2
                voted -> comment.score + 1
                !voted -> comment.score - 1
                else -> comment.score
            },
        )
    }

    suspend fun downVote(comment: CommentModel, auth: String, downVoted: Boolean): CommentModel {
        val data = CreateCommentLikeForm(
            commentId = comment.id,
            score = if (downVoted) -1 else 0,
            auth = auth,
        )
        services.comment.like(data)
        return comment.copy(
            myVote = if (downVoted) -1 else 0,
            score = when {
                downVoted && comment.myVote > 0 -> comment.score - 2
                downVoted -> comment.score - 1
                !downVoted -> comment.score + 1
                else -> comment.score
            },
        )
    }

    suspend fun save(comment: CommentModel, auth: String, saved: Boolean): CommentModel {
        val data = SaveCommentForm(
            commentId = comment.id,
            save = saved,
            auth = auth,
        )
        services.comment.save(data)
        return comment.copy(saved = saved)
    }
}
