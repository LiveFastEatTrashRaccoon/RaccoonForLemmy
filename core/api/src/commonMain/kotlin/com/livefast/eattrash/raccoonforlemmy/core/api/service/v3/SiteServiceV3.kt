package com.livefast.eattrash.raccoonforlemmy.core.api.service.v3

import com.livefast.eattrash.raccoonforlemmy.core.api.dto.BlockInstanceForm
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.BlockInstanceResponse
import com.livefast.eattrash.raccoonforlemmy.core.api.dto.GetSiteResponse

interface SiteServiceV3 {
    suspend fun get(authHeader: String? = null, auth: String? = null): GetSiteResponse

    suspend fun block(authHeader: String? = null, form: BlockInstanceForm): BlockInstanceResponse
}
