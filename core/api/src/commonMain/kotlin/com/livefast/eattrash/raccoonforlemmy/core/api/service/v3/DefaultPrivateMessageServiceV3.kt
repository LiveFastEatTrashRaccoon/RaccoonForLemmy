package com.livefast.eattrash.raccoonforlemmy.core.api.service.v3

import com.livefast.eattrash.raccoonforlemmy.core.api.dto.CreatePrivateMessageForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.DeletePrivateMessageForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.EditPrivateMessageForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.MarkPrivateMessageAsReadForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.PersonId
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.PrivateMessageResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.PrivateMessagesResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

internal class DefaultPrivateMessageServiceV3(val baseUrl: String, val client: HttpClient) :
    PrivateMessageServiceV3 {
    override suspend fun getAll(
        authHeader: String?,
        auth: String?,
        page: Int?,
        creatorId: PersonId?,
        limit: Int?,
        unreadOnly: Boolean?,
    ): PrivateMessagesResponse = client.get("$baseUrl/v3/private_message/list") {
        header("Authorization", authHeader)
        parameter("auth", auth)
        parameter("page", page)
        parameter("creator_id", creatorId)
        parameter("limit", limit)
        parameter("unread_only", unreadOnly)
    }.body()

    override suspend fun create(authHeader: String?, form: CreatePrivateMessageForm): PrivateMessageResponse =
        client.post("$baseUrl/v3/private_message") {
            header("Authorization", authHeader)
            contentType(ContentType.Application.Json)
            setBody(form)
        }.body()

    override suspend fun edit(authHeader: String?, form: EditPrivateMessageForm): PrivateMessageResponse =
        client.put("$baseUrl/v3/private_message") {
            header("Authorization", authHeader)
            contentType(ContentType.Application.Json)
            setBody(form)
        }.body()

    override suspend fun markAsRead(authHeader: String?, form: MarkPrivateMessageAsReadForm): PrivateMessageResponse =
        client.post("$baseUrl/v3/private_message/mark_as_read") {
            header("Authorization", authHeader)
            contentType(ContentType.Application.Json)
            setBody(form)
        }.body()

    override suspend fun delete(authHeader: String?, form: DeletePrivateMessageForm): PrivateMessageResponse =
        client.post("$baseUrl/v3/private_message/delete") {
            header("Authorization", authHeader)
            contentType(ContentType.Application.Json)
            setBody(form)
        }.body()
}
