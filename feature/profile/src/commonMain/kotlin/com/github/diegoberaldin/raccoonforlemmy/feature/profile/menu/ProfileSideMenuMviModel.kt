package com.github.diegoberaldin.raccoonforlemmy.feature.profile.menu

import cafe.adriel.voyager.core.model.ScreenModel
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel

interface ProfileSideMenuMviModel :
    MviModel<ProfileSideMenuMviModel.Intent, ProfileSideMenuMviModel.State, ProfileSideMenuMviModel.Effect>,
    ScreenModel {
    sealed interface Intent

    data class State(
        val isModerator: Boolean = false,
        val canCreateCommunity: Boolean = false,
        val isBookmarksVisible: Boolean = true,
    )

    sealed interface Effect
}
