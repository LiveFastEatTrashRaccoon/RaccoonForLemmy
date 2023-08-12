package com.github.diegoberaldin.raccoonforlemmy.core.commonui.userdetail

import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.SortType

interface UserDetailMviModel :
    MviModel<UserDetailMviModel.Intent, UserDetailMviModel.UiState, UserDetailMviModel.Effect> {

    sealed interface Intent {
        data class SelectTab(val value: UserDetailSection) : Intent
        data class ChangeSort(val value: SortType) : Intent
    }

    data class UiState(
        val currentTab: UserDetailSection = UserDetailSection.POSTS,
        val sortType: SortType = SortType.Active,
    )

    sealed interface Effect
}
