package com.livefast.eattrash.raccoonforlemmy.core.api.service.v3

import com.livefast.eattrash.raccoonforlemmy.core.api.dto.AddModToCommunityForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.AddModToCommunityResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.BanFromCommunityForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.BanFromCommunityResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.BlockCommunityForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.BlockCommunityResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.CommunityId
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.CommunityResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.CreateCommunityForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.DeleteCommunityForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.EditCommunityForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.FollowCommunityForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.GetCommunityResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.HideCommunityForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.ListCommunitiesResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.PurgeCommunityForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.SortType
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.SuccessResponse
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Query

interface CommunityServiceV3 {
    @GET("v3/community")
    suspend fun get(
        @Header("Authorization") authHeader: String? = null,
        @Query("auth") auth: String? = null,
        @Query("id") id: CommunityId? = null,
        @Query("name") name: String? = null,
    ): GetCommunityResponse

    @GET("v3/community/list")
    suspend fun getAll(
        @Header("Authorization") authHeader: String? = null,
        @Query("auth") auth: String? = null,
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("show_nsfw") showNsfw: Boolean = true,
        @Query("sort") sort: SortType = SortType.Active,
    ): ListCommunitiesResponse

    @POST("v3/community/follow")
    @Headers("Content-Type: application/json")
    suspend fun follow(
        @Header("Authorization") authHeader: String? = null,
        @Body form: FollowCommunityForm,
    ): CommunityResponse

    @POST("v3/community/block")
    @Headers("Content-Type: application/json")
    suspend fun block(
        @Header("Authorization") authHeader: String? = null,
        @Body form: BlockCommunityForm,
    ): BlockCommunityResponse

    @POST("v3/community/ban_user")
    @Headers("Content-Type: application/json")
    suspend fun ban(
        @Header("Authorization") authHeader: String? = null,
        @Body form: BanFromCommunityForm,
    ): BanFromCommunityResponse

    @POST("v3/community/mod")
    @Headers("Content-Type: application/json")
    suspend fun addMod(
        @Header("Authorization") authHeader: String? = null,
        @Body form: AddModToCommunityForm,
    ): AddModToCommunityResponse

    @POST("v3/community")
    @Headers("Content-Type: application/json")
    suspend fun create(
        @Header("Authorization") authHeader: String? = null,
        @Body form: CreateCommunityForm,
    ): CommunityResponse

    @PUT("v3/community")
    @Headers("Content-Type: application/json")
    suspend fun edit(
        @Header("Authorization") authHeader: String? = null,
        @Body form: EditCommunityForm,
    ): CommunityResponse

    @PUT("v3/community/hide")
    @Headers("Content-Type: application/json")
    suspend fun hide(
        @Header("Authorization") authHeader: String? = null,
        @Body form: HideCommunityForm,
    ): SuccessResponse

    @POST("v3/community/delete")
    @Headers("Content-Type: application/json")
    suspend fun delete(
        @Header("Authorization") authHeader: String? = null,
        @Body form: DeleteCommunityForm,
    ): CommunityResponse

    @POST("v3/admin/purge/community")
    @Headers("Content-Type: application/json")
    suspend fun purge(
        @Header("Authorization") authHeader: String? = null,
        @Body form: PurgeCommunityForm,
    ): SuccessResponse
}
