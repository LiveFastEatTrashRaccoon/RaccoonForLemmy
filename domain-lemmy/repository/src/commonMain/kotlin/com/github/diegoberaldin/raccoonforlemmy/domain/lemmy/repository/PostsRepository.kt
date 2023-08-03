package com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository

import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.CreatePostLikeForm
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.SavePostForm
import com.github.diegoberaldin.raccoonforlemmy.core.api.provider.ServiceProvider
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.ListingType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.PostModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.SortType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.utils.toDto
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.utils.toModel

class PostsRepository(
    private val services: ServiceProvider,
) {

    companion object {
        const val DEFAULT_PAGE_SIZE = 20
    }

    suspend fun getPosts(
        auth: String? = null,
        page: Int,
        limit: Int = DEFAULT_PAGE_SIZE,
        type: ListingType = ListingType.Local,
        sort: SortType = SortType.Active,
    ): List<PostModel> {
        val response = services.post.getAll(
            auth = auth,
            page = page,
            limit = limit,
            type = type.toDto(),
            sort = sort.toDto(),
        )
        val dto = response.body()?.posts ?: emptyList()
        return dto.map { it.toModel() }
    }

    suspend fun upVote(post: PostModel, auth: String, voted: Boolean): PostModel {
        val data = CreatePostLikeForm(
            postId = post.id,
            score = if (voted) 1 else 0,
            auth = auth,
        )
        services.post.like(data)
        return post.copy(
            myVote = if (voted) 1 else 0,
            score = when {
                voted && post.myVote < 0 -> post.score + 2
                voted -> post.score + 1
                !voted -> post.score - 1
                else -> post.score
            },
        )
    }

    suspend fun downVote(post: PostModel, auth: String, downVoted: Boolean): PostModel {
        val data = CreatePostLikeForm(
            postId = post.id,
            score = if (downVoted) -1 else 0,
            auth = auth,
        )
        services.post.like(data)
        return post.copy(
            myVote = if (downVoted) -1 else 0,
            score = when {
                downVoted && post.myVote > 0 -> post.score - 2
                downVoted -> post.score - 1
                !downVoted -> post.score + 1
                else -> post.score
            },
        )
    }

    suspend fun save(post: PostModel, auth: String, saved: Boolean): PostModel {
        val data = SavePostForm(
            postId = post.id,
            save = saved,
            auth = auth,
        )
        services.post.save(data)
        return post.copy(saved = saved)
    }
}
