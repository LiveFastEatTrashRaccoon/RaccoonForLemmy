package com.github.diegoberaldin.raccoonforlemmy.core_api.service

import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.CreatePostForm
import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.CreatePostLikeForm
import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.DeletePostForm
import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.EditPostForm
import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.GetPostResponse
import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.GetPostsResponse
import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.ListingType
import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.PostResponse
import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.SavePostForm
import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.SortType
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Query

interface PostService {

    @GET("post/list")
    suspend fun getPosts(
        @Query("auth") auth: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("sort") sort: SortType? = null,
        @Query("comment_id") commentId: Int? = null,
        @Query("page") page: Int? = null,
        @Query("type_") type: ListingType? = null,
        @Query("community_name") communityName: String? = null,
        @Query("saved_only") savedOnly: Boolean? = null,
    ): Response<GetPostsResponse>

    @GET("post")
    suspend fun getPost(
        @Query("auth") auth: String? = null,
        @Query("id") id: Int? = null,
        @Query("comment_id") commentId: Int? = null,
    ): Response<GetPostResponse>

    @PUT("post/save")
    @Headers("Content-Type: application/json")
    suspend fun savePost(@Body form: SavePostForm): Response<PostResponse>

    @POST("post/like")
    @Headers("Content-Type: application/json")
    suspend fun likePost(@Body form: CreatePostLikeForm): Response<PostResponse>

    @POST("post")
    @Headers("Content-Type: application/json")
    suspend fun createPost(@Body form: CreatePostForm): Response<PostResponse>

    @PUT("post")
    @Headers("Content-Type: application/json")
    suspend fun editPost(@Body form: EditPostForm): Response<PostResponse>

    @POST("post/delete")
    @Headers("Content-Type: application/json")
    suspend fun deletePost(@Body form: DeletePostForm): Response<PostResponse>
}
