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

internal class DefaultCommunityServiceV3(val baseUrl: String, val client: HttpClient) : CommunityServiceV3 {
    override suspend fun get(
        authHeader: String?,
        auth: String?,
        id: CommunityId?,
        name: String?,
    ): GetCommunityResponse = client.get("$baseUrl/v3/community") {
        header("Authorization", authHeader)
        parameter("auth", auth)
        parameter("id", id)
        parameter("name", name)
    }.body()

    override suspend fun getAll(
        authHeader: String?,
        auth: String?,
        page: Int?,
        limit: Int?,
        showNsfw: Boolean,
        sort: SortType,
    ): ListCommunitiesResponse = client.get("$baseUrl/v3/community/list") {
        header("Authorization", authHeader)
        parameter("auth", auth)
        parameter("limit", limit)
        parameter("page", page)
        parameter("show_nsfw", showNsfw)
        parameter("sort", sort)
    }.body()

    override suspend fun follow(authHeader: String?, form: FollowCommunityForm): CommunityResponse =
        client.post("$baseUrl/v3/community/follow") {
            header("Authorization", authHeader)
            contentType(ContentType.Application.Json)
            setBody(form)
        }.body()

    override suspend fun block(authHeader: String?, form: BlockCommunityForm): BlockCommunityResponse =
        client.post("$baseUrl/v3/community/block") {
            header("Authorization", authHeader)
            contentType(ContentType.Application.Json)
            setBody(form)
        }.body()

    override suspend fun ban(authHeader: String?, form: BanFromCommunityForm): BanFromCommunityResponse =
        client.post("$baseUrl/v3/community/ban_user") {
            header("Authorization", authHeader)
            contentType(ContentType.Application.Json)
            setBody(form)
        }.body()

    override suspend fun addMod(authHeader: String?, form: AddModToCommunityForm): AddModToCommunityResponse =
        client.post("$baseUrl/v3/community/mod") {
            header("Authorization", authHeader)
            contentType(ContentType.Application.Json)
            setBody(form)
        }.body()

    override suspend fun create(authHeader: String?, form: CreateCommunityForm): CommunityResponse =
        client.post("$baseUrl/v3/community") {
            header("Authorization", authHeader)
            contentType(ContentType.Application.Json)
            setBody(form)
        }.body()

    override suspend fun edit(authHeader: String?, form: EditCommunityForm): CommunityResponse =
        client.put("$baseUrl/v3/community") {
            header("Authorization", authHeader)
            contentType(ContentType.Application.Json)
            setBody(form)
        }.body()

    override suspend fun hide(authHeader: String?, form: HideCommunityForm): SuccessResponse =
        client.put("$baseUrl/v3/community/hide") {
            header("Authorization", authHeader)
            contentType(ContentType.Application.Json)
            setBody(form)
        }.body()

    override suspend fun delete(authHeader: String?, form: DeleteCommunityForm): CommunityResponse =
        client.post("$baseUrl/v3/community/delete") {
            header("Authorization", authHeader)
            contentType(ContentType.Application.Json)
            setBody(form)
        }.body()

    override suspend fun purge(authHeader: String?, form: PurgeCommunityForm): SuccessResponse =
        client.post("$baseUrl/v3/admin/purge/community") {
            header("Authorization", authHeader)
            contentType(ContentType.Application.Json)
            setBody(form)
        }.body()
}
