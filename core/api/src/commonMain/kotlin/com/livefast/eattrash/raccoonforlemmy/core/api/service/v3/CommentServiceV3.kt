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
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Query

interface CommentServiceV3 {
    @GET("v3/comment/list")
    suspend fun getAll(
        @Header("Authorization") authHeader: String? = null,
        @Query("auth") auth: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("sort") sort: CommentSortType? = null,
        @Query("post_id") postId: PostId? = null,
        @Query("parent_id") parentId: CommentId? = null,
        @Query("page") page: Int? = null,
        @Query("max_depth") maxDepth: Int? = null,
        @Query("type_") type: ListingType? = null,
        @Query("community_id") communityId: CommunityId? = null,
        @Query("community_name") communityName: String? = null,
        @Query("saved_only") savedOnly: Boolean? = null,
        @Query("liked_only") likedOnly: Boolean? = null,
        @Query("disliked_only") dislikedOnly: Boolean? = null,
    ): GetCommentsResponse

    @GET("v3/comment")
    suspend fun getBy(
        @Header("Authorization") authHeader: String? = null,
        @Query("id") id: CommentId,
        @Query("auth") auth: String? = null,
    ): GetCommentResponse

    @PUT("v3/comment/save")
    @Headers("Content-Type: application/json")
    suspend fun save(@Header("Authorization") authHeader: String? = null, @Body form: SaveCommentForm): CommentResponse

    @POST("v3/comment/like")
    @Headers("Content-Type: application/json")
    suspend fun like(
        @Header("Authorization") authHeader: String? = null,
        @Body form: CreateCommentLikeForm,
    ): CommentResponse

    @POST("v3/comment")
    @Headers("Content-Type: application/json")
    suspend fun create(
        @Header("Authorization") authHeader: String? = null,
        @Body form: CreateCommentForm,
    ): CommentResponse

    @PUT("v3/comment")
    @Headers("Content-Type: application/json")
    suspend fun edit(@Header("Authorization") authHeader: String? = null, @Body form: EditCommentForm): CommentResponse

    @POST("v3/comment/mark_as_read")
    @Headers("Content-Type: application/json")
    suspend fun markAsRead(
        @Header("Authorization") authHeader: String? = null,
        @Body form: MarkCommentAsReadForm,
    ): CommentReplyResponse

    @POST("v3/comment/delete")
    @Headers("Content-Type: application/json")
    suspend fun delete(
        @Header("Authorization") authHeader: String? = null,
        @Body form: DeleteCommentForm,
    ): CommentResponse

    @POST("v3/comment/report")
    @Headers("Content-Type: application/json")
    suspend fun createReport(
        @Header("Authorization") authHeader: String? = null,
        @Body form: CreateCommentReportForm,
    ): CommentReportResponse

    @POST("v3/comment/remove")
    @Headers("Content-Type: application/json")
    suspend fun remove(
        @Header("Authorization") authHeader: String? = null,
        @Body form: RemoveCommentForm,
    ): CommentResponse

    @POST("v3/comment/distinguish")
    @Headers("Content-Type: application/json")
    suspend fun distinguish(
        @Header("Authorization") authHeader: String? = null,
        @Body form: DistinguishCommentForm,
    ): CommentResponse

    @GET("v3/comment/report/list")
    @Headers("Content-Type: application/json")
    suspend fun listReports(
        @Header("Authorization") authHeader: String? = null,
        @Query("auth") auth: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("page") page: Int? = null,
        @Query("unresolved_only") unresolvedOnly: Boolean? = null,
        @Query("community_id") communityId: CommunityId? = null,
    ): ListCommentReportsResponse

    @PUT("v3/comment/report/resolve")
    @Headers("Content-Type: application/json")
    suspend fun resolveReport(
        @Header("Authorization") authHeader: String? = null,
        @Body form: ResolveCommentReportForm,
    ): CommentReportResponse

    @POST("v3/admin/purge/comment")
    @Headers("Content-Type: application/json")
    suspend fun purge(
        @Header("Authorization") authHeader: String? = null,
        @Body form: PurgeCommentForm,
    ): SuccessResponse
}
