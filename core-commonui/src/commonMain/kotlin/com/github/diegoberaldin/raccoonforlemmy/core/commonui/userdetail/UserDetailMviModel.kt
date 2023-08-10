package com.github.diegoberaldin.raccoonforlemmy.core.commonui.userdetail

import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel

interface UserDetailMviModel :
    MviModel<UserDetailMviModel.Intent, UserDetailMviModel.UiState, UserDetailMviModel.Effect> {

    sealed interface Intent {
        data class SelectTab(val value: UserDetailSection) : Intent
    }

    data class UiState(
        val currentTab: UserDetailSection = UserDetailSection.POSTS,
    )

    sealed interface Effect
}
