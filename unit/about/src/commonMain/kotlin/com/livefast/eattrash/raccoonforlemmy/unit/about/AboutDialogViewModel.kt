package com.livefast.eattrash.raccoonforlemmy.unit.about

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.utils.debug.AppInfoRepository
import kotlinx.coroutines.launch

class AboutDialogViewModel(appInfoRepository: AppInfoRepository) :
    ViewModel(),
    MviModelDelegate<AboutDialogMviModel.Intent, AboutDialogMviModel.UiState, AboutDialogMviModel.Effect>
    by DefaultMviModelDelegate(initialState = AboutDialogMviModel.UiState()),
    AboutDialogMviModel {
    init {
        val appInfo = appInfoRepository.geInfo()
        viewModelScope.launch {
            updateState {
                it.copy(
                    version = appInfo.versionCode,
                )
            }
        }
    }
}
