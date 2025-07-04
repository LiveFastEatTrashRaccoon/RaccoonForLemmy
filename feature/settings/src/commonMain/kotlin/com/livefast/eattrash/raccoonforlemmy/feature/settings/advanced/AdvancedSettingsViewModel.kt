package com.livefast.eattrash.raccoonforlemmy.feature.settings.advanced

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.UiBarTheme
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.toInt
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.toUiBarTheme
import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.ThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.BarColorProvider
import com.livefast.eattrash.raccoonforlemmy.core.architecture.DefaultMviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.architecture.MviModelDelegate
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.SelectNumberBottomSheetType
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.toSelectNumberBottomSheetType
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.SettingsModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.AccountRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.repository.SettingsRepository
import com.livefast.eattrash.raccoonforlemmy.core.persistence.usecase.ExportSettingsUseCase
import com.livefast.eattrash.raccoonforlemmy.core.persistence.usecase.ImportSettingsUseCase
import com.livefast.eattrash.raccoonforlemmy.core.preferences.appconfig.AppConfigStore
import com.livefast.eattrash.raccoonforlemmy.core.utils.appicon.AppIconManager
import com.livefast.eattrash.raccoonforlemmy.core.utils.appicon.AppIconVariant
import com.livefast.eattrash.raccoonforlemmy.core.utils.appicon.toAppIconVariant
import com.livefast.eattrash.raccoonforlemmy.core.utils.debug.AppInfoRepository
import com.livefast.eattrash.raccoonforlemmy.core.utils.fs.FileSystemManager
import com.livefast.eattrash.raccoonforlemmy.core.utils.gallery.GalleryHelper
import com.livefast.eattrash.raccoonforlemmy.core.utils.toInboxDefaultType
import com.livefast.eattrash.raccoonforlemmy.core.utils.toInboxUnreadOnly
import com.livefast.eattrash.raccoonforlemmy.domain.identity.repository.IdentityRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.ListingType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SearchResultType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toInt
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toListingType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toSearchResultType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.SiteRepository
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.time.Duration

