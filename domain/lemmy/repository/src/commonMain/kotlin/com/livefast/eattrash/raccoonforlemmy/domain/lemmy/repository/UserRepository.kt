package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository

import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommentModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PersonMentionModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SortType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel

interface UserRepository {
    suspend fun getResolved(query: String, auth: String? = null): UserModel?

    suspend fun get(
        id: Long,
        auth: String? = null,
        username: String? = null,
        otherInstance: String? = null,
    ): UserModel?

    suspend fun getPosts(
        id: Long? = null,
        auth: String? = null,
        page: Int,
        limit: Int = PostRepository.DEFAULT_PAGE_SIZE,
        sort: SortType = SortType.Active,
        username: String? = null,
        otherInstance: String? = null,
    ): List<PostModel>?

    suspend fun getSavedPosts(
        id: Long,
        auth: String? = null,
        page: Int,
        limit: Int = PostRepository.DEFAULT_PAGE_SIZE,
        sort: SortType = SortType.Active,
    ): List<PostModel>?

    suspend fun getComments(
        id: Long?,
        auth: String? = null,
        page: Int,
        limit: Int = PostRepository.DEFAULT_PAGE_SIZE,
        sort: SortType = SortType.Active,
        username: String? = null,
        otherInstance: String? = null,
    ): List<CommentModel>?

    suspend fun getSavedComments(
        id: Long,
        auth: String? = null,
        page: Int,
        limit: Int = PostRepository.DEFAULT_PAGE_SIZE,
        sort: SortType = SortType.Active,
    ): List<CommentModel>?

    suspend fun getMentions(
        auth: String? = null,
        page: Int,
        limit: Int = PostRepository.DEFAULT_PAGE_SIZE,
        sort: SortType = SortType.New,
        unreadOnly: Boolean = true,
    ): List<PersonMentionModel>?

    suspend fun getReplies(
        auth: String? = null,
        page: Int,
        limit: Int = PostRepository.DEFAULT_PAGE_SIZE,
        sort: SortType = SortType.New,
        unreadOnly: Boolean = true,
    ): List<PersonMentionModel>?

    suspend fun readAll(auth: String? = null)

    suspend fun setMentionRead(read: Boolean, mentionId: Long, auth: String? = null)

    suspend fun setReplyRead(read: Boolean, replyId: Long, auth: String? = null)

    suspend fun block(id: Long, blocked: Boolean, auth: String? = null)

    suspend fun getModeratedCommunities(auth: String? = null, id: Long?): List<CommunityModel>

    suspend fun getLikedPosts(
        auth: String? = null,
        page: Int,
        pageCursor: String? = null,
        limit: Int = PostRepository.DEFAULT_PAGE_SIZE,
        sort: SortType = SortType.New,
        liked: Boolean = true,
    ): Pair<List<PostModel>, String?>?

    suspend fun getLikedComments(
        auth: String? = null,
        page: Int,
        limit: Int = PostRepository.DEFAULT_PAGE_SIZE,
        sort: SortType = SortType.New,
        liked: Boolean = true,
    ): List<CommentModel>?

    suspend fun purge(auth: String?, id: Long, reason: String? = null)

    suspend fun getHiddenPosts(
        auth: String?,
        page: Int,
        pageCursor: String? = null,
        limit: Int = PostRepository.DEFAULT_PAGE_SIZE,
        sort: SortType = SortType.New,
    ): Pair<List<PostModel>, String?>?

    suspend fun deleteAccount(auth: String?, password: String, deleteContent: Boolean = false): Boolean
}
