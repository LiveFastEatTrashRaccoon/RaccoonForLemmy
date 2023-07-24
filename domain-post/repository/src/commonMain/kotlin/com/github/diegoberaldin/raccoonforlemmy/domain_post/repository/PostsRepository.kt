package com.github.diegoberaldin.raccoonforlemmy.domain_post.repository

import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.PostView
import com.github.diegoberaldin.raccoonforlemmy.core_api.service.PostService
import com.github.diegoberaldin.raccoonforlemmy.data.PostModel

class PostsRepository(
    private val postService: PostService,
) {

    companion object {
        const val DEFAULT_PAGE_SIZE = 15
    }

    suspend fun getPosts(page: Int, limit: Int = DEFAULT_PAGE_SIZE): List<PostModel> {
        val response = postService.getPosts(
            page = page,
            limit = limit
        )
        val dto = response.body()?.posts ?: emptyList()
        return dto.map { it.toModel() }
    }
}

private fun PostView.toModel() = PostModel(
    title = this.post.name,
    text = this.post.body.orEmpty(),
)