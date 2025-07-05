package com.livefast.eattrash.raccoonforlemmy.feature.profile.menu

import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModel

interface ProfileSideMenuMviModel :
    MviModel<ProfileSideMenuMviModel.Intent, ProfileSideMenuMviModel.State, ProfileSideMenuMviModel.Effect> {
    sealed interface Intent

    data class State(
        val isModerator: Boolean = false,
        val canCreateCommunity: Boolean = false,
        val isBookmarksVisible: Boolean = true,
    )

    sealed interface Effect
}
