package com.github.diegoberaldin.raccoonforlemmy.feature.profile.content.logged

import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.UserModel

interface ProfileLoggedMviModel :
    MviModel<ProfileLoggedMviModel.Intent, ProfileLoggedMviModel.UiState, ProfileLoggedMviModel.Effect> {

    sealed interface Intent {
        data class SelectTab(val value: ProfileLoggedSection) : Intent
    }

    data class UiState(
        val user: UserModel? = null,
        val currentTab: ProfileLoggedSection = ProfileLoggedSection.POSTS,
    )

    sealed interface Effect
}
