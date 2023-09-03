package com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository

import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.MarkAllAsReadForm
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.MarkCommentReplyAsReadForm
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.MarkPersonMentionAsReadForm
import com.github.diegoberaldin.raccoonforlemmy.core.api.provider.ServiceProvider
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommentModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.PersonMentionModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.PostModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.SortType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.UserModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.utils.toCommentDto
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.utils.toHost
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.utils.toModel

class UserRepository(
    private val serviceProvider: ServiceProvider,
) {

    suspend fun get(
        id: Int,
        auth: String? = null,
    ): UserModel? {
        val response = serviceProvider.user.getDetails(
            auth = auth,
            personId = id,
        )
        val dto = response.body() ?: return null
        return UserModel(
            id = dto.personView.person.id,
            name = dto.personView.person.name,
            avatar = dto.personView.person.avatar,
            banner = dto.personView.person.banner,
            host = dto.personView.person.actorId.toHost(),
            score = dto.personView.counts.toModel(),
            accountAge = dto.personView.person.published,
        )
    }

    suspend fun getPosts(
        id: Int,
        auth: String? = null,
        page: Int,
        limit: Int = PostsRepository.DEFAULT_PAGE_SIZE,
        sort: SortType = SortType.Active,
        savedOnly: Boolean = false,
    ): List<PostModel> {
        val response = serviceProvider.user.getDetails(
            auth = auth,
            personId = id,
            page = page,
            limit = limit,
            sort = sort.toCommentDto(),
            savedOnly = savedOnly,
        )
        val dto = response.body() ?: return emptyList()
        return dto.posts.map { it.toModel() }
    }

    suspend fun getComments(
        id: Int,
        auth: String? = null,
        page: Int,
        limit: Int = PostsRepository.DEFAULT_PAGE_SIZE,
        sort: SortType = SortType.Active,
    ): List<CommentModel> {
        val response = serviceProvider.user.getDetails(
            auth = auth,
            personId = id,
            page = page,
            limit = limit,
            sort = sort.toCommentDto(),
        )
        val dto = response.body() ?: return emptyList()
        return dto.comments.map { it.toModel() }
    }

    suspend fun getMentions(
        auth: String? = null,
        page: Int,
        limit: Int = PostsRepository.DEFAULT_PAGE_SIZE,
        sort: SortType = SortType.Active,
        unreadOnly: Boolean = true,
    ): List<PersonMentionModel> {
        val response = serviceProvider.user.getMentions(
            auth = auth,
            limit = limit,
            sort = sort.toCommentDto(),
            page = page,
            unreadOnly = unreadOnly,
        )
        val dto = response.body() ?: return emptyList()
        return dto.mentions.map { it.toModel() }
    }

    suspend fun getReplies(
        auth: String? = null,
        page: Int,
        limit: Int = PostsRepository.DEFAULT_PAGE_SIZE,
        sort: SortType = SortType.Active,
        unreadOnly: Boolean = true,
    ): List<PersonMentionModel> {
        val response = serviceProvider.user.getReplies(
            auth = auth,
            limit = limit,
            sort = sort.toCommentDto(),
            page = page,
            unreadOnly = unreadOnly,
        )
        val dto = response.body() ?: return emptyList()
        return dto.replies.map { it.toModel() }
    }

    suspend fun readAll(
        auth: String? = null,
    ) {
        val data = MarkAllAsReadForm(auth.orEmpty())
        serviceProvider.user.markAllAsRead(data)
    }

    suspend fun setMentionRead(read: Boolean, mentionId: Int, auth: String? = null) {
        val data = MarkPersonMentionAsReadForm(
            mentionId = mentionId,
            read = read,
            auth = auth.orEmpty(),
        )
        serviceProvider.user.markPersonMentionAsRead(data)
    }

    suspend fun setReplyRead(read: Boolean, replyId: Int, auth: String? = null) {
        val data = MarkCommentReplyAsReadForm(
            replyId = replyId,
            read = read,
            auth = auth.orEmpty(),
        )
        serviceProvider.user.markCommentReplyAsRead(data)
    }
}