class AdvancedSettingsViewModel(
    private val themeRepository: ThemeRepository,
    private val identityRepository: IdentityRepository,
    private val settingsRepository: SettingsRepository,
    private val accountRepository: AccountRepository,
    private val siteRepository: SiteRepository,
    private val notificationCenter: NotificationCenter,
    private val galleryHelper: GalleryHelper,
    private val appIconManager: AppIconManager,
    private val fileSystemManager: FileSystemManager,
    private val importSettings: ImportSettingsUseCase,
    private val exportSettings: ExportSettingsUseCase,
    private val appConfigStore: AppConfigStore,
    private val appInfoRepository: AppInfoRepository,
    private val barColorProvider: BarColorProvider,
) : ViewModel(),
    MviModelDelegate<AdvancedSettingsMviModel.Intent, AdvancedSettingsMviModel.UiState, AdvancedSettingsMviModel.Effect>
    by DefaultMviModelDelegate(initialState = AdvancedSettingsMviModel.UiState()),
    AdvancedSettingsMviModel {
    init {
        viewModelScope.launch {
            themeRepository.navItemTitles
                .onEach { value ->
                    updateState { it.copy(navBarTitlesVisible = value) }
                }.launchIn(this)

            identityRepository.isLogged
                .onEach { logged ->
                    updateState { it.copy(isLogged = logged ?: false) }
                }.launchIn(this)

            notificationCenter
                .subscribe(NotificationCenterEvent.ChangeZombieInterval::class)
                .onEach { evt ->
                    changeZombieModeInterval(evt.value)
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.ChangeFeedType::class)
                .onEach { evt ->
                    if (evt.screenKey == "advancedSettings") {
                        changeExploreType(evt.value)
                    }
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.ChangeZombieScrollAmount::class)
                .onEach { evt ->
                    changeZombieModeScrollAmount(evt.value)
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.ChangeInboxType::class)
                .onEach { evt ->
                    changeDefaultInboxUnreadOnly(evt.unreadOnly)
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.ChangeSystemBarTheme::class)
                .onEach { evt ->
                    changeSystemBarTheme(evt.value)
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.ChangeInboxBackgroundCheckPeriod::class)
                .onEach { evt ->
                    changeInboxBackgroundCheckPeriod(evt.value)
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.AppIconVariantSelected::class)
                .onEach { evt ->
                    changeAppIconVariant(evt.value.toAppIconVariant())
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.SelectNumberBottomSheetClosed::class)
                .onEach { evt ->
                    if (evt.type.toSelectNumberBottomSheetType() == SelectNumberBottomSheetType.InboxPreviewMaxLines) {
                        changeInboxPreviewMaxLines(evt.value)
                    }
                }.launchIn(this)
            notificationCenter
                .subscribe(NotificationCenterEvent.ChangeSearchResultType::class)
                .onEach { evt ->
                    if (evt.screenKey == "advancedSettings") {
                        changeExploreResultType(evt.value)
                    }
                }.launchIn(this)

            val isDebug = appInfoRepository.geInfo().isDebug
            appConfigStore.appConfig
                .map { conf -> conf.alternateMarkdownRenderingSettingsItemEnabled }
                .distinctUntilChanged()
                .onEach { itemEnabled ->
                    val itemVisible = itemEnabled || isDebug
                    updateState {
                        it.copy(alternateMarkdownRenderingItemVisible = itemVisible)
                    }
                }.launchIn(this)

            updateAvailableLanguages()

            val settings = settingsRepository.currentSettings.value
            updateState {
                it.copy(
                    defaultExploreType = settings.defaultExploreType.toListingType(),
                    defaultInboxUnreadOnly = settings.defaultInboxType.toInboxUnreadOnly(),
                    enableDoubleTapAction = settings.enableDoubleTapAction,
                    autoLoadImages = settings.autoLoadImages,
                    autoExpandComments = settings.autoExpandComments,
                    hideNavigationBarWhileScrolling = settings.hideNavigationBarWhileScrolling,
                    zombieModeInterval = settings.zombieModeInterval,
                    zombieModeScrollAmount = settings.zombieModeScrollAmount,
                    markAsReadWhileScrolling = settings.markAsReadWhileScrolling,
                    searchPostTitleOnly = settings.searchPostTitleOnly,
                    infiniteScrollDisabled = !settings.infiniteScrollEnabled,
                    systemBarTheme = settings.systemBarTheme.toUiBarTheme(),
                    imageSourceSupported = galleryHelper.supportsCustomPath,
                    imageSourcePath = settings.imageSourcePath,
                    defaultLanguageId = settings.defaultLanguageId,
                    appIconChangeSupported = appIconManager.supportsMultipleIcons,
                    fadeReadPosts = settings.fadeReadPosts,
                    showUnreadComments = settings.showUnreadComments,
                    inboxBackgroundCheckPeriod = settings.inboxBackgroundCheckPeriod,
                    supportSettingsImportExport = fileSystemManager.isSupported,
                    enableButtonsToScrollBetweenComments = settings.enableButtonsToScrollBetweenComments,
                    enableToggleFavoriteInNavDrawer = settings.enableToggleFavoriteInNavDrawer,
                    inboxPreviewMaxLines = settings.inboxPreviewMaxLines,
                    defaultExploreResultType = settings.defaultExploreResultType.toSearchResultType(),
                    useAvatarAsProfileNavigationIcon = settings.useAvatarAsProfileNavigationIcon,
                    openPostWebPageOnImageClick = settings.openPostWebPageOnImageClick,
                    enableAlternateMarkdownRendering = settings.enableAlternateMarkdownRendering,
                    restrictLocalUserSearch = settings.restrictLocalUserSearch,
                    isBarThemeSupported = barColorProvider.isBarThemeSupported,
                    isBarOpaqueThemeSupported = barColorProvider.isOpaqueThemeSupported,
                    markAsReadOnInteraction = settings.markAsReadOnInteraction,
                )
            }
        }
    }

    override fun reduce(intent: AdvancedSettingsMviModel.Intent) {
        when (intent) {
            is AdvancedSettingsMviModel.Intent.ChangeNavBarTitlesVisible ->
                changeNavBarTitlesVisible(intent.value)

            is AdvancedSettingsMviModel.Intent.ChangeEnableDoubleTapAction ->
                changeEnableDoubleTapAction(intent.value)

            is AdvancedSettingsMviModel.Intent.ChangeAutoLoadImages -> changeAutoLoadImages(intent.value)
            is AdvancedSettingsMviModel.Intent.ChangeAutoExpandComments ->
                changeAutoExpandComments(intent.value)

            is AdvancedSettingsMviModel.Intent.ChangeHideNavigationBarWhileScrolling ->
                changeHideNavigationBarWhileScrolling(intent.value)

            is AdvancedSettingsMviModel.Intent.ChangeMarkAsReadWhileScrolling ->
                changeMarkAsReadWhileScrolling(intent.value)

            is AdvancedSettingsMviModel.Intent.ChangeMarkAsReadOnInteraction ->
                changeMarkAsReadOnInteraction(intent.value)

            is AdvancedSettingsMviModel.Intent.ChangeSearchPostTitleOnly ->
                changeSearchPostTitleOnly(intent.value)

            is AdvancedSettingsMviModel.Intent.ChangeInfiniteScrollDisabled ->
                changeInfiniteScrollDisabled(intent.value)

            is AdvancedSettingsMviModel.Intent.ChangeImageSourcePath -> changeImageSourcePath(intent.value)
            is AdvancedSettingsMviModel.Intent.ChangeDefaultLanguage ->
                changeDefaultLanguageId(intent.value)

            is AdvancedSettingsMviModel.Intent.ChangeFadeReadPosts -> changeFadeReadPosts(intent.value)
            is AdvancedSettingsMviModel.Intent.ChangeShowUnreadComments ->
                changeShowUnreadPosts(intent.value)

            is AdvancedSettingsMviModel.Intent.ExportSettings -> handleExportSettings()
            is AdvancedSettingsMviModel.Intent.ImportSettings -> handleImportSettings(intent.content)
            is AdvancedSettingsMviModel.Intent.ChangeEnableButtonsToScrollBetweenComments ->
                changeEnableButtonsToScrollBetweenComments(intent.value)

            is AdvancedSettingsMviModel.Intent.ChangeEnableToggleFavoriteInNavDrawer ->
                changeEnableToggleFavoriteInNavDrawer(intent.value)

            is AdvancedSettingsMviModel.Intent.ChangeUseAvatarAsProfileNavigationIcon ->
                changeUseAvatarAsProfileNavigationIcon(intent.value)

            is AdvancedSettingsMviModel.Intent.ChangeOpenPostWebPageOnImageClick ->
                changeOpenPostWebPageOnImageClick(intent.value)

            is AdvancedSettingsMviModel.Intent.ChangeEnableAlternateMarkdownRendering ->
                changeEnableAlternateMarkdownRendering(intent.value)

            is AdvancedSettingsMviModel.Intent.ChangeRestrictLocalUserSearch ->
                changeRestrictLocalUserSearch(intent.value)
        }
    }

    private fun changeNavBarTitlesVisible(value: Boolean) {
        themeRepository.changeNavItemTitles(value)
        viewModelScope.launch {
            val settings =
                settingsRepository.currentSettings.value.copy(navigationTitlesVisible = value)
            saveSettings(settings)
        }
    }

    private fun changeEnableDoubleTapAction(value: Boolean) {
        viewModelScope.launch {
            updateState { it.copy(enableDoubleTapAction = value) }
            val settings =
                settingsRepository.currentSettings.value.copy(enableDoubleTapAction = value)
            saveSettings(settings)
        }
    }

    private fun changeAutoLoadImages(value: Boolean) {
        viewModelScope.launch {
            updateState { it.copy(autoLoadImages = value) }
            val settings = settingsRepository.currentSettings.value.copy(autoLoadImages = value)
            saveSettings(settings)
        }
    }

    private fun changeAutoExpandComments(value: Boolean) {
        viewModelScope.launch {
            updateState { it.copy(autoExpandComments = value) }
            val settings = settingsRepository.currentSettings.value.copy(autoExpandComments = value)
            saveSettings(settings)
        }
    }

    private fun changeHideNavigationBarWhileScrolling(value: Boolean) {
        viewModelScope.launch {
            updateState { it.copy(hideNavigationBarWhileScrolling = value) }
            val settings =
                settingsRepository.currentSettings.value.copy(hideNavigationBarWhileScrolling = value)
            saveSettings(settings)
        }
    }

    private fun changeMarkAsReadWhileScrolling(value: Boolean) {
        viewModelScope.launch {
            updateState { it.copy(markAsReadWhileScrolling = value) }
            val settings =
                settingsRepository.currentSettings.value.copy(markAsReadWhileScrolling = value)
            saveSettings(settings)
        }
    }

    private fun changeMarkAsReadOnInteraction(value: Boolean) {
        viewModelScope.launch {
            updateState { it.copy(markAsReadOnInteraction = value) }
            val settings =
                settingsRepository.currentSettings.value.copy(markAsReadOnInteraction = value)
            saveSettings(settings)
        }
    }

    private fun changeZombieModeInterval(value: Duration) {
        viewModelScope.launch {
            updateState { it.copy(zombieModeInterval = value) }
            val settings = settingsRepository.currentSettings.value.copy(zombieModeInterval = value)
            saveSettings(settings)
        }
    }

    private fun changeZombieModeScrollAmount(value: Float) {
        viewModelScope.launch {
            updateState { it.copy(zombieModeScrollAmount = value) }
            val settings =
                settingsRepository.currentSettings.value.copy(zombieModeScrollAmount = value)
            saveSettings(settings)
        }
    }

    private fun changeDefaultInboxUnreadOnly(value: Boolean) {
        viewModelScope.launch {
            updateState { it.copy(defaultInboxUnreadOnly = value) }
            val settings =
                settingsRepository.currentSettings.value.copy(defaultInboxType = value.toInboxDefaultType())
            saveSettings(settings)
            notificationCenter.send(NotificationCenterEvent.ResetInbox)
        }
    }

    private fun changeSearchPostTitleOnly(value: Boolean) {
        viewModelScope.launch {
            updateState { it.copy(searchPostTitleOnly = value) }
            val settings =
                settingsRepository.currentSettings.value.copy(searchPostTitleOnly = value)
            saveSettings(settings)
        }
    }

    private fun changeExploreType(value: ListingType) {
        viewModelScope.launch {
            updateState { it.copy(defaultExploreType = value) }
            val settings =
                settingsRepository.currentSettings.value.copy(defaultExploreType = value.toInt())
            saveSettings(settings)
            notificationCenter.send(NotificationCenterEvent.ResetExplore)
        }
    }

    private fun changeExploreResultType(value: SearchResultType) {
        viewModelScope.launch {
            updateState { it.copy(defaultExploreResultType = value) }
            val settings =
                settingsRepository.currentSettings.value.copy(defaultExploreResultType = value.toInt())
            saveSettings(settings)
            notificationCenter.send(NotificationCenterEvent.ResetExplore)
        }
    }

    private fun changeInfiniteScrollDisabled(value: Boolean) {
        viewModelScope.launch {
            updateState { it.copy(infiniteScrollDisabled = value) }
            val settings =
                settingsRepository.currentSettings.value.copy(infiniteScrollEnabled = !value)
            saveSettings(settings)
        }
    }

    private fun changeSystemBarTheme(value: UiBarTheme) {
        viewModelScope.launch {
            updateState { it.copy(systemBarTheme = value) }
            val settings =
                settingsRepository.currentSettings.value.copy(systemBarTheme = value.toInt())
            saveSettings(settings)
        }
    }

    private fun changeImageSourcePath(value: Boolean) {
        viewModelScope.launch {
            updateState { it.copy(imageSourcePath = value) }
            val settings = settingsRepository.currentSettings.value.copy(imageSourcePath = value)
            saveSettings(settings)
        }
    }

    private fun updateAvailableLanguages() {
        viewModelScope.launch {
            val auth = identityRepository.authToken.value
            val languages = siteRepository.getLanguages(auth)
            updateState { it.copy(availableLanguages = languages) }
        }
    }

    private fun changeDefaultLanguageId(value: Long?) {
        viewModelScope.launch {
            updateState { it.copy(defaultLanguageId = value) }
            val settings = settingsRepository.currentSettings.value.copy(defaultLanguageId = value)
            saveSettings(settings)
        }
    }

    private fun changeInboxBackgroundCheckPeriod(value: Duration?) {
        viewModelScope.launch {
            updateState { it.copy(inboxBackgroundCheckPeriod = value) }
            val settings =
                settingsRepository.currentSettings.value.copy(inboxBackgroundCheckPeriod = value)
            saveSettings(settings)
        }
    }

    private fun changeFadeReadPosts(value: Boolean) {
        viewModelScope.launch {
            updateState { it.copy(fadeReadPosts = value) }
            val settings = settingsRepository.currentSettings.value.copy(fadeReadPosts = value)
            saveSettings(settings)
        }
    }

    private fun changeShowUnreadPosts(value: Boolean) {
        viewModelScope.launch {
            updateState { it.copy(showUnreadComments = value) }
            val settings = settingsRepository.currentSettings.value.copy(showUnreadComments = value)
            saveSettings(settings)
        }
    }

    private fun changeEnableButtonsToScrollBetweenComments(value: Boolean) {
        viewModelScope.launch {
            updateState { it.copy(enableButtonsToScrollBetweenComments = value) }
            val settings =
                settingsRepository.currentSettings.value.copy(enableButtonsToScrollBetweenComments = value)
            saveSettings(settings)
        }
    }

    private fun changeEnableToggleFavoriteInNavDrawer(value: Boolean) {
        viewModelScope.launch {
            updateState { it.copy(enableToggleFavoriteInNavDrawer = value) }
            val settings =
                settingsRepository.currentSettings.value.copy(enableToggleFavoriteInNavDrawer = value)
            saveSettings(settings)
        }
    }

    private fun changeInboxPreviewMaxLines(value: Int?) {
        viewModelScope.launch {
            updateState { it.copy(inboxPreviewMaxLines = value) }
            val settings =
                settingsRepository.currentSettings.value.copy(inboxPreviewMaxLines = value)
            saveSettings(settings)
        }
    }

    private fun changeUseAvatarAsProfileNavigationIcon(value: Boolean) {
        viewModelScope.launch {
            updateState { it.copy(useAvatarAsProfileNavigationIcon = value) }
            val settings =
                settingsRepository.currentSettings.value.copy(useAvatarAsProfileNavigationIcon = value)
            saveSettings(settings)
        }
    }

    private fun changeOpenPostWebPageOnImageClick(value: Boolean) {
        viewModelScope.launch {
            updateState { it.copy(openPostWebPageOnImageClick = value) }
            val settings =
                settingsRepository.currentSettings.value.copy(openPostWebPageOnImageClick = value)
            saveSettings(settings)
        }
    }

    private fun changeEnableAlternateMarkdownRendering(value: Boolean) {
        viewModelScope.launch {
            updateState { it.copy(enableAlternateMarkdownRendering = value) }
            val settings =
                settingsRepository.currentSettings.value.copy(enableAlternateMarkdownRendering = value)
            saveSettings(settings)
        }
    }

    private fun changeRestrictLocalUserSearch(value: Boolean) {
        viewModelScope.launch {
            updateState { it.copy(restrictLocalUserSearch = value) }
            val settings =
                settingsRepository.currentSettings.value.copy(restrictLocalUserSearch = value)
            saveSettings(settings)
        }
    }

    private suspend fun saveSettings(settings: SettingsModel) {
        val accountId = accountRepository.getActive()?.id
        settingsRepository.updateSettings(settings, accountId)
        settingsRepository.changeCurrentSettings(settings)
    }

    private fun changeAppIconVariant(value: AppIconVariant) {
        appIconManager.changeIcon(value)
    }

    private fun handleImportSettings(content: String) {
        viewModelScope.launch {
            updateState { it.copy(loading = true) }
            importSettings(content)
            updateState { it.copy(loading = false) }
        }
    }

    private fun handleExportSettings() {
        viewModelScope.launch {
            updateState { it.copy(loading = true) }
            val content = exportSettings()
            updateState { it.copy(loading = false) }
            emitEffect(AdvancedSettingsMviModel.Effect.SaveSettings(content))
        }
    }
}
