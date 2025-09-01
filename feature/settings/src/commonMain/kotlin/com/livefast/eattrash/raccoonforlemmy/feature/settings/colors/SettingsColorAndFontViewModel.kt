package com.livefast.eattrash.raccoonforlemmy.feature.settings.colors

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.CommentBarTheme
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.UiFontFamily
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.UiTheme
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.toInt
import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.ThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.ColorSchemeProvider
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.SettingsModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.AccountRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.feature.settings.colors.SettingsColorAndFontMviModel.Effect
import com.livefast.eattrash.raccoonforlemmy.feature.settings.colors.SettingsColorAndFontMviModel.Intent
import com.livefast.eattrash.raccoonforlemmy.feature.settings.colors.SettingsColorAndFontMviModel.UiState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SettingsColorAndFontViewModel(
    private val themeRepository: ThemeRepository,
    private val colorSchemeProvider: ColorSchemeProvider,
    private val identityRepository: IdentityRepository,
    private val settingsRepository: SettingsRepository,
    private val accountRepository: AccountRepository,
) : ViewModel(),
    MviModelDelegate<Intent, UiState, Effect> by DefaultMviModelDelegate(initialState = UiState()),
    SettingsColorAndFontMviModel {
    init {
        viewModelScope.launch {
            themeRepository.uiTheme
                .onEach { value ->
                    updateState { it.copy(uiTheme = value) }
                }.launchIn(this)
            themeRepository.uiFontFamily
                .onEach { value ->
                    updateState { it.copy(uiFontFamily = value) }
                }.launchIn(this)
            themeRepository.uiFontScale
                .onEach { value ->
                    updateState { it.copy(uiFontScale = value) }
                }.launchIn(this)
            themeRepository.dynamicColors
                .onEach { value ->
                    updateState { it.copy(dynamicColors = value) }
                }.launchIn(this)
            themeRepository.customSeedColor
                .onEach { value ->
                    updateState { it.copy(customSeedColor = value) }
                }.launchIn(this)
            themeRepository.upVoteColor
                .onEach { value ->
                    updateState { it.copy(upVoteColor = value) }
                }.launchIn(this)
            themeRepository.downVoteColor
                .onEach { value ->
                    updateState { it.copy(downVoteColor = value) }
                }.launchIn(this)
            themeRepository.replyColor
                .onEach { value ->
                    updateState { it.copy(replyColor = value) }
                }.launchIn(this)
            themeRepository.saveColor
                .onEach { value ->
                    updateState { it.copy(saveColor = value) }
                }.launchIn(this)
            themeRepository.commentBarTheme
                .onEach { value ->
                    updateState { it.copy(commentBarTheme = value) }
                }.launchIn(this)
            settingsRepository.currentSettings
                .onEach { settings ->
                    updateState { it.copy(randomColor = settings.randomThemeColor) }
                }.launchIn(this)

            identityRepository.isLogged
                .onEach { logged ->
                    updateState { it.copy(isLogged = logged ?: false) }
                }.launchIn(this)

            updateState {
                it.copy(
                    supportsDynamicColors = colorSchemeProvider.supportsDynamicColors,
                )
            }
        }
    }

    override fun reduce(intent: Intent) {
        when (intent) {
            is Intent.ChangeDynamicColors -> changeDynamicColors(intent.value)
            is Intent.ChangeRandomColor -> changeRandomColor(intent.value)
            is Intent.ChangeCommentBarTheme -> changeCommentBarTheme(intent.value)
            is Intent.ChangeFontFamily -> changeFontFamily(intent.value)
            is Intent.ChangeFontSize -> changeUiFontScale(intent.value)
            is Intent.ChangeTheme -> changeTheme(intent.value)
            is Intent.ChangeThemeColor -> changeCustomSeedColor(intent.value)
            is Intent.ChangeActionColor -> {
                when (intent.type) {
                    CustomColorType.SaveColor -> changeSaveColor(intent.value)
                    CustomColorType.ReplyColor -> changeReplyColor(intent.value)
                    CustomColorType.DownvoteColor -> changeDownVoteColor(intent.value)
                    CustomColorType.UpvoteColor -> changeUpVoteColor(intent.value)
                    CustomColorType.None -> Unit
                }
            }
        }
    }

    private fun changeTheme(value: UiTheme) {
        themeRepository.changeUiTheme(value)
        viewModelScope.launch {
            val settings =
                settingsRepository.currentSettings.value.copy(theme = value.toInt())
            saveSettings(settings)
        }
    }

    private fun changeFontFamily(value: UiFontFamily) {
        themeRepository.changeUiFontFamily(value)
        viewModelScope.launch {
            val settings =
                settingsRepository.currentSettings.value.copy(
                    uiFontFamily = value.toInt(),
                )
            saveSettings(settings)
        }
    }

    private fun changeUiFontScale(value: Float) {
        themeRepository.changeUiFontScale(value)
        viewModelScope.launch {
            val settings =
                settingsRepository.currentSettings.value.copy(
                    uiFontScale = value,
                )
            saveSettings(settings)
        }
    }

    private fun changeDynamicColors(value: Boolean) {
        themeRepository.changeDynamicColors(value)
        viewModelScope.launch {
            val oldSettings = settingsRepository.currentSettings.value
            val newRandomThemeColor = if (value) false else oldSettings.randomThemeColor
            val settings =
                oldSettings.copy(
                    dynamicColors = value,
                    randomThemeColor = newRandomThemeColor,
                )
            saveSettings(settings)
        }
    }

    private fun changeRandomColor(value: Boolean) {
        viewModelScope.launch {
            val oldSettings = settingsRepository.currentSettings.value
            val newDynamicColors = if (value) false else oldSettings.dynamicColors
            themeRepository.changeDynamicColors(newDynamicColors)
            val settings =
                oldSettings.copy(
                    randomThemeColor = value,
                    dynamicColors = newDynamicColors,
                )
            saveSettings(settings)
        }
    }

    private fun changeCustomSeedColor(value: Color?) {
        themeRepository.changeCustomSeedColor(value)
        viewModelScope.launch {
            val settings =
                settingsRepository.currentSettings.value.copy(
                    customSeedColor = value?.toArgb(),
                )
            saveSettings(settings)
        }
    }

    private fun changeUpVoteColor(value: Color?) {
        themeRepository.changeUpVoteColor(value)
        viewModelScope.launch {
            val settings =
                settingsRepository.currentSettings.value.copy(
                    upVoteColor = value?.toArgb(),
                )
            saveSettings(settings)
        }
    }

    private fun changeDownVoteColor(value: Color?) {
        themeRepository.changeDownVoteColor(value)
        viewModelScope.launch {
            val settings =
                settingsRepository.currentSettings.value.copy(
                    downVoteColor = value?.toArgb(),
                )
            saveSettings(settings)
        }
    }

    private fun changeReplyColor(value: Color?) {
        themeRepository.changeReplyColor(value)
        viewModelScope.launch {
            val settings =
                settingsRepository.currentSettings.value.copy(
                    replyColor = value?.toArgb(),
                )
            saveSettings(settings)
        }
    }

    private fun changeSaveColor(value: Color?) {
        themeRepository.changeSaveColor(value)
        viewModelScope.launch {
            val settings =
                settingsRepository.currentSettings.value.copy(
                    saveColor = value?.toArgb(),
                )
            saveSettings(settings)
        }
    }

    private fun changeCommentBarTheme(value: CommentBarTheme) {
        themeRepository.changeCommentBarTheme(value)
        viewModelScope.launch {
            val settings =
                settingsRepository.currentSettings.value.copy(
                    commentBarTheme = value.toInt(),
                )
            saveSettings(settings)
        }
    }

    private suspend fun saveSettings(settings: SettingsModel) {
        val accountId = accountRepository.getActive()?.id
        settingsRepository.updateSettings(settings, accountId)
        settingsRepository.changeCurrentSettings(settings)
    }
}
