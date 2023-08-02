package com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository

import com.github.diegoberaldin.raccoonforlemmy.core.api.provider.ServiceProvider
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommentModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.ListingType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.SortType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.utils.toDto
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.utils.toModel

class CommentRepository(
    private val serviceProvider: ServiceProvider,
) {
    companion object {
        const val DEFAULT_PAGE_SIZE = 20
    }

    suspend fun getComments(
        postId: Int,
        auth: String? = null,
        page: Int,
        limit: Int = PostsRepository.DEFAULT_PAGE_SIZE,
        type: ListingType = ListingType.All,
        sort: SortType = SortType.Active,
    ): List<CommentModel> {
        val response = serviceProvider.comment.getComments(
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
}
