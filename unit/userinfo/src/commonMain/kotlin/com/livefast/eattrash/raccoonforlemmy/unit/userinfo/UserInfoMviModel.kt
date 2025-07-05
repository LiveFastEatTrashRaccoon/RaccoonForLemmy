package com.livefast.eattrash.raccoonforlemmy.unit.userinfo

import androidx.compose.runtime.Stable
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel

@Stable
interface UserInfoMviModel : MviModel<UserInfoMviModel.Intent, UserInfoMviModel.UiState, UserInfoMviModel.Effect> {
    sealed interface Intent

    data class UiState(
        val user: UserModel = UserModel(),
        val isAdmin: Boolean = false,
        val autoLoadImages: Boolean = true,
        val preferNicknames: Boolean = true,
        val moderatedCommunities: List<CommunityModel> = emptyList(),
    )

    sealed interface Effect
}
