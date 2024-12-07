package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository

import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommentModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single
internal class DefaultLemmyItemCache(
    @Named("postCache") private val postCache: LocalItemCache<PostModel>,
    @Named("communityCache") private val communityCache: LocalItemCache<CommunityModel>,
    @Named("commentCache") private val commentCache: LocalItemCache<CommentModel>,
    @Named("userCache") private val userCache: LocalItemCache<UserModel>,
) : LemmyItemCache {
    override suspend fun putPost(value: PostModel) =
        withContext(Dispatchers.IO) {
            postCache.put(value = value, key = value.id)
        }

    override suspend fun getPost(id: Long): PostModel? = withContext(Dispatchers.IO) { postCache.get(id) }

    override suspend fun putComment(value: CommentModel) =
        withContext(Dispatchers.IO) {
            commentCache.put(value = value, key = value.id)
        }

    override suspend fun getComment(id: Long): CommentModel? = withContext(Dispatchers.IO) { commentCache.get(id) }

    override suspend fun putCommunity(value: CommunityModel) =
        withContext(Dispatchers.IO) {
            communityCache.put(value = value, key = value.id)
        }

    override suspend fun getCommunity(id: Long): CommunityModel? = withContext(Dispatchers.IO) { communityCache.get(id) }

    override suspend fun putUser(value: UserModel) =
        withContext(Dispatchers.IO) {
            userCache.put(value = value, key = value.id)
        }

    override suspend fun getUser(id: Long): UserModel? = withContext(Dispatchers.IO) { userCache.get(id) }
}
