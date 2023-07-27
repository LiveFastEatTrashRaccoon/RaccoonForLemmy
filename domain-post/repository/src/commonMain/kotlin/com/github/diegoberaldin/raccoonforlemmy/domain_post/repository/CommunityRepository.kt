package com.github.diegoberaldin.raccoonforlemmy.domain_post.repository

import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.CommunityView
import com.github.diegoberaldin.raccoonforlemmy.core_api.service.CommunityService
import com.github.diegoberaldin.raccoonforlemmy.data.CommunityModel
import com.github.diegoberaldin.raccoonforlemmy.domain_post.repository.utils.toModel

class CommunityRepository(
    private val communityService: CommunityService,
) {

    suspend fun getCommunity(id: Int): CommunityModel? {
        val response = communityService.getCommunity(id = id).body()
        return response?.communityView?.toModel()
    }
}

private fun CommunityView.toModel() = community.toModel()
