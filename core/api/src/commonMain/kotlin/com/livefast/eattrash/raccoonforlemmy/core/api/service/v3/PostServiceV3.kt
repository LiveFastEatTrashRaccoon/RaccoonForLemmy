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
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Query
import de.jensklingenberg.ktorfit.http.Url
import io.ktor.client.request.forms.MultiPartFormDataContent

interface PostServiceV3 {
    @GET("v3/post/list")
    suspend fun getAll(
        @Header("Authorization") authHeader: String? = null,
        @Query("auth") auth: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("sort") sort: SortType? = null,
        @Query("page") page: Int? = null,
        @Query("page_cursor") pageCursor: String? = null,
        @Query("type_") type: ListingType? = null,
        @Query("community_id") communityId: CommunityId? = null,
        @Query("community_name") communityName: String? = null,
        @Query("saved_only") savedOnly: Boolean? = null,
        @Query("liked_only") likedOnly: Boolean? = null,
        @Query("disliked_only") dislikedOnly: Boolean? = null,
        @Query("show_hidden") showHidden: Boolean? = null,
    ): GetPostsResponse

    @GET("v3/post")
    suspend fun get(
        @Header("Authorization") authHeader: String? = null,
        @Query("auth") auth: String? = null,
        @Query("id") id: PostId? = null,
        @Query("comment_id") commentId: CommentId? = null,
    ): GetPostResponse

    @GET("v3/post/site_metadata")
    suspend fun getSiteMetadata(
        @Header("Authorization") authHeader: String? = null,
        @Query("url")
        url: String,
    ): GetSiteMetadataResponse

    @PUT("v3/post/save")
    @Headers("Content-Type: application/json")
    suspend fun save(
        @Header("Authorization") authHeader: String? = null,
        @Body form: SavePostForm,
    ): PostResponse

    @POST("v3/post/like")
    @Headers("Content-Type: application/json")
    suspend fun like(
        @Header("Authorization") authHeader: String? = null,
        @Body form: CreatePostLikeForm,
    ): PostResponse

    @POST("v3/post")
    @Headers("Content-Type: application/json")
    suspend fun create(
        @Header("Authorization") authHeader: String? = null,
        @Body form: CreatePostForm,
    ): PostResponse

    @PUT("v3/post")
    @Headers("Content-Type: application/json")
    suspend fun edit(
        @Header("Authorization") authHeader: String? = null,
        @Body form: EditPostForm,
    ): PostResponse

    @POST("v3/post/mark_as_read")
    @Headers("Content-Type: application/json")
    suspend fun markAsRead(
        @Header("Authorization") authHeader: String? = null,
        @Body form: MarkPostAsReadForm,
    ): PostResponse

    @POST("v3/post/hide")
    @Headers("Content-Type: application/json")
    suspend fun hide(
        @Header("Authorization") authHeader: String? = null,
        @Body form: HidePostForm,
    ): PostResponse

    @POST("v3/post/delete")
    @Headers("Content-Type: application/json")
    suspend fun delete(
        @Header("Authorization") authHeader: String? = null,
        @Body form: DeletePostForm,
    ): PostResponse

    @POST
    suspend fun uploadImage(
        @Url url: String,
        @Header("Cookie") token: String,
        @Header("Authorization") authHeader: String? = null,
        @Body content: MultiPartFormDataContent,
    ): PictrsImages

    @GET
    suspend fun deleteImage(
        @Url url: String,
        @Header("Cookie") token: String,
        @Header("Authorization") authHeader: String? = null,
    )

    @POST("v3/post/report")
    @Headers("Content-Type: application/json")
    suspend fun createReport(
        @Header("Authorization") authHeader: String? = null,
        @Body form: CreatePostReportForm,
    ): PostReportResponse

    @POST("v3/post/feature")
    @Headers("Content-Type: application/json")
    suspend fun feature(
        @Header("Authorization") authHeader: String? = null,
        @Body form: FeaturePostForm,
    ): PostResponse

    @POST("v3/post/remove")
    @Headers("Content-Type: application/json")
    suspend fun remove(
        @Header("Authorization") authHeader: String? = null,
        @Body form: RemovePostForm,
    ): PostResponse

    @POST("v3/post/lock")
    @Headers("Content-Type: application/json")
    suspend fun lock(
        @Header("Authorization") authHeader: String? = null,
        @Body form: LockPostForm,
    ): PostResponse

    @GET("v3/post/report/list")
    @Headers("Content-Type: application/json")
    suspend fun listReports(
        @Header("Authorization") authHeader: String? = null,
        @Query("auth") auth: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("page") page: Int? = null,
        @Query("unresolved_only") unresolvedOnly: Boolean? = null,
        @Query("community_id") communityId: CommunityId? = null,
    ): ListPostReportsResponse

    @PUT("v3/post/report/resolve")
    @Headers("Content-Type: application/json")
    suspend fun resolveReport(
        @Header("Authorization") authHeader: String? = null,
        @Body form: ResolvePostReportForm,
    ): PostReportResponse

    @POST("v3/admin/purge/post")
    @Headers("Content-Type: application/json")
    suspend fun purge(
        @Header("Authorization") authHeader: String? = null,
        @Body form: PurgePostForm,
    ): SuccessResponse
}
