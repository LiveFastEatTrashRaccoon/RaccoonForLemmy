package com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository

import com.github.diegoberaldin.raccoonforlemmy.core.api.provider.ServiceProvider
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommentModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.PostModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.SortType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.UserModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.utils.toDto
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.utils.toHost
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.utils.toModel

class UserRepository(
    private val serviceProvider: ServiceProvider,
) {

    suspend fun getUser(
        id: Int,
        auth: String? = null,
    ): UserModel? {
        val response = serviceProvider.user.getPersonDetails(
            auth = auth,
            personId = id,
        )
        val dto = response.body() ?: return null
        return UserModel(
            id = dto.personView.person.id,
            name = dto.personView.person.name,
            avatar = dto.personView.person.avatar,
            host = dto.personView.person.actorId.toHost(),
            score = dto.personView.counts.toModel(),
        )
    }

    suspend fun getUserPosts(
        id: Int,
        auth: String? = null,
        page: Int,
        limit: Int = PostsRepository.DEFAULT_PAGE_SIZE,
        sort: SortType = SortType.Active,
        savedOnly: Boolean = false,
    ): List<PostModel> {
        val response = serviceProvider.user.getPersonDetails(
            auth = auth,
            personId = id,
            page = page,
            limit = limit,
            sort = sort.toDto(),
            savedOnly = savedOnly,
        )
        val dto = response.body() ?: return emptyList()
        return dto.posts.map { it.toModel() }
    }

    suspend fun getUserComments(
        id: Int,
        auth: String? = null,
        page: Int,
        limit: Int = PostsRepository.DEFAULT_PAGE_SIZE,
        sort: SortType = SortType.Active,
    ): List<CommentModel> {
        val response = serviceProvider.user.getPersonDetails(
            auth = auth,
            personId = id,
            page = page,
            limit = limit,
            sort = sort.toDto(),
        )
        val dto = response.body() ?: return emptyList()
        return dto.comments.map { it.toModel() }
    }
}
