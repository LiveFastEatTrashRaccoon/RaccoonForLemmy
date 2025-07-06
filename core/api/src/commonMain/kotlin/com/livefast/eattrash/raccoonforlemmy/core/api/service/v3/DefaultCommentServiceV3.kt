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
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

internal class DefaultCommentServiceV3(val baseUrl: String, val client: HttpClient) : CommentServiceV3 {
    override suspend fun getAll(
        authHeader: String?,
        auth: String?,
        limit: Int?,
        sort: CommentSortType?,
        postId: PostId?,
        parentId: CommentId?,
        page: Int?,
        maxDepth: Int?,
        type: ListingType?,
        communityId: CommunityId?,
        communityName: String?,
        savedOnly: Boolean?,
        likedOnly: Boolean?,
        dislikedOnly: Boolean?,
    ): GetCommentsResponse = client.get("$baseUrl/v3/comment/list") {
        header("Authorization", authHeader)
        parameter("auth", auth)
        parameter("limit", limit)
        parameter("sort", sort)
        parameter("post_id", postId)
        parameter("parent_id", parentId)
        parameter("page", page)
        parameter("max_depth", maxDepth)
        parameter("type_", type)
        parameter("community_id", communityId)
        parameter("community_name", communityName)
        parameter("saved_only", savedOnly)
        parameter("liked_only", likedOnly)
        parameter("disliked_only", dislikedOnly)
    }.body()

    override suspend fun getBy(authHeader: String?, id: CommentId, auth: String?): GetCommentResponse =
        client.get("$baseUrl/v3/comment") {
            header("Authorization", authHeader)
            parameter("auth", auth)
            parameter("id", id)
        }.body()

    override suspend fun save(authHeader: String?, form: SaveCommentForm): CommentResponse =
        client.put("$baseUrl/v3/comment/save") {
            header("Authorization", authHeader)
            contentType(ContentType.Application.Json)
            setBody(form)
        }.body()

    override suspend fun like(authHeader: String?, form: CreateCommentLikeForm): CommentResponse =
        client.post("$baseUrl/v3/comment/like") {
            header("Authorization", authHeader)
            contentType(ContentType.Application.Json)
            setBody(form)
        }.body()

    override suspend fun create(authHeader: String?, form: CreateCommentForm): CommentResponse =
        client.post("$baseUrl/v3/comment") {
            header("Authorization", authHeader)
            contentType(ContentType.Application.Json)
            setBody(form)
        }.body()

    override suspend fun edit(authHeader: String?, form: EditCommentForm): CommentResponse =
        client.put("$baseUrl/v3/comment") {
            header("Authorization", authHeader)
            contentType(ContentType.Application.Json)
            setBody(form)
        }.body()

    override suspend fun markAsRead(authHeader: String?, form: MarkCommentAsReadForm): CommentReplyResponse =
        client.post("$baseUrl/v3/comment/mark_as_read") {
            header("Authorization", authHeader)
            contentType(ContentType.Application.Json)
            setBody(form)
        }.body()

    override suspend fun delete(authHeader: String?, form: DeleteCommentForm): CommentResponse =
        client.post("$baseUrl/v3/comment/delete") {
            header("Authorization", authHeader)
            contentType(ContentType.Application.Json)
            setBody(form)
        }.body()

    override suspend fun createReport(authHeader: String?, form: CreateCommentReportForm): CommentReportResponse =
        client.post("$baseUrl/v3/comment/report") {
            header("Authorization", authHeader)
        }.body()

    override suspend fun remove(authHeader: String?, form: RemoveCommentForm): CommentResponse =
        client.post("$baseUrl/v3/comment/remove") {
            header("Authorization", authHeader)
            contentType(ContentType.Application.Json)
            setBody(form)
        }.body()

    override suspend fun distinguish(authHeader: String?, form: DistinguishCommentForm): CommentResponse =
        client.post("$baseUrl/v3/comment/distinguish") {
            header("Authorization", authHeader)
            contentType(ContentType.Application.Json)
            setBody(form)
        }.body()

    override suspend fun listReports(
        authHeader: String?,
        auth: String?,
        limit: Int?,
        page: Int?,
        unresolvedOnly: Boolean?,
        communityId: CommunityId?,
    ): ListCommentReportsResponse = client.get("$baseUrl/v3/comment/report/list") {
        header("Authorization", authHeader)
        parameter("auth", auth)
        parameter("limit", limit)
        parameter("page", page)
        parameter("unresolved_only", unresolvedOnly)
        parameter("community_id", communityId)
    }.body()

    override suspend fun resolveReport(authHeader: String?, form: ResolveCommentReportForm): CommentReportResponse =
        client.put("$baseUrl/v3/comment/report/resolve") {
            header("Authorization", authHeader)
            contentType(ContentType.Application.Json)
            setBody(form)
        }.body()

    override suspend fun purge(authHeader: String?, form: PurgeCommentForm): SuccessResponse =
        client.post("$baseUrl/v3/admin/purge/comment") {
            header("Authorization", authHeader)
            contentType(ContentType.Application.Json)
            setBody(form)
        }.body()
}
