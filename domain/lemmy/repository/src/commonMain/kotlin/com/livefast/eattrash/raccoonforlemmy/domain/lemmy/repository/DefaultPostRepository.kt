package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository

import com.livefast.eattrash.raccoonforlemmy.core.api.dto.CreatePostForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.CreatePostLikeForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.CreatePostReportForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.DeletePostForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.EditPostForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.FeaturePostForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.HidePostForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.LockPostForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.MarkPostAsReadForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.PostFeatureType
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.PurgePostForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.RemovePostForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.ResolvePostReportForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.SavePostForm
import com.livefast.eattrash.raccoonforlemmy.core.api.provider.ServiceProvider
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.ListingType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostReportModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SortType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.utils.toAuthHeader
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.utils.toDto
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.utils.toModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single
internal class DefaultPostRepository(
    @Named("default") private val services: ServiceProvider,
    @Named("custom") private val customServices: ServiceProvider,
) : PostRepository {
    override suspend fun getAll(
        auth: String?,
        page: Int,
        pageCursor: String?,
        limit: Int,
        type: ListingType,
        sort: SortType,
        communityId: Long?,
        communityName: String?,
        otherInstance: String?,
    ): Pair<List<PostModel>, String?>? =
        withContext(Dispatchers.IO) {
            runCatching {
                val response =
                    if (otherInstance.isNullOrEmpty()) {
                        services.post.getAll(
                            authHeader = auth.toAuthHeader(),
                            auth = auth,
                            communityId = communityId,
                            page = if (pageCursor.isNullOrEmpty()) page else null,
                            pageCursor = pageCursor,
                            limit = limit,
                            type = type.toDto(),
                            sort = sort.toDto(),
                        )
                    } else {
                        customServices.changeInstance(otherInstance)
                        customServices.post.getAll(
                            communityName = communityName,
                            page = if (pageCursor.isNullOrEmpty()) page else null,
                            pageCursor = pageCursor,
                            limit = limit,
                            type = type.toDto(),
                            sort = sort.toDto(),
                        )
                    }
                val posts = response.posts.map { it.toModel() }
                posts to response.nextPage
            }.getOrNull()
        }

    override suspend fun get(
        id: Long,
        auth: String?,
        instance: String?,
    ): PostModel? =
        withContext(Dispatchers.IO) {
            runCatching {
                val response =
                    if (instance.isNullOrEmpty()) {
                        services.post.get(
                            authHeader = auth.toAuthHeader(),
                            auth = auth,
                            id = id,
                        )
                    } else {
                        customServices.changeInstance(instance)
                        customServices.post.get(id = id)
                    }
                response.postView.toModel().copy(
                    crossPosts = response.crossPosts.map { it.toModel() },
                )
            }.getOrNull()
        }

    override fun asUpVoted(
        post: PostModel,
        voted: Boolean,
    ) = post.copy(
        myVote = if (voted) 1 else 0,
        score =
            when {
                voted && post.myVote < 0 -> post.score + 2
                voted -> post.score + 1
                !voted -> post.score - 1
                else -> post.score
            },
        upvotes =
            when {
                voted -> post.upvotes + 1
                else -> post.upvotes - 1
            },
        downvotes =
            when {
                post.myVote < 0 -> post.downvotes - 1
                else -> post.downvotes
            },
    )

    override suspend fun upVote(
        post: PostModel,
        auth: String,
        voted: Boolean,
    ) = withContext(Dispatchers.IO) {
        runCatching {
            val data =
                CreatePostLikeForm(
                    postId = post.id,
                    score = if (voted) 1 else 0,
                    auth = auth,
                )
            services.post.like(
                authHeader = auth.toAuthHeader(),
                form = data,
            )
            Unit
        }
    }

    override fun asDownVoted(
        post: PostModel,
        downVoted: Boolean,
    ) = post.copy(
        myVote = if (downVoted) -1 else 0,
        score =
            when {
                downVoted && post.myVote > 0 -> post.score - 2
                downVoted -> post.score - 1
                !downVoted -> post.score + 1
                else -> post.score
            },
        downvotes =
            when {
                downVoted -> post.downvotes + 1
                else -> post.downvotes - 1
            },
        upvotes =
            when {
                post.myVote > 0 -> post.upvotes - 1
                else -> post.upvotes
            },
    )

    override suspend fun downVote(
        post: PostModel,
        auth: String,
        downVoted: Boolean,
    ) = withContext(Dispatchers.IO) {
        runCatching {
            val data =
                CreatePostLikeForm(
                    postId = post.id,
                    score = if (downVoted) -1 else 0,
                    auth = auth,
                )
            services.post.like(
                authHeader = auth.toAuthHeader(),
                form = data,
            )
            Unit
        }
    }

    override fun asSaved(
        post: PostModel,
        saved: Boolean,
    ): PostModel = post.copy(saved = saved)

    override suspend fun save(
        post: PostModel,
        auth: String,
        saved: Boolean,
    ) = withContext(Dispatchers.IO) {
        runCatching {
            val data =
                SavePostForm(
                    postId = post.id,
                    save = saved,
                    auth = auth,
                )
            services.post.save(
                authHeader = auth.toAuthHeader(),
                form = data,
            )
            Unit
        }
    }

    override suspend fun create(
        communityId: Long,
        title: String,
        body: String?,
        url: String?,
        nsfw: Boolean,
        languageId: Long?,
        auth: String,
    ): Unit =
        withContext(Dispatchers.IO) {
            val data =
                CreatePostForm(
                    communityId = communityId,
                    name = title,
                    body = body,
                    url = url,
                    nsfw = nsfw,
                    languageId = languageId,
                    auth = auth,
                )
            services.post.create(
                authHeader = auth.toAuthHeader(),
                form = data,
            )
        }

    override suspend fun edit(
        postId: Long,
        title: String,
        body: String?,
        url: String?,
        nsfw: Boolean,
        languageId: Long?,
        auth: String,
    ): Unit =
        withContext(Dispatchers.IO) {
            val data =
                EditPostForm(
                    postId = postId,
                    name = title,
                    body = body,
                    url = url,
                    nsfw = nsfw,
                    languageId = languageId,
                    auth = auth,
                )
            services.post.edit(
                authHeader = auth.toAuthHeader(),
                form = data,
            )
        }

    override suspend fun setRead(
        read: Boolean,
        postId: Long,
        auth: String?,
    ) = withContext(Dispatchers.IO) {
        runCatching {
            val data =
                MarkPostAsReadForm(
                    postId = postId,
                    read = read,
                    auth = auth.orEmpty(),
                )
            services.post.markAsRead(
                authHeader = auth.toAuthHeader(),
                form = data,
            )
            Unit
        }
    }

    override suspend fun hide(
        hidden: Boolean,
        postId: Long,
        auth: String?,
    ) = withContext(Dispatchers.IO) {
        runCatching {
            val data =
                HidePostForm(
                    postIds = listOf(postId),
                    hidden = hidden,
                )
            services.post.hide(
                authHeader = auth.toAuthHeader(),
                form = data,
            )
            Unit
        }
    }

    override suspend fun delete(
        id: Long,
        auth: String,
    ) = withContext(Dispatchers.IO) {
        runCatching {
            val data =
                DeletePostForm(
                    postId = id,
                    deleted = true,
                )
            val res =
                services.post.delete(
                    authHeader = auth.toAuthHeader(),
                    form = data,
                )
            res.postView.toModel()
        }.getOrNull()
    }

    override suspend fun restore(
        id: Long,
        auth: String,
    ): PostModel? =
        withContext(Dispatchers.IO) {
            runCatching {
                val data =
                    DeletePostForm(
                        postId = id,
                        deleted = false,
                    )
                val res =
                    services.post.delete(
                        authHeader = auth.toAuthHeader(),
                        form = data,
                    )
                res.postView.toModel()
            }.getOrNull()
        }

    override suspend fun report(
        postId: Long,
        reason: String,
        auth: String,
    ): Unit =
        withContext(Dispatchers.IO) {
            runCatching {
                val data =
                    CreatePostReportForm(
                        postId = postId,
                        reason = reason,
                        auth = auth,
                    )
                services.post.createReport(
                    form = data,
                    authHeader = auth.toAuthHeader(),
                )
            }
        }

    override suspend fun featureInCommunity(
        postId: Long,
        auth: String,
        featured: Boolean,
    ): PostModel? =
        withContext(Dispatchers.IO) {
            runCatching {
                val data =
                    FeaturePostForm(
                        postId = postId,
                        auth = auth,
                        featured = featured,
                        featureType = PostFeatureType.Community,
                    )
                val response =
                    services.post.feature(
                        form = data,
                        authHeader = auth.toAuthHeader(),
                    )
                response.postView.toModel()
            }.getOrNull()
        }

    override suspend fun featureInInstance(
        postId: Long,
        auth: String,
        featured: Boolean,
    ): PostModel? =
        withContext(Dispatchers.IO) {
            runCatching {
                val data =
                    FeaturePostForm(
                        postId = postId,
                        auth = auth,
                        featured = featured,
                        featureType = PostFeatureType.Local,
                    )
                val response =
                    services.post.feature(
                        form = data,
                        authHeader = auth.toAuthHeader(),
                    )
                response.postView.toModel()
            }.getOrNull()
        }

    override suspend fun lock(
        postId: Long,
        auth: String,
        locked: Boolean,
    ): PostModel? =
        withContext(Dispatchers.IO) {
            runCatching {
                val data =
                    LockPostForm(
                        postId = postId,
                        auth = auth,
                        locked = locked,
                    )
                val response =
                    services.post.lock(
                        form = data,
                        authHeader = auth.toAuthHeader(),
                    )
                response.postView.toModel()
            }.getOrNull()
        }

    override suspend fun remove(
        postId: Long,
        auth: String,
        reason: String,
        removed: Boolean,
    ): PostModel? =
        runCatching {
            val data =
                RemovePostForm(
                    postId = postId,
                    auth = auth,
                    removed = removed,
                    reason = reason,
                )
            val response =
                services.post.remove(
                    form = data,
                    authHeader = auth.toAuthHeader(),
                )
            response.postView.toModel()
        }.getOrNull()

    override suspend fun getReports(
        auth: String,
        communityId: Long?,
        page: Int,
        limit: Int,
        unresolvedOnly: Boolean,
    ): List<PostReportModel>? =
        withContext(Dispatchers.IO) {
            runCatching {
                val response =
                    services.post.listReports(
                        authHeader = auth.toAuthHeader(),
                        auth = auth,
                        communityId = communityId,
                        page = page,
                        limit = limit,
                        unresolvedOnly = unresolvedOnly,
                    )
                response.postReports.map {
                    it.toModel()
                }
            }.getOrNull()
        }

    override suspend fun resolveReport(
        reportId: Long,
        auth: String,
        resolved: Boolean,
    ): PostReportModel? =
        withContext(Dispatchers.IO) {
            runCatching {
                val data =
                    ResolvePostReportForm(
                        reportId = reportId,
                        auth = auth,
                        resolved = resolved,
                    )
                val response =
                    services.post.resolveReport(
                        form = data,
                        authHeader = auth.toAuthHeader(),
                    )
                response.postReportView.toModel()
            }.getOrNull()
        }

    override suspend fun purge(
        auth: String?,
        postId: Long,
        reason: String?,
    ) = withContext(Dispatchers.IO) {
        val data =
            PurgePostForm(
                postId = postId,
                reason = reason,
            )
        val response =
            services.post.purge(
                form = data,
                authHeader = auth.toAuthHeader(),
            )
        require(response.success)
    }

    override suspend fun getResolved(
        query: String,
        auth: String?,
    ): PostModel? =
        withContext(Dispatchers.IO) {
            runCatching {
                val response =
                    services.search.resolveObject(
                        authHeader = auth.toAuthHeader(),
                        q = query,
                    )
                response.post?.toModel()
            }.getOrNull()
        }
}
