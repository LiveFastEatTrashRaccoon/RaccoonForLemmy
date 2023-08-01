package com.github.diegoberaldin.raccoonforlemmy.feature_profile.content.logged

import com.github.diegoberaldin.raccoonforlemmy.core_architecture.MviModel

interface ProfileLoggedMviModel :
    MviModel<ProfileLoggedMviModel.Intent, ProfileLoggedMviModel.UiState, ProfileLoggedMviModel.Effect> {

    sealed interface Intent {
        data class SelectTab(val value: ProfileLoggedSection) : Intent
    }

    data class UiState(
        val currentTab: ProfileLoggedSection = ProfileLoggedSection.POSTS,
    )

    sealed interface Effect
}
