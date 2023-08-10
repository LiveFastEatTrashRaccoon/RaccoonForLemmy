package com.github.diegoberaldin.raccoonforlemmy.feature.profile.content

import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.UserModel

interface ProfileContentMviModel :
    MviModel<ProfileContentMviModel.Intent, ProfileContentMviModel.UiState, ProfileContentMviModel.Effect> {

    sealed interface Intent {
        object Logout : Intent
    }

    data class UiState(
        val initial: Boolean = true,
        val currentUser: UserModel? = null,
    )

    sealed interface Effect
}
