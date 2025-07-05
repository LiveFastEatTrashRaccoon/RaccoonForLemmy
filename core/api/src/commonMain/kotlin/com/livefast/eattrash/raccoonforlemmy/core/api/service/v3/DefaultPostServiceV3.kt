package com.livefast.eattrash.raccoonforlemmy.core.api.service.v3

import com.livefast.eattrash.raccoonforlemmy.core.api.dto.CommentId
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.CommunityId
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.CreatePostForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.CreatePostLikeForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.CreatePostReportForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.DeletePostForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.EditPostForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.FeaturePostForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.GetPostResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.GetPostsResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.GetSiteMetadataResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.HidePostForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.ListPostReportsResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.ListingType
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.LockPostForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.MarkPostAsReadForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.PictrsImages
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.PostId
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.PostReportResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.PostResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.PurgePostForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.RemovePostForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.ResolvePostReportForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.SavePostForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.SortType
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.SuccessResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

internal class DefaultPostServiceV3(val baseUrl: String, val client: HttpClient) : PostServiceV3 {
    override suspend fun getAll(
        authHeader: String?,
        auth: String?,
        limit: Int?,
        sort: SortType?,
        page: Int?,
        pageCursor: String?,
        type: ListingType?,
        communityId: CommunityId?,
        communityName: String?,
        savedOnly: Boolean?,
        likedOnly: Boolean?,
        dislikedOnly: Boolean?,
        showHidden: Boolean?,
    ): GetPostsResponse = client.get("$baseUrl/v3/post/list") {
        header("Authorization", authHeader)
        parameter("auth", auth)
        parameter("limit", limit)
        parameter("sort", sort)
        parameter("page", page)
        parameter("page_cursor", pageCursor)
        parameter("type_", type)
        parameter("community_id", communityId)
        parameter("community_name", communityName)
        parameter("saved_only", savedOnly)
        parameter("liked_only", likedOnly)
        parameter("disliked_only", dislikedOnly)
        parameter("show_hidden", showHidden)
    }.body()

    override suspend fun get(authHeader: String?, auth: String?, id: PostId?, commentId: CommentId?): GetPostResponse =
        client.get("$baseUrl/v3/post") {
            header("Authorization", authHeader)
            parameter("auth", auth)
            parameter("id", id)
            parameter("comment_id", commentId)
        }.body()

    override suspend fun getSiteMetadata(authHeader: String?, url: String): GetSiteMetadataResponse =
        client.get("$baseUrl/v3/post/site_metadata") {
            header("Authorization", authHeader)
            header("url", url)
        }.body()

    override suspend fun save(authHeader: String?, form: SavePostForm): PostResponse =
        client.put("$baseUrl/v3/post/save") {
            header("Authorization", authHeader)
            contentType(ContentType.Application.Json)
            setBody(form)
        }.body()

    override suspend fun like(authHeader: String?, form: CreatePostLikeForm): PostResponse =
        client.post("$baseUrl/v3/post/like") {
            header("Authorization", authHeader)
            contentType(ContentType.Application.Json)
            setBody(form)
        }.body()

    override suspend fun create(authHeader: String?, form: CreatePostForm): PostResponse =
        client.post("$baseUrl/v3/post") {
            header("Authorization", authHeader)
            contentType(ContentType.Application.Json)
            setBody(form)
        }.body()

    override suspend fun edit(authHeader: String?, form: EditPostForm): PostResponse = client.put("$baseUrl/v3/post") {
        header("Authorization", authHeader)
        contentType(ContentType.Application.Json)
        setBody(form)
    }.body()

    override suspend fun markAsRead(authHeader: String?, form: MarkPostAsReadForm): PostResponse =
        client.post("$baseUrl/v3/post/mark_as_read") {
            header("Authorization", authHeader)
            contentType(ContentType.Application.Json)
            setBody(form)
        }.body()

    override suspend fun hide(authHeader: String?, form: HidePostForm): PostResponse =
        client.post("$baseUrl/v3/post/hide") {
            header("Authorization", authHeader)
            contentType(ContentType.Application.Json)
            setBody(form)
        }.body()

    override suspend fun delete(authHeader: String?, form: DeletePostForm): PostResponse =
        client.post("$baseUrl/v3/post/delete") {
            header("Authorization", authHeader)
            contentType(ContentType.Application.Json)
            setBody(form)
        }.body()

    override suspend fun uploadImage(
        url: String,
        token: String,
        authHeader: String?,
        content: MultiPartFormDataContent,
    ): PictrsImages = client.post(url) {
        header("Authorization", authHeader)
        header("Cookie", token)
        contentType(ContentType.MultiPart.FormData)
        setBody(content)
    }.body()

    override suspend fun deleteImage(url: String, token: String, authHeader: String?): Boolean = client.get(url) {
        header("Authorization", authHeader)
        header("Cookie", token)
    }.status.isSuccess()

    override suspend fun createReport(authHeader: String?, form: CreatePostReportForm): PostReportResponse =
        client.post("$baseUrl/v3/post/report") {
            header("Authorization", authHeader)
            contentType(ContentType.Application.Json)
            setBody(form)
        }.body()

    override suspend fun feature(authHeader: String?, form: FeaturePostForm): PostResponse =
        client.post("$baseUrl/v3/post/feature") {
            header("Authorization", authHeader)
            contentType(ContentType.Application.Json)
            setBody(form)
        }.body()

    override suspend fun remove(authHeader: String?, form: RemovePostForm): PostResponse =
        client.post("$baseUrl/v3/post/remove") {
            header("Authorization", authHeader)
            contentType(ContentType.Application.Json)
            setBody(form)
        }.body()

    override suspend fun lock(authHeader: String?, form: LockPostForm): PostResponse =
        client.post("$baseUrl/v3/post/lock") {
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
    ): ListPostReportsResponse = client.get("$baseUrl/v3/post/report/list") {
        header("Authorization", authHeader)
        parameter("auth", auth)
        parameter("limit", limit)
        parameter("page", page)
        parameter("unresolved_only", unresolvedOnly)
        parameter("community_id", communityId)
    }.body()

    override suspend fun resolveReport(authHeader: String?, form: ResolvePostReportForm): PostReportResponse =
        client.put("$baseUrl/v3/post/report/resolve") {
            header("Authorization", authHeader)
            contentType(ContentType.Application.Json)
            setBody(form)
        }.body()

    override suspend fun purge(authHeader: String?, form: PurgePostForm): SuccessResponse =
        client.post("$baseUrl/v3/admin/purge/post") {
            header("Authorization", authHeader)
            contentType(ContentType.Application.Json)
            setBody(form)
        }.body()
}
