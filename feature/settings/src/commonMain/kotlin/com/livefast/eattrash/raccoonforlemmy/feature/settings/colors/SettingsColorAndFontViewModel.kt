package com.livefast.eattrash.raccoonforlemmy.feature.settings.colors

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import cafe.adriel.voyager.core.model.screenModelScope
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.CommentBarTheme
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.UiFontFamily
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.UiTheme
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.toInt
import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.ThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.ColorSchemeProvider
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModel
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
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
    private val notificationCenter: NotificationCenter,
) : DefaultMviModel<Intent, UiState, Effect>(
    initialState = UiState(),
),
    SettingsColorAndFontMviModel {
    init {
        screenModelScope.launch {
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

            notificationCenter
                .subscribe(NotificationCenterEvent.ChangeTheme::class)
                .onEach { evt ->
                    changeTheme(evt.value)
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.ChangeFontFamily::class)
                .onEach { evt ->
                    changeFontFamily(evt.value)
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.ChangeUiFontSize::class)
                .onEach { evt ->
                    changeUiFontScale(evt.value)
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.ChangeColor::class)
                .onEach { evt ->
                    changeCustomSeedColor(evt.color)
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.ChangeCommentBarTheme::class)
                .onEach { evt ->
                    changeCommentBarTheme(evt.value)
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.ChangeActionColor::class)
                .onEach { evt ->
                    when (evt.actionType) {
                        3 -> changeSaveColor(evt.color)
                        2 -> changeReplyColor(evt.color)
                        1 -> changeDownVoteColor(evt.color)
                        else -> changeUpVoteColor(evt.color)
                    }
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
        }
    }

    private fun changeTheme(value: UiTheme) {
        themeRepository.changeUiTheme(value)
        screenModelScope.launch {
            val settings =
                settingsRepository.currentSettings.value.copy(theme = value.toInt())
            saveSettings(settings)
        }
    }

    private fun changeFontFamily(value: UiFontFamily) {
        themeRepository.changeUiFontFamily(value)
        screenModelScope.launch {
            val settings =
                settingsRepository.currentSettings.value.copy(
                    uiFontFamily = value.toInt(),
                )
            saveSettings(settings)
        }
    }

    private fun changeUiFontScale(value: Float) {
        themeRepository.changeUiFontScale(value)
        screenModelScope.launch {
            val settings =
                settingsRepository.currentSettings.value.copy(
                    uiFontScale = value,
                )
            saveSettings(settings)
        }
    }

    private fun changeDynamicColors(value: Boolean) {
        themeRepository.changeDynamicColors(value)
        screenModelScope.launch {
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
        screenModelScope.launch {
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
        screenModelScope.launch {
            val settings =
                settingsRepository.currentSettings.value.copy(
                    customSeedColor = value?.toArgb(),
                )
            saveSettings(settings)
        }
    }

    private fun changeUpVoteColor(value: Color?) {
        themeRepository.changeUpVoteColor(value)
        screenModelScope.launch {
            val settings =
                settingsRepository.currentSettings.value.copy(
                    upVoteColor = value?.toArgb(),
                )
            saveSettings(settings)
        }
    }

    private fun changeDownVoteColor(value: Color?) {
        themeRepository.changeDownVoteColor(value)
        screenModelScope.launch {
            val settings =
                settingsRepository.currentSettings.value.copy(
                    downVoteColor = value?.toArgb(),
                )
            saveSettings(settings)
        }
    }

    private fun changeReplyColor(value: Color?) {
        themeRepository.changeReplyColor(value)
        screenModelScope.launch {
            val settings =
                settingsRepository.currentSettings.value.copy(
                    replyColor = value?.toArgb(),
                )
            saveSettings(settings)
        }
    }

    private fun changeSaveColor(value: Color?) {
        themeRepository.changeSaveColor(value)
        screenModelScope.launch {
            val settings =
                settingsRepository.currentSettings.value.copy(
                    saveColor = value?.toArgb(),
                )
            saveSettings(settings)
        }
    }

    private fun changeCommentBarTheme(value: CommentBarTheme) {
        themeRepository.changeCommentBarTheme(value)
        screenModelScope.launch {
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
