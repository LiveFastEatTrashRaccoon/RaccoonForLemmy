package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository

import com.livefast.eattrash.raccoonforlemmy.core.api.provider.ServiceProvider
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.ModlogItem
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.ModlogItemType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.utils.toAuthHeader
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.utils.toDto

internal class DefaultModlogRepository(private val services: ServiceProvider) : ModlogRepository {
    override suspend fun getItems(
        auth: String?,
        communityId: Long?,
        limit: Int,
        page: Int,
        type: ModlogItemType,
    ): List<ModlogItem>? = runCatching {
        val response =
            services.v3.modLog.getItems(
                authHeader = auth.toAuthHeader(),
                auth = auth,
                communityId = communityId,
                limit = limit,
                page = page,
                type = type.toDto(),
            )
        val result =
            buildList<ModlogItem> {
                this += response.added?.map { it.toDto() }.orEmpty()
                this += response.addedToCommunity?.map { it.toDto() }.orEmpty()
                this += response.adminPurgedComments?.map { it.toDto() }.orEmpty()
                this += response.adminPurgedCommunities?.map { it.toDto() }.orEmpty()
                this += response.adminPurgedPersons?.map { it.toDto() }.orEmpty()
                this += response.adminPurgedPosts?.map { it.toDto() }.orEmpty()
                this += response.banned?.map { it.toDto() }.orEmpty()
                this += response.bannedFromCommunity?.map { it.toDto() }.orEmpty()
                this += response.featuredPosts?.map { it.toDto() }.orEmpty()
                this += response.hiddenCommunities?.map { it.toDto() }.orEmpty()
                this += response.lockedPosts?.map { it.toDto() }.orEmpty()
                this += response.removedComments?.map { it.toDto() }.orEmpty()
                this += response.removedCommunities?.map { it.toDto() }.orEmpty()
                this += response.removedPosts?.map { it.toDto() }.orEmpty()
                this += response.transferredToCommunity?.map { it.toDto() }.orEmpty()
            }
        result.sortedByDescending { it.date }
    }.getOrNull()
}
