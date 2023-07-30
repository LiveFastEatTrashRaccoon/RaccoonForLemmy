package com.github.diegoberaldin.raccoonforlemmy.domain_post.repository

import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.PostView
import com.github.diegoberaldin.raccoonforlemmy.core_api.provider.ServiceProvider
import com.github.diegoberaldin.raccoonforlemmy.data.ListingType
import com.github.diegoberaldin.raccoonforlemmy.data.PostModel
import com.github.diegoberaldin.raccoonforlemmy.data.SortType
import com.github.diegoberaldin.raccoonforlemmy.domain_post.repository.utils.toDto
import com.github.diegoberaldin.raccoonforlemmy.domain_post.repository.utils.toModel

class PostsRepository(
    private val services: ServiceProvider,
) {

    companion object {
        const val DEFAULT_PAGE_SIZE = 15
    }

    suspend fun getPosts(
        page: Int,
        limit: Int = DEFAULT_PAGE_SIZE,
        type: ListingType = ListingType.Local,
        sort: SortType = SortType.Active,
    ): List<PostModel> {
        val response = services.post.getPosts(
            page = page,
            limit = limit,
            type = type.toDto(),
            sort = sort.toDto(),
        )
        val dto = response.body()?.posts ?: emptyList()
        return dto.map { it.toModel() }
    }
}

private fun PostView.toModel() = PostModel(
    id = post.id,
    title = post.name,
    text = post.body.orEmpty(),
    score = counts.score,
    comments = counts.comments,
    thumbnailUrl = post.thumbnailUrl.orEmpty(),
    community = community.toModel(),
    creator = creator.toModel(),
)