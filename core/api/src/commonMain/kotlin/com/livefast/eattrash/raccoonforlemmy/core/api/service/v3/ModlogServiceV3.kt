package com.livefast.eattrash.raccoonforlemmy.core.api.service.v3

import com.livefast.eattrash.raccoonforlemmy.core.api.dto.CommunityId
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.GetModlogResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.ModlogActionType
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.PersonId

interface ModlogServiceV3 {
    suspend fun getItems(
        authHeader: String? = null,
        auth: String? = null,
        page: Int? = null,
        communityId: CommunityId? = null,
        limit: Int? = null,
        modId: PersonId? = null,
        otherId: PersonId? = null,
        type: ModlogActionType? = null,
    ): GetModlogResponse
}
