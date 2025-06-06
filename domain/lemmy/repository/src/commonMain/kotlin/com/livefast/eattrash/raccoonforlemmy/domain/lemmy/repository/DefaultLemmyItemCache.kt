package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository

import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommentModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel

internal class DefaultLemmyItemCache(
    private val postCache: LocalItemCache<PostModel>,
    private val communityCache: LocalItemCache<CommunityModel>,
    private val commentCache: LocalItemCache<CommentModel>,
    private val userCache: LocalItemCache<UserModel>,
) : LemmyItemCache {
    override suspend fun putPost(value: PostModel) =
        postCache.put(value = value, key = value.id)

    override suspend fun getPost(id: Long): PostModel? = postCache.get(id)

    override suspend fun putComment(value: CommentModel) {
        commentCache.put(value = value, key = value.id)
    }

    override suspend fun getComment(id: Long): CommentModel? = commentCache.get(id)

    override suspend fun putCommunity(value: CommunityModel) {
        communityCache.put(value = value, key = value.id)
    }

    override suspend fun getCommunity(id: Long): CommunityModel? = communityCache.get(id)

    override suspend fun putUser(value: UserModel) {
        userCache.put(value = value, key = value.id)
    }

    override suspend fun getUser(id: Long): UserModel? = userCache.get(id)
}
