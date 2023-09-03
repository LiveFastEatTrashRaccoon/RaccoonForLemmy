package com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository

import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.CommunityView
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.FollowCommunityForm
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.SearchType
import com.github.diegoberaldin.raccoonforlemmy.core.api.dto.SubscribedType
import com.github.diegoberaldin.raccoonforlemmy.core.api.provider.ServiceProvider
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.ListingType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.SortType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.utils.toDto
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.utils.toModel

class CommunityRepository(
    private val services: ServiceProvider,
    private val customServices: ServiceProvider,
) {

    companion object {
        const val DEFAULT_PAGE_SIZE = 20
    }

    suspend fun getAll(
        query: String = "",
        auth: String? = null,
        page: Int,
        limit: Int = DEFAULT_PAGE_SIZE,
        listingType: ListingType = ListingType.All,
        sortType: SortType = SortType.Active,
    ): List<CommunityModel> {
        val response = services.search.search(
            q = query,
            auth = auth,
            page = page,
            limit = limit,
            type = SearchType.Communities,
            listingType = listingType.toDto(),
            sort = sortType.toDto(),
        ).body()
        return response?.communities?.map {
            it.toModel()
        }.orEmpty()
    }

    suspend fun getAllInInstance(
        instance: String = "",
        auth: String? = null,
        page: Int,
        limit: Int = DEFAULT_PAGE_SIZE,
        sort: SortType = SortType.Active,
    ): List<CommunityModel> {
        customServices.changeInstance(instance)
        val response = customServices.community.getAll(
            auth = auth,
            page = page,
            limit = limit,
            sort = sort.toDto(),
        ).body()
        return response?.communities?.map {
            it.toModel()
        }.orEmpty()
    }

    suspend fun getSubscribed(
        auth: String? = null,
    ): List<CommunityModel> {
        val response = services.site.get(auth).body()
        return response?.myUser?.follows?.map { it.community.toModel() }.orEmpty()
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

    suspend fun subscribe(
        auth: String? = null,
        id: Int,
    ): CommunityModel? {
        val data = FollowCommunityForm(
            auth = auth.orEmpty(),
            communityId = id,
            follow = true,
        )
        val response = services.community.follow(data)
        return response.body()?.communityView?.toModel()
    }

    suspend fun unsubscribe(
        auth: String? = null,
        id: Int,
    ): CommunityModel? {
        val data = FollowCommunityForm(
            auth = auth.orEmpty(),
            communityId = id,
            follow = false,
        )
        val response = services.community.follow(data)
        return response.body()?.communityView?.toModel()
    }
}

private fun CommunityView.toModel() = community.toModel().copy(
    subscribed = when (subscribed) {
        SubscribedType.Subscribed -> true
        SubscribedType.NotSubscribed -> false
        else -> null
    },
)
