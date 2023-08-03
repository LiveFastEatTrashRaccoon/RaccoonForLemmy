package com.github.diegoberaldin.raccoonforlemmy.core.api.service

import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.CommentResponse
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.CreateCommentForm
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.CreateCommentLikeForm
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.DeleteCommentForm
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.EditCommentForm
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.GetCommentsResponse
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.ListingType
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.SaveCommentForm
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.SortType
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Query

interface CommentService {
    @GET("comment/list")
    suspend fun getAll(
        @Query("auth") auth: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("sort") sort: SortType? = null,
        @Query("post_id") postId: Int? = null,
        @Query("parent_id") parentId: Int? = null,
        @Query("page") page: Int? = null,
        @Query("max_depth") maxDepth: Int? = null,
        @Query("type_") type: ListingType? = null,
        @Query("community_id") communityId: Int? = null,
        @Query("community_name") communityName: String? = null,
        @Query("saved_only") savedOnly: Boolean? = null,
    ): Response<GetCommentsResponse>

    @PUT("comment/save")
    @Headers("Content-Type: application/json")
    suspend fun save(@Body form: SaveCommentForm): Response<CommentResponse>

    @POST("comment/like")
    @Headers("Content-Type: application/json")
    suspend fun like(@Body form: CreateCommentLikeForm): Response<CommentResponse>

    @POST("comment")
    @Headers("Content-Type: application/json")
    suspend fun create(@Body form: CreateCommentForm): Response<CommentResponse>

    @PUT("comment")
    @Headers("Content-Type: application/json")
    suspend fun edit(@Body form: EditCommentForm): Response<CommentResponse>

    @POST("comment/delete")
    @Headers("Content-Type: application/json")
    suspend fun delete(@Body form: DeleteCommentForm): Response<CommentResponse>
}
