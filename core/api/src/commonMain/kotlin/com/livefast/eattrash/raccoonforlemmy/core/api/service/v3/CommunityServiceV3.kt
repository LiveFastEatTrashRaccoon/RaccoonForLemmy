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

interface CommunityServiceV3 {
    suspend fun get(
        authHeader: String? = null,
        auth: String? = null,
        id: CommunityId? = null,
        name: String? = null,
    ): GetCommunityResponse

    suspend fun getAll(
        authHeader: String? = null,
        auth: String? = null,
        page: Int? = null,
        limit: Int? = null,
        showNsfw: Boolean = true,
        sort: SortType = SortType.Active,
    ): ListCommunitiesResponse

    suspend fun follow(authHeader: String? = null, form: FollowCommunityForm): CommunityResponse

    suspend fun block(authHeader: String? = null, form: BlockCommunityForm): BlockCommunityResponse

    suspend fun ban(authHeader: String? = null, form: BanFromCommunityForm): BanFromCommunityResponse

    suspend fun addMod(authHeader: String? = null, form: AddModToCommunityForm): AddModToCommunityResponse

    suspend fun create(authHeader: String? = null, form: CreateCommunityForm): CommunityResponse

    suspend fun edit(authHeader: String? = null, form: EditCommunityForm): CommunityResponse

    suspend fun hide(authHeader: String? = null, form: HideCommunityForm): SuccessResponse

    suspend fun delete(authHeader: String? = null, form: DeleteCommunityForm): CommunityResponse

    suspend fun purge(authHeader: String? = null, form: PurgeCommunityForm): SuccessResponse
}
