package com.github.diegoberaldin.raccoonforlemmy.feature_settings.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import com.github.diegoberaldin.raccoonforlemmy.core_appearance.data.ThemeState
import com.github.diegoberaldin.raccoonforlemmy.core_appearance.data.toInt
import com.github.diegoberaldin.raccoonforlemmy.core_appearance.repository.ThemeRepository
import com.github.diegoberaldin.raccoonforlemmy.core_architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.core_architecture.MviModel
import com.github.diegoberaldin.raccoonforlemmy.core_preferences.KeyStoreKeys
import com.github.diegoberaldin.raccoonforlemmy.core_preferences.TemporaryKeyStore
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SettingsScreenModel(
    private val themeRepository: ThemeRepository,
    private val keyStore: TemporaryKeyStore,
    private val mvi: DefaultMviModel<SettingsScreenMviModel.Intent, SettingsScreenMviModel.UiState, SettingsScreenMviModel.Effect>,
) : ScreenModel,
    MviModel<SettingsScreenMviModel.Intent, SettingsScreenMviModel.UiState, SettingsScreenMviModel.Effect> by mvi {

    override fun onStarted() {
        mvi.onStarted()
        themeRepository.state.onEach { currentTheme ->
            mvi.updateState { it.copy(currentTheme = currentTheme) }
        }.launchIn(mvi.scope)
    }

    override fun reduce(intent: SettingsScreenMviModel.Intent) {
        when (intent) {
            is SettingsScreenMviModel.Intent.ChangeTheme -> applyTheme(intent.value)
        }
    }

    private fun applyTheme(value: ThemeState) {
        themeRepository.changeTheme(value)
        mvi.scope.launch {
            keyStore.save(KeyStoreKeys.UITheme, value.toInt())
        }
    }
}