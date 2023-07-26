package com.github.diegoberaldin.raccoonforlemmy.core_api.service

import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.GetPersonDetailsResponse
import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.SortType
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query

interface UserService {

    @GET("user")
    suspend fun getPersonDetails(
        @Query("auth") auth: String? = null,
        @Query("community_id") communityId: Int? = null,
        @Query("person_id") personId: Int? = null,
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("sort") sort: SortType,
        @Query("username") username: String? = null,
        @Query("saved_only") savedOnly: Boolean? = null,
    ): Response<GetPersonDetailsResponse>
}