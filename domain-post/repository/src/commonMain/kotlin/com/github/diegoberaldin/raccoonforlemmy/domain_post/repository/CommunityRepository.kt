package com.github.diegoberaldin.raccoonforlemmy.domain_post.repository

import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.CommunityView
import com.github.diegoberaldin.raccoonforlemmy.core_api.provider.ServiceProvider
import com.github.diegoberaldin.raccoonforlemmy.data.CommunityModel
import com.github.diegoberaldin.raccoonforlemmy.domain_post.repository.utils.toModel

class CommunityRepository(
    private val services: ServiceProvider,
) {

    suspend fun getCommunity(
        auth: String? = null,
        id: Int,
    ): CommunityModel? {
        val response = services.community.getCommunity(
            auth = auth,
            id = id,
        ).body()
        return response?.communityView?.toModel()
    }
}

private fun CommunityView.toModel() = community.toModel()
