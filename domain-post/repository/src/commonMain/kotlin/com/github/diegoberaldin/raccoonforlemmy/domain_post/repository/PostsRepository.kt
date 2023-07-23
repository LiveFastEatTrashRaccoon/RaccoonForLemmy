package com.github.diegoberaldin.raccoonforlemmy.domain_post.repository

import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.PostView
import com.github.diegoberaldin.raccoonforlemmy.core_api.service.PostService
import com.github.diegoberaldin.raccoonforlemmy.data.PostModel

class PostsRepository(
    private val postService: PostService,
) {

    suspend fun getPosts(): List<PostModel> {
        val response = postService.getPosts()
        val dto = response.body()?.posts ?: emptyList()
        return dto.map { it.toModel() }
    }
}

private fun PostView.toModel() = PostModel(
    title = this.post.name,
    text = this.post.body.orEmpty(),
)