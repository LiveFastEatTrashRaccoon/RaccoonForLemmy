package com.livefast.eattrash.raccoonforlemmy.core.api.service.v3

import com.livefast.eattrash.raccoonforlemmy.core.api.dto.BlockPersonForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.BlockPersonResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.CommentSortType
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.CommunityId
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.DeleteAccountForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.GetPersonDetailsResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.GetPersonMentionsResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.GetRepliesResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.ListMediaResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.MarkAllAsReadForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.MarkPersonMentionAsReadForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.PersonId
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.PersonMentionResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.PurgePersonForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.SaveUserSettingsForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.SaveUserSettingsResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.SuccessResponse
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Query
import io.ktor.client.HttpClient

interface UserServiceV3 {
    @GET("v3/user")
    suspend fun getDetails(
        @Header("Authorization") authHeader: String? = null,
        @Query("auth") auth: String? = null,
        @Query("community_id") communityId: CommunityId? = null,
        @Query("person_id") personId: PersonId? = null,
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("sort") sort: CommentSortType = CommentSortType.New,
        @Query("username") username: String? = null,
        @Query("saved_only") savedOnly: Boolean? = null,
    ): GetPersonDetailsResponse

    @GET("v3/user/mention")
    suspend fun getMentions(
        @Header("Authorization") authHeader: String? = null,
        @Query("auth") auth: String? = null,
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("sort") sort: CommentSortType = CommentSortType.New,
        @Query("unread_only") unreadOnly: Boolean? = null,
    ): GetPersonMentionsResponse

    @GET("v3/user/replies")
    suspend fun getReplies(
        @Header("Authorization") authHeader: String? = null,
        @Query("auth") auth: String? = null,
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("sort") sort: CommentSortType = CommentSortType.New,
        @Query("unread_only") unreadOnly: Boolean? = null,
    ): GetRepliesResponse

    @POST("v3/user/mark_all_as_read")
    @Headers("Content-Type: application/json")
    suspend fun markAllAsRead(
        @Header("Authorization") authHeader: String? = null,
        @Body form: MarkAllAsReadForm,
    ): GetRepliesResponse

    @POST("v3/user/mention/mark_as_read")
    @Headers("Content-Type: application/json")
    suspend fun markPersonMentionAsRead(
        @Header("Authorization") authHeader: String? = null,
        @Body form: MarkPersonMentionAsReadForm,
    ): PersonMentionResponse

    @POST("v3/user/block")
    @Headers("Content-Type: application/json")
    suspend fun block(
        @Header("Authorization") authHeader: String? = null,
        @Body form: BlockPersonForm,
    ): BlockPersonResponse

    @PUT("v3/user/save_user_settings")
    @Headers("Content-Type: application/json")
    suspend fun saveUserSettings(
        @Header("Authorization") authHeader: String? = null,
        @Body form: SaveUserSettingsForm,
    ): SaveUserSettingsResponse

    @POST("v3/admin/purge/person")
    @Headers("Content-Type: application/json")
    suspend fun purge(
        @Header("Authorization") authHeader: String? = null,
        @Body form: PurgePersonForm,
    ): SuccessResponse

    @GET("v3/account/list_media")
    suspend fun listMedia(
        @Header("Authorization") authHeader: String? = null,
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null,
    ): ListMediaResponse

    @POST("v3/user/delete_account")
    @Headers("Content-Type: application/json")
    suspend fun deleteAccount(
        @Header("Authorization") authHeader: String? = null,
        @Body form: DeleteAccountForm,
    ): Boolean
}

internal class DefaultUserServiceV3(val baseUrl: String, val client: HttpClient) : UserServiceV3 {
    override suspend fun getDetails(
        authHeader: String?,
        auth: String?,
        communityId: CommunityId?,
        personId: PersonId?,
        page: Int?,
        limit: Int?,
        sort: CommentSortType,
        username: String?,
        savedOnly: Boolean?,
    ): GetPersonDetailsResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getMentions(
        authHeader: String?,
        auth: String?,
        page: Int?,
        limit: Int?,
        sort: CommentSortType,
        unreadOnly: Boolean?,
    ): GetPersonMentionsResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getReplies(
        authHeader: String?,
        auth: String?,
        page: Int?,
        limit: Int?,
        sort: CommentSortType,
        unreadOnly: Boolean?,
    ): GetRepliesResponse {
        TODO("Not yet implemented")
    }

    override suspend fun markAllAsRead(authHeader: String?, form: MarkAllAsReadForm): GetRepliesResponse {
        TODO("Not yet implemented")
    }

    override suspend fun markPersonMentionAsRead(
        authHeader: String?,
        form: MarkPersonMentionAsReadForm,
    ): PersonMentionResponse {
        TODO("Not yet implemented")
    }

    override suspend fun block(authHeader: String?, form: BlockPersonForm): BlockPersonResponse {
        TODO("Not yet implemented")
    }

    override suspend fun saveUserSettings(authHeader: String?, form: SaveUserSettingsForm): SaveUserSettingsResponse {
        TODO("Not yet implemented")
    }

    override suspend fun purge(authHeader: String?, form: PurgePersonForm): SuccessResponse {
        TODO("Not yet implemented")
    }

    override suspend fun listMedia(authHeader: String?, page: Int?, limit: Int?): ListMediaResponse {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAccount(authHeader: String?, form: DeleteAccountForm): Boolean {
        TODO("Not yet implemented")
    }
}
