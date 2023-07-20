package com.github.diegoberaldin.racoonforlemmy.feature_settings

import cafe.adriel.voyager.core.model.ScreenModel
import com.github.diegoberaldin.raccoonforlemmy.core_appearance.data.ThemeState
import com.github.diegoberaldin.raccoonforlemmy.core_appearance.repository.ThemeRepository
import com.github.diegoberaldin.raccoonforlemmy.core_preferences.KeyStoreKeys
import com.github.diegoberaldin.raccoonforlemmy.core_preferences.TemporaryKeyStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SettingsScreenModel(
    private val themeRepository: ThemeRepository,
    private val keyStore: TemporaryKeyStore,
) : ScreenModel {
    private val _uiState = MutableStateFlow(SettingsScreenUiState())
    val uiState = _uiState.asStateFlow()

    val scope = CoroutineScope(SupervisorJob())

    init {

        themeRepository.state.onEach {
            val isDarkTheme = when (themeRepository.state.value) {
                ThemeState.Dark -> true
                else -> false
            }
            _uiState.getAndUpdate { it.copy(darkTheme = isDarkTheme) }
        }.launchIn(scope)// TODO: is this running forever?
    }

    fun setDarkTheme(value: Boolean) {
        themeRepository.changeTheme(if (value) ThemeState.Dark else ThemeState.Light)
        scope.launch {
            keyStore.save(KeyStoreKeys.EnableDarkTheme, value)
        }
    }
}

expect fun getSettingsScreenModel(): SettingsScreenModel
