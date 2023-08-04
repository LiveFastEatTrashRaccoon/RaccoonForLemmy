package com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository

import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.CommunityView
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.SearchType
import com.github.diegoberaldin.raccoonforlemmy.core.api.provider.ServiceProvider
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.utils.toModel

class CommunityRepository(
    private val services: ServiceProvider,
) {

    companion object {
        const val DEFAULT_PAGE_SIZE = 20
    }

    suspend fun getAll(
        query: String = "",
        auth: String? = null,
        page: Int,
        limit: Int = DEFAULT_PAGE_SIZE,
    ): List<CommunityModel> {
        val response = services.search.search(
            q = query,
            auth = auth,
            page = page,
            limit = limit,
            type = SearchType.Communities,
        ).body()
        return response?.communities?.map {
            it.toModel()
        }.orEmpty()
    }

    suspend fun getSubscribed(
        auth: String? = null,
    ): List<CommunityModel> {
        val response = services.site.get(auth).body()
        return response?.myUser?.follows?.map { it.toModel() }.orEmpty()
    }

    suspend fun get(
        auth: String? = null,
        id: Int,
    ): CommunityModel? {
        val response = services.community.get(
            auth = auth,
            id = id,
        ).body()
        return response?.communityView?.toModel()
    }
}

private fun CommunityView.toModel() = community.toModel()
