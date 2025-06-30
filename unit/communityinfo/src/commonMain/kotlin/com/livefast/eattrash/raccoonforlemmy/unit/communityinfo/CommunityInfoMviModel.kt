package com.livefast.eattrash.raccoonforlemmy.unit.communityinfo

import androidx.compose.runtime.Stable
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel

@Stable
interface CommunityInfoMviModel :
    MviModel<CommunityInfoMviModel.Intent, CommunityInfoMviModel.UiState, CommunityInfoMviModel.Effect> {
    sealed interface Intent

    data class UiState(
        val community: CommunityModel = CommunityModel(),
        val autoLoadImages: Boolean = true,
        val preferNicknames: Boolean = true,
        val moderators: List<UserModel> = emptyList(),
    )

    sealed interface Effect
}
