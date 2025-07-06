package com.livefast.eattrash.raccoonforlemmy.core.api.service.v3

import com.livefast.eattrash.raccoonforlemmy.core.api.dto.CommentId
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.CommentReplyResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.CommentReportResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.CommentResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.CommentSortType
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.CommunityId
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.CreateCommentForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.CreateCommentLikeForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.CreateCommentReportForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.DeleteCommentForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.DistinguishCommentForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.EditCommentForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.GetCommentResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.GetCommentsResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.ListCommentReportsResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.ListingType
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.MarkCommentAsReadForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.PostId
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.PurgeCommentForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.RemoveCommentForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.ResolveCommentReportForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.SaveCommentForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.SuccessResponse

interface CommentServiceV3 {
    suspend fun getAll(
        authHeader: String? = null,
        auth: String? = null,
        limit: Int? = null,
        sort: CommentSortType? = null,
        postId: PostId? = null,
        parentId: CommentId? = null,
        page: Int? = null,
        maxDepth: Int? = null,
        type: ListingType? = null,
        communityId: CommunityId? = null,
        communityName: String? = null,
        savedOnly: Boolean? = null,
        likedOnly: Boolean? = null,
        dislikedOnly: Boolean? = null,
    ): GetCommentsResponse

    suspend fun getBy(authHeader: String? = null, id: CommentId, auth: String? = null): GetCommentResponse

    suspend fun save(authHeader: String? = null, form: SaveCommentForm): CommentResponse

    suspend fun like(authHeader: String? = null, form: CreateCommentLikeForm): CommentResponse

    suspend fun create(authHeader: String? = null, form: CreateCommentForm): CommentResponse

    suspend fun edit(authHeader: String? = null, form: EditCommentForm): CommentResponse

    suspend fun markAsRead(authHeader: String? = null, form: MarkCommentAsReadForm): CommentReplyResponse

    suspend fun delete(authHeader: String? = null, form: DeleteCommentForm): CommentResponse

    suspend fun createReport(authHeader: String? = null, form: CreateCommentReportForm): CommentReportResponse

    suspend fun remove(authHeader: String? = null, form: RemoveCommentForm): CommentResponse

    suspend fun distinguish(authHeader: String? = null, form: DistinguishCommentForm): CommentResponse

    suspend fun listReports(
        authHeader: String? = null,
        auth: String? = null,
        limit: Int? = null,
        page: Int? = null,
        unresolvedOnly: Boolean? = null,
        communityId: CommunityId? = null,
    ): ListCommentReportsResponse

    suspend fun resolveReport(authHeader: String? = null, form: ResolveCommentReportForm): CommentReportResponse

    suspend fun purge(authHeader: String? = null, form: PurgeCommentForm): SuccessResponse
}
