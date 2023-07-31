package com.github.diegoberaldin.raccoonforlemmy.core_api.service

import com.github.diegoberaldin.raccoonforlemmy.core_api.dto.GetSiteResponse
import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query

interface SiteService {
    @GET("site")
    suspend fun getSite(
        @Query("auth") auth: String? = null,
    ): Response<GetSiteResponse>
}