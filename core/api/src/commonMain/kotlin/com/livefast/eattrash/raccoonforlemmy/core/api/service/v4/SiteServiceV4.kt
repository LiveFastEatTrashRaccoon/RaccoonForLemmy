package com.livefast.eattrash.raccoonforlemmy.core.api.service.v4

import com.livefast.eattrash.raccoonforlemmy.core.api.dto.GetSiteResponse

interface SiteServiceV4 {
    suspend fun get(authHeader: String? = null): GetSiteResponse
}
