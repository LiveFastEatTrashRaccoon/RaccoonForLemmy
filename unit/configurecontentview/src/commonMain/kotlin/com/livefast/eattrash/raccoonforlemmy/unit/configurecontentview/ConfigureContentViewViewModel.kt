package com.livefast.eattrash.raccoonforlemmy.unit.configurecontentview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.PostLayout
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.UiFontFamily
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.VoteFormat
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.toInt
import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.ContentFontClass
import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.ThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.SelectNumberBottomSheetType
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.toSelectNumberBottomSheetType
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.SettingsModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.AccountRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.LemmyValueCache
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

private val COMMENT_BAR_THICKNESS_RANGE = 1..5
private val COMMENT_INDENT_AMOUNT_RANGE = 1..20

class ConfigureContentViewViewModel(
    private val themeRepository: ThemeRepository,
    private val settingsRepository: SettingsRepository,
    private val accountRepository: AccountRepository,
    private val notificationCenter: NotificationCenter,
    private val lemmyValueCache: LemmyValueCache,
) : ViewModel(),
    MviModelDelegate<
        ConfigureContentViewMviModel.Intent,
        ConfigureContentViewMviModel.UiState,
        ConfigureContentViewMviModel.Effect,
        >
    by DefaultMviModelDelegate(initialState = ConfigureContentViewMviModel.UiState()),
    ConfigureContentViewMviModel {
    init {
        viewModelScope.launch {
            themeRepository.postLayout
                .onEach { value ->
                    updateState { it.copy(postLayout = value) }
                }.launchIn(this)
            themeRepository.contentFontScale
                .onEach { value ->
                    updateState { it.copy(contentFontScale = value) }
                }.launchIn(this)
            themeRepository.contentFontFamily
                .onEach { value ->
                    updateState { it.copy(contentFontFamily = value) }
                }.launchIn(this)

            notificationCenter
                .subscribe(NotificationCenterEvent.ChangePostLayout::class)
                .onEach { evt ->
                    changePostLayout(evt.value)
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.ChangeVoteFormat::class)
                .onEach { evt ->
                    changeVoteFormat(evt.value)
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.SelectNumberBottomSheetClosed::class)
                .onEach { evt ->
                    if (evt.type.toSelectNumberBottomSheetType() == SelectNumberBottomSheetType.PostBodyMaxLines) {
                        changePostBodyMaxLines(evt.value)
                    }
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.ChangeContentFontSize::class)
                .onEach { evt ->
                    changeContentFontScale(evt.value, evt.contentClass)
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.ChangeContentFontFamily::class)
                .onEach { evt ->
                    changeContentFontFamily(evt.value)
                }.launchIn(this)
            lemmyValueCache.isDownVoteEnabled
                .onEach { value ->
                    updateState {
                        it.copy(downVoteEnabled = value)
                    }
                }.launchIn(this)

            val settings = settingsRepository.currentSettings.value
            updateState {
                it.copy(
                    voteFormat = if (!settings.showScores) VoteFormat.Hidden else settings.voteFormat,
                    fullHeightImages = settings.fullHeightImages,
                    fullWidthImages = settings.fullWidthImages,
                    postBodyMaxLines = settings.postBodyMaxLines,
                    preferUserNicknames = settings.preferUserNicknames,
                    commentBarThickness = settings.commentBarThickness,
                    commentIndentAmount = settings.commentIndentAmount,
                )
            }
        }
    }

    override fun reduce(intent: ConfigureContentViewMviModel.Intent) {
        when (intent) {
            is ConfigureContentViewMviModel.Intent.ChangeFullHeightImages -> {
                changeFullHeightImages(
                    intent.value,
                )
            }

            is ConfigureContentViewMviModel.Intent.ChangeFullWidthImages -> {
                changeFullWidthImages(
                    intent.value,
                )
            }

            is ConfigureContentViewMviModel.Intent.ChangePreferUserNicknames -> {
                changePreferUserNicknames(
                    intent.value,
                )
            }

            ConfigureContentViewMviModel.Intent.IncrementCommentBarThickness -> {
                val value = (uiState.value.commentBarThickness + 1).coerceIn(COMMENT_BAR_THICKNESS_RANGE)
                changeCommentBarThickness(value)
            }

            ConfigureContentViewMviModel.Intent.DecrementCommentBarThickness -> {
                val value = (uiState.value.commentBarThickness - 1).coerceIn(COMMENT_BAR_THICKNESS_RANGE)
                changeCommentBarThickness(value)
            }

            ConfigureContentViewMviModel.Intent.IncrementCommentIndentAmount -> {
                val value = (uiState.value.commentIndentAmount + 1).coerceIn(COMMENT_INDENT_AMOUNT_RANGE)
                changeCommentIndentAmount(value)
            }

            ConfigureContentViewMviModel.Intent.DecrementCommentIndentAmount -> {
                val value = (uiState.value.commentIndentAmount - 1).coerceIn(COMMENT_INDENT_AMOUNT_RANGE)
                changeCommentIndentAmount(value)
            }
        }
    }

    private fun changePostLayout(value: PostLayout) {
        themeRepository.changePostLayout(value)
        viewModelScope.launch {
            val settings =
                settingsRepository.currentSettings.value.copy(
                    postLayout = value.toInt(),
                )
            saveSettings(settings)
        }
    }

    private fun changeVoteFormat(value: VoteFormat) {
        viewModelScope.launch {
            updateState { it.copy(voteFormat = value) }
            val settings =
                settingsRepository.currentSettings.value.let {
                    if (value == VoteFormat.Hidden) {
                        it.copy(showScores = false)
                    } else {
                        it.copy(
                            voteFormat = value,
                            showScores = true,
                        )
                    }
                }
            saveSettings(settings)
        }
    }

    private fun changeFullHeightImages(value: Boolean) {
        viewModelScope.launch {
            updateState { it.copy(fullHeightImages = value) }
            val settings =
                settingsRepository.currentSettings.value.copy(
                    fullHeightImages = value,
                )
            saveSettings(settings)
        }
    }

    private fun changeFullWidthImages(value: Boolean) {
        viewModelScope.launch {
            updateState { it.copy(fullWidthImages = value) }
            val settings =
                settingsRepository.currentSettings.value.copy(
                    fullWidthImages = value,
                )
            saveSettings(settings)
        }
    }

    private fun changePreferUserNicknames(value: Boolean) {
        viewModelScope.launch {
            updateState { it.copy(preferUserNicknames = value) }
            val settings =
                settingsRepository.currentSettings.value.copy(
                    preferUserNicknames = value,
                )
            saveSettings(settings)
        }
    }

    private fun changePostBodyMaxLines(value: Int?) {
        viewModelScope.launch {
            updateState { it.copy(postBodyMaxLines = value) }
            val settings =
                settingsRepository.currentSettings.value.copy(
                    postBodyMaxLines = value,
                )
            saveSettings(settings)
        }
    }

    private fun changeContentFontScale(value: Float, contentClass: ContentFontClass) {
        val contentFontScale =
            themeRepository.contentFontScale.value.let {
                when (contentClass) {
                    ContentFontClass.Title -> it.copy(title = value)
                    ContentFontClass.Body -> it.copy(body = value)
                    ContentFontClass.Comment -> it.copy(comment = value)
                    ContentFontClass.AncillaryText -> it.copy(ancillary = value)
                }
            }
        viewModelScope.launch {
            val settings =
                settingsRepository.currentSettings.value.copy(
                    contentFontScale = contentFontScale,
                )
            saveSettings(settings)
        }
    }

    private fun changeContentFontFamily(value: UiFontFamily) {
        themeRepository.changeContentFontFamily(value)
        viewModelScope.launch {
            val settings =
                settingsRepository.currentSettings.value.copy(
                    contentFontFamily = value.toInt(),
                )
            saveSettings(settings)
        }
    }

    private fun changeCommentBarThickness(value: Int) {
        viewModelScope.launch {
            updateState { it.copy(commentBarThickness = value) }
            val settings =
                settingsRepository.currentSettings.value.copy(
                    commentBarThickness = value,
                )
            saveSettings(settings)
        }
    }

    private fun changeCommentIndentAmount(value: Int) {
        viewModelScope.launch {
            updateState { it.copy(commentIndentAmount = value) }
            val settings =
                settingsRepository.currentSettings.value.copy(
                    commentIndentAmount = value,
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
