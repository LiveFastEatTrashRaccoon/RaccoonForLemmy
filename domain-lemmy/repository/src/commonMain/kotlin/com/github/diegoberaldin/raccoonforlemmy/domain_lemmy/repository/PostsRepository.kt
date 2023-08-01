package com.github.diegoberaldin.raccoonforlemmy.domain_lemmy.repository

import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.CreatePostLikeForm
import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.SavePostForm
import com.github.diegoberaldin.raccoonforlemmy.core_api.provider.ServiceProvider
import com.github.diegoberaldin.raccoonforlemmy.domain_lemmy.data.ListingType
import com.github.diegoberaldin.raccoonforlemmy.domain_lemmy.data.PostModel
import com.github.diegoberaldin.raccoonforlemmy.domain_lemmy.data.SortType
import com.github.diegoberaldin.raccoonforlemmy.domain_lemmy.repository.utils.toDto
import com.github.diegoberaldin.raccoonforlemmy.domain_lemmy.repository.utils.toModel

class PostsRepository(
    private val services: ServiceProvider,
) {

    companion object {
        const val DEFAULT_PAGE_SIZE = 15
    }

    suspend fun getPosts(
        auth: String? = null,
        page: Int,
        limit: Int = DEFAULT_PAGE_SIZE,
        type: ListingType = ListingType.Local,
        sort: SortType = SortType.Active,
    ): List<PostModel> {
        val response = services.post.getPosts(
            auth = auth,
            page = page,
            limit = limit,
            type = type.toDto(),
            sort = sort.toDto(),
        )
        val dto = response.body()?.posts ?: emptyList()
        return dto.map { it.toModel() }
    }

    suspend fun upVote(post: PostModel, auth: String) {
        val data = CreatePostLikeForm(
            postId = post.id,
            score = 1,
            auth = auth,
        )
        services.post.likePost(data)
    }

    suspend fun undoUpVote(post: PostModel, auth: String) {
        val data = CreatePostLikeForm(
            postId = post.id,
            score = 0,
            auth = auth,
        )
        services.post.likePost(data)
    }

    suspend fun downVote(post: PostModel, auth: String) {
        val data = CreatePostLikeForm(
            postId = post.id,
            score = -1,
            auth = auth,
        )
        services.post.likePost(data)
    }

    suspend fun undoDownVote(post: PostModel, auth: String) {
        val data = CreatePostLikeForm(
            postId = post.id,
            score = 0,
            auth = auth,
        )
        services.post.likePost(data)
    }

    suspend fun save(post: PostModel, auth: String) {
        val data = SavePostForm(
            postId = post.id,
            save = true,
            auth = auth,
        )
        services.post.savePost(data)
    }

    suspend fun undoSave(post: PostModel, auth: String) {
        val data = SavePostForm(
            postId = post.id,
            save = false,
            auth = auth,
        )
        services.post.savePost(data)
    }
}
