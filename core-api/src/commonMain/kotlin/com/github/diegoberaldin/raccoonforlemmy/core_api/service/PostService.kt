package com.github.diegoberaldin.raccoonforlemmy.core_api.service

import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.GetPostResponse
import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.GetPostsResponse
import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.ListingType
import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.SortType
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.GET
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
}