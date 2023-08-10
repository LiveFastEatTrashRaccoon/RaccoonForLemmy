package com.github.diegoberaldin.raccoonforlemmy.core.commonui.userdetail

import cafe.adriel.voyager.core.model.ScreenModel
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel

class UserDetailViewModel(
    private val mvi: DefaultMviModel<UserDetailMviModel.Intent, UserDetailMviModel.UiState, UserDetailMviModel.Effect>,
) : ScreenModel,
    MviModel<UserDetailMviModel.Intent, UserDetailMviModel.UiState, UserDetailMviModel.Effect> by mvi {
    override fun onStarted() {
        mvi.onStarted()
    }

    override fun reduce(intent: UserDetailMviModel.Intent) {
        when (intent) {
            is UserDetailMviModel.Intent.SelectTab -> mvi.updateState {
                it.copy(currentTab = intent.value)
            }
        }
    }
}
