package com.github.diegoberaldin.raccoonforlemmy.core_api.service

import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.GetCommentsResponse
import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.ListingType
import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.SortType
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query

interface CommentService {
    @GET("comment/list")
    suspend fun getComments(
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
}
