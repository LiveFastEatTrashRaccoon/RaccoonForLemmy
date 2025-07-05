package com.livefast.eattrash.raccoonforlemmy.core.api.service.v3

import com.livefast.eattrash.raccoonforlemmy.core.api.dto.CreatePrivateMessageForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.DeletePrivateMessageForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.EditPrivateMessageForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.MarkPrivateMessageAsReadForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.PersonId
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.PrivateMessageResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.PrivateMessagesResponse
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Query
import io.ktor.client.HttpClient

interface PrivateMessageServiceV3 {
    @GET("v3/private_message/list")
    suspend fun getAll(
        @Header("Authorization") authHeader: String? = null,
        @Query("auth") auth: String? = null,
        @Query("page") page: Int? = null,
        @Query("creator_id") creatorId: PersonId? = null,
        @Query("limit") limit: Int? = null,
        @Query("unread_only") unreadOnly: Boolean? = null,
    ): PrivateMessagesResponse

    @POST("v3/private_message")
    @Headers("Content-Type: application/json")
    suspend fun create(
        @Header("Authorization") authHeader: String? = null,
        @Body form: CreatePrivateMessageForm,
    ): PrivateMessageResponse

    @PUT("v3/private_message")
    @Headers("Content-Type: application/json")
    suspend fun edit(
        @Header("Authorization") authHeader: String? = null,
        @Body form: EditPrivateMessageForm,
    ): PrivateMessageResponse

    @POST("v3/private_message/mark_as_read")
    @Headers("Content-Type: application/json")
    suspend fun markAsRead(
        @Header("Authorization") authHeader: String? = null,
        @Body form: MarkPrivateMessageAsReadForm,
    ): PrivateMessageResponse

    @POST("v3/private_message/delete")
    @Headers("Content-Type: application/json")
    suspend fun delete(
        @Header("Authorization") authHeader: String? = null,
        @Body form: DeletePrivateMessageForm,
    ): PrivateMessageResponse
}

internal class DefaultPrivateMessageServiceV3(val baseUrl: String, val client: HttpClient) : PrivateMessageServiceV3 {
    override suspend fun getAll(
        authHeader: String?,
        auth: String?,
        page: Int?,
        creatorId: PersonId?,
        limit: Int?,
        unreadOnly: Boolean?,
    ): PrivateMessagesResponse {
        TODO("Not yet implemented")
    }

    override suspend fun create(authHeader: String?, form: CreatePrivateMessageForm): PrivateMessageResponse {
        TODO("Not yet implemented")
    }

    override suspend fun edit(authHeader: String?, form: EditPrivateMessageForm): PrivateMessageResponse {
        TODO("Not yet implemented")
    }

    override suspend fun markAsRead(authHeader: String?, form: MarkPrivateMessageAsReadForm): PrivateMessageResponse {
        TODO("Not yet implemented")
    }

    override suspend fun delete(authHeader: String?, form: DeletePrivateMessageForm): PrivateMessageResponse {
        TODO("Not yet implemented")
    }
}
