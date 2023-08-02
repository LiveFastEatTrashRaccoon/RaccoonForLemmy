package com.github.diegoberaldin.raccoonforlemmy.core.api.service

import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.BlockCommunityForm
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.BlockCommunityResponse
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.CommunityResponse
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.FollowCommunityForm
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.GetCommunityResponse
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Query

interface CommunityService {

    @GET("community")
    suspend fun getCommunity(
        @Query("auth") auth: String? = null,
        @Query("id") id: Int? = null,
        @Query("name") name: String? = null,
    ): Response<GetCommunityResponse>

    @POST("community/follow")
    suspend fun followCommunity(@Body form: FollowCommunityForm): Response<CommunityResponse>

    @POST("community/block")
    suspend fun blockCommunity(@Body form: BlockCommunityForm): Response<BlockCommunityResponse>
}
