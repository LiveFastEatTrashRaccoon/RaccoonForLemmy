package com.github.diegoberaldin.raccoonforlemmy.feature_profile.content.logged

import cafe.adriel.voyager.core.model.ScreenModel
import com.github.diegoberaldin.raccoonforlemmy.core_architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.core_architecture.MviModel

class ProfileLoggedViewModel(
    private val mvi: DefaultMviModel<ProfileLoggedMviModel.Intent, ProfileLoggedMviModel.UiState, ProfileLoggedMviModel.Effect>,
) : ScreenModel,
    MviModel<ProfileLoggedMviModel.Intent, ProfileLoggedMviModel.UiState, ProfileLoggedMviModel.Effect> by mvi {
    override fun onStarted() {
        mvi.onStarted()
    }

    override fun reduce(intent: ProfileLoggedMviModel.Intent) {
        when (intent) {
            is ProfileLoggedMviModel.Intent.SelectTab -> mvi.updateState {
                it.copy(
                    currentTab = intent.value,
                )
            }
        }
    }
}
