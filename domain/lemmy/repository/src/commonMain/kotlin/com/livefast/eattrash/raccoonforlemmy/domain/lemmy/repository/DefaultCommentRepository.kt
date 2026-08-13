package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository

import com.livefast.eattrash.raccoonforlemmy.core.api.dto.CreateCommentForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.CreateCommentLikeForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.CreateCommentReportForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.DeleteCommentForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.DistinguishCommentForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.EditCommentForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.PurgeCommentForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.RemoveCommentForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.ResolveCommentReportForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.SaveCommentForm
import com.livefast.eattrash.raccoonforlemmy.core.api.provider.ServiceProvider
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommentModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommentReportModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.ListingType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PersonMentionModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SortType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.utils.toAuthHeader
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.utils.toCommentDto
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.utils.toDto
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.utils.toModel
import kotlinx.coroutines.CancellationException

internal class DefaultCommentRepository(
    private val services: ServiceProvider,
    private val customServices: ServiceProvider,
) : CommentRepository {
    override suspend fun getAll(
        postId: Long?,
        auth: String?,
        instance: String?,
        page: Int,
        limit: Int,
        type: ListingType,
        sort: SortType,
        maxDepth: Int,
    ): List<CommentModel>? = try {
        val response =
            if (instance.isNullOrEmpty()) {
                services.v3.comment.getAll(
                    authHeader = auth.toAuthHeader(),
                    auth = auth,
                    postId = postId,
                    page = page,
                    limit = limit,
                    type = type.toDto(),
                    sort = sort.toCommentDto(),
                    maxDepth = maxDepth,
                )
            } else {
                customServices.changeInstance(instance)
                customServices.v3.comment.getAll(
                    postId = postId,
                    page = page,
                    limit = limit,
                    type = type.toDto(),
                    sort = sort.toCommentDto(),
                    maxDepth = maxDepth,
                )
            }
        response.comments.map { it.toModel() }
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        null
    }

    override suspend fun getBy(id: Long, auth: String?, instance: String?): CommentModel? = try {
        if (instance.isNullOrEmpty()) {
            services.v3.comment.getBy(
                authHeader = auth.toAuthHeader(),
                id = id,
                auth = auth,
            )
        } else {
            customServices.changeInstance(instance)
            customServices.v3.comment.getBy(id = id)
        }.commentView.toModel()
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        null
    }

    override suspend fun getChildren(
        parentId: Long,
        auth: String?,
        instance: String?,
        limit: Int,
        type: ListingType,
        sort: SortType,
        maxDepth: Int,
    ): List<CommentModel>? = try {
        val response =
            if (instance.isNullOrEmpty()) {
                services.v3.comment.getAll(
                    authHeader = auth.toAuthHeader(),
                    auth = auth,
                    parentId = parentId,
                    limit = limit,
                    type = type.toDto(),
                    sort = sort.toCommentDto(),
                    maxDepth = maxDepth,
                )
            } else {
                customServices.changeInstance(instance)
                customServices.v3.comment.getAll(
                    parentId = parentId,
                    limit = limit,
                    type = type.toDto(),
                    sort = sort.toCommentDto(),
                    maxDepth = maxDepth,
                )
            }
        response.comments.map { it.toModel() }
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        null
    }

    override fun asUpVoted(comment: CommentModel, voted: Boolean) = comment.copy(
        myVote = if (voted) 1 else 0,
        score =
        when {
            voted && comment.myVote < 0 -> comment.score + 2
            voted -> comment.score + 1
            !voted -> comment.score - 1
            else -> comment.score
        },
        upvotes =
        when {
            voted -> comment.upvotes + 1
            else -> comment.upvotes - 1
        },
        downvotes =
        when {
            comment.myVote < 0 -> comment.downvotes - 1
            else -> comment.downvotes
        },
    )

    override fun asUpVoted(mention: PersonMentionModel, voted: Boolean) = mention.copy(
        myVote = if (voted) 1 else 0,
        score =
        when {
            voted && mention.myVote < 0 -> mention.score + 2
            voted -> mention.score + 1
            !voted -> mention.score - 1
            else -> mention.score
        },
        upvotes =
        when {
            voted -> mention.upvotes + 1
            else -> mention.upvotes - 1
        },
        downvotes =
        when {
            mention.myVote < 0 -> mention.downvotes - 1
            else -> mention.downvotes
        },
    )

    override suspend fun upVote(comment: CommentModel, auth: String, voted: Boolean) = try {
        val data =
            CreateCommentLikeForm(
                commentId = comment.id,
                score = if (voted) 1 else 0,
                auth = auth,
            )
        services.v3.comment.like(authHeader = auth.toAuthHeader(), form = data)
        Result.success(Unit)
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        Result.failure(e)
    }

    override fun asDownVoted(comment: CommentModel, downVoted: Boolean) = comment.copy(
        myVote = if (downVoted) -1 else 0,
        score =
        when {
            downVoted && comment.myVote > 0 -> comment.score - 2
            downVoted -> comment.score - 1
            !downVoted -> comment.score + 1
            else -> comment.score
        },
        downvotes =
        when {
            downVoted -> comment.downvotes + 1
            else -> comment.downvotes - 1
        },
        upvotes =
        when {
            comment.myVote > 0 -> comment.upvotes - 1
            else -> comment.upvotes
        },
    )

    override fun asDownVoted(mention: PersonMentionModel, downVoted: Boolean) = mention.copy(
        myVote = if (downVoted) -1 else 0,
        score =
        when {
            downVoted && mention.myVote > 0 -> mention.score - 2
            downVoted -> mention.score - 1
            !downVoted -> mention.score + 1
            else -> mention.score
        },
        downvotes =
        when {
            downVoted -> mention.downvotes + 1
            else -> mention.downvotes - 1
        },
        upvotes =
        when {
            mention.myVote > 0 -> mention.upvotes - 1
            else -> mention.upvotes
        },
    )

    override suspend fun downVote(comment: CommentModel, auth: String, downVoted: Boolean) = try {
        val data =
            CreateCommentLikeForm(
                commentId = comment.id,
                score = if (downVoted) -1 else 0,
                auth = auth,
            )
        services.v3.comment.like(authHeader = auth.toAuthHeader(), form = data)
        Result.success(Unit)
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        Result.failure(e)
    }

    override fun asSaved(comment: CommentModel, saved: Boolean) = comment.copy(saved = saved)

    override suspend fun save(comment: CommentModel, auth: String, saved: Boolean) = try {
        val data =
            SaveCommentForm(
                commentId = comment.id,
                save = saved,
                auth = auth,
            )
        services.v3.comment.save(authHeader = auth.toAuthHeader(), form = data)
        Result.success(Unit)
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        Result.failure(e)
    }

    override suspend fun create(postId: Long, parentId: Long?, text: String, languageId: Long?, auth: String) = try {
        val data =
            CreateCommentForm(
                content = text,
                postId = postId,
                parentId = parentId,
                languageId = languageId,
                auth = auth,
            )
        services.v3.comment.create(authHeader = auth.toAuthHeader(), form = data)
        Unit
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        Unit
    }

    override suspend fun edit(commentId: Long, text: String, languageId: Long?, auth: String) = try {
        val data =
            EditCommentForm(
                content = text,
                commentId = commentId,
                languageId = languageId,
                auth = auth,
            )
        services.v3.comment.edit(authHeader = auth.toAuthHeader(), form = data)
        Unit
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        Unit
    }

    override suspend fun delete(commentId: Long, auth: String) = try {
        val data =
            DeleteCommentForm(
                commentId = commentId,
                deleted = true,
            )
        val res = services.v3.comment.delete(authHeader = auth.toAuthHeader(), form = data)
        res.commentView.toModel()
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        null
    }

    override suspend fun restore(commentId: Long, auth: String) = try {
        val data =
            DeleteCommentForm(
                commentId = commentId,
                deleted = false,
            )
        val res = services.v3.comment.delete(authHeader = auth.toAuthHeader(), form = data)
        res.commentView.toModel()
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        null
    }

    override suspend fun report(commentId: Long, reason: String, auth: String) = try {
        val data =
            CreateCommentReportForm(
                commentId = commentId,
                reason = reason,
                auth = auth,
            )
        services.v3.comment.createReport(
            form = data,
            authHeader = auth.toAuthHeader(),
        )
        Unit
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        Unit
    }

    override suspend fun remove(commentId: Long, auth: String, removed: Boolean, reason: String): CommentModel? = try {
        val data =
            RemoveCommentForm(
                commentId = commentId,
                removed = removed,
                reason = reason,
                auth = auth,
            )
        val response =
            services.v3.comment.remove(
                form = data,
                authHeader = auth.toAuthHeader(),
            )
        response.commentView.toModel()
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        null
    }

    override suspend fun distinguish(commentId: Long, auth: String, distinguished: Boolean): CommentModel? = try {
        val data =
            DistinguishCommentForm(
                commentId = commentId,
                distinguished = distinguished,
                auth = auth,
            )
        val response =
            services.v3.comment.distinguish(
                form = data,
                authHeader = auth.toAuthHeader(),
            )
        response.commentView.toModel()
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        null
    }

    override suspend fun getReports(
        auth: String,
        communityId: Long?,
        page: Int,
        limit: Int,
        unresolvedOnly: Boolean,
    ): List<CommentReportModel>? = try {
        val response =
            services.v3.comment.listReports(
                authHeader = auth.toAuthHeader(),
                auth = auth,
                communityId = communityId,
                page = page,
                limit = limit,
                unresolvedOnly = unresolvedOnly,
            )
        response.commentReports.map {
            it.toModel()
        }
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        null
    }

    override suspend fun resolveReport(reportId: Long, auth: String, resolved: Boolean): CommentReportModel? = try {
        val data =
            ResolveCommentReportForm(
                reportId = reportId,
                resolved = resolved,
                auth = auth,
            )
        val response =
            services.v3.comment.resolveReport(
                form = data,
                authHeader = auth.toAuthHeader(),
            )
        response.commentReportView.toModel()
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        null
    }

    override suspend fun purge(auth: String?, commentId: Long, reason: String?) {
        val data =
            PurgeCommentForm(
                commentId = commentId,
                reason = reason,
            )
        val res =
            services.v3.comment.purge(
                form = data,
                authHeader = auth.toAuthHeader(),
            )
        require(res.success)
    }

    override suspend fun getResolved(query: String, auth: String?): CommentModel? = try {
        val response =
            services.v3.search.resolveObject(
                authHeader = auth.toAuthHeader(),
                q = query,
            )
        response.comment?.toModel()
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        null
    }
}
