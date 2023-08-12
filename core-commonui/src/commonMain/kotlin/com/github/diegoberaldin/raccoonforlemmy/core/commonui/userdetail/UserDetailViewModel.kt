package com.github.diegoberaldin.raccoonforlemmy.core.commonui.userdetail

import cafe.adriel.voyager.core.model.ScreenModel
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.core.preferences.KeyStoreKeys
import com.github.diegoberaldin.raccoonforlemmy.core.preferences.TemporaryKeyStore
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.SortType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.toSortType

class UserDetailViewModel(
    private val mvi: DefaultMviModel<UserDetailMviModel.Intent, UserDetailMviModel.UiState, UserDetailMviModel.Effect>,
    private val keyStore: TemporaryKeyStore,
) : ScreenModel,
    MviModel<UserDetailMviModel.Intent, UserDetailMviModel.UiState, UserDetailMviModel.Effect> by mvi {
    override fun onStarted() {
        mvi.onStarted()
        val sortType = keyStore[KeyStoreKeys.DefaultPostSortType, 0].toSortType()
        mvi.updateState {
            it.copy(sortType = sortType)
        }
    }

    override fun reduce(intent: UserDetailMviModel.Intent) {
        when (intent) {
            is UserDetailMviModel.Intent.SelectTab -> mvi.updateState {
                it.copy(currentTab = intent.value)
            }

            is UserDetailMviModel.Intent.ChangeSort -> applySortType(intent.value)
        }
    }

    private fun applySortType(value: SortType) {
        mvi.updateState { it.copy(sortType = value) }
    }
}
