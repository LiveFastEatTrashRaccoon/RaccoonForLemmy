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
    ): GetPersonDetailsResponse = client.get("$baseUrl/v3/user") {
        header("Authorization", authHeader)
        parameter("auth", auth)
        parameter("community_id", communityId)
        parameter("person_id", personId)
        parameter("page", page)
        parameter("limit", limit)
        parameter("sort", sort)
        parameter("username", username)
        parameter("saved_only", savedOnly)
    }.body()

    override suspend fun getMentions(
        authHeader: String?,
        auth: String?,
        page: Int?,
        limit: Int?,
        sort: CommentSortType,
        unreadOnly: Boolean?,
    ): GetPersonMentionsResponse = client.get("$baseUrl/v3/user/mention") {
        header("Authorization", authHeader)
        parameter("auth", auth)
        parameter("page", page)
        parameter("limit", limit)
        parameter("sort", sort)
        parameter("unread_only", unreadOnly)
    }.body()

    override suspend fun getReplies(
        authHeader: String?,
        auth: String?,
        page: Int?,
        limit: Int?,
        sort: CommentSortType,
        unreadOnly: Boolean?,
    ): GetRepliesResponse = client.get("$baseUrl/v3/user/replies") {
        header("Authorization", authHeader)
        parameter("auth", auth)
        parameter("page", page)
        parameter("limit", limit)
        parameter("sort", sort)
        parameter("unread_only", unreadOnly)
    }.body()

    override suspend fun markAllAsRead(authHeader: String?, form: MarkAllAsReadForm): GetRepliesResponse =
        client.get("$baseUrl/v3/user/mark_all_as_read") {}.body()

    override suspend fun markPersonMentionAsRead(
        authHeader: String?,
        form: MarkPersonMentionAsReadForm,
    ): PersonMentionResponse = client.post("$baseUrl/v3/user/mark_as_read") {
        header("Authorization", authHeader)
        contentType(ContentType.Application.Json)
        setBody(form)
    }.body()

    override suspend fun block(authHeader: String?, form: BlockPersonForm): BlockPersonResponse =
        client.post("$baseUrl/v3/user/block") {
            header("Authorization", authHeader)
            contentType(ContentType.Application.Json)
            setBody(form)
        }.body()

    override suspend fun saveUserSettings(authHeader: String?, form: SaveUserSettingsForm): SaveUserSettingsResponse =
        client.put("$baseUrl/v3/user/save_user_settings") {
            header("Authorization", authHeader)
            contentType(ContentType.Application.Json)
            setBody(form)
        }.body()

    override suspend fun purge(authHeader: String?, form: PurgePersonForm): SuccessResponse =
        client.post("$baseUrl/v3/admin/purge/person") {
            header("Authorization", authHeader)
            contentType(ContentType.Application.Json)
            setBody(form)
        }.body()

    override suspend fun listMedia(authHeader: String?, page: Int?, limit: Int?): ListMediaResponse =
        client.get("$baseUrl/v3/account/list_media") {
            header("Authorization", authHeader)
            parameter("page", page)
            parameter("limit", limit)
        }.body()

    override suspend fun deleteAccount(authHeader: String?, form: DeleteAccountForm): Boolean =
        client.post("$baseUrl/v3/user/delete_account\"") {
            header("Authorization", authHeader)
            contentType(ContentType.Application.Json)
            setBody(form)
        }.body()
}
