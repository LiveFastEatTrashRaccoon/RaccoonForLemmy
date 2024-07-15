package com.github.diegoberaldin.raccoonforlemmy.unit.about

import cafe.adriel.voyager.core.model.screenModelScope
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.core.utils.debug.AppInfoRepository
import kotlinx.coroutines.launch

class AboutDialogViewModel(
    appInfoRepository: AppInfoRepository,
) : DefaultMviModel<AboutDialogMviModel.Intent, AboutDialogMviModel.UiState, AboutDialogMviModel.Effect>(
        initialState = AboutDialogMviModel.UiState(),
    ),
    AboutDialogMviModel {
    init {
        val appInfo = appInfoRepository.geInfo()
        screenModelScope.launch {
            updateState {
                it.copy(
                    version = appInfo.versionCode,
                )
            }
        }
    }
}
