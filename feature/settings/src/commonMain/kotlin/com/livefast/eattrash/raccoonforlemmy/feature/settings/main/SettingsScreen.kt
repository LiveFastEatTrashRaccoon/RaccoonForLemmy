package com.livefast.eattrash.raccoonforlemmy.feature.settings.main

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Explicit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.SettingsApplications
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.buildAnnotatedString
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.IconSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.toWindowInsets
import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.getViewModel
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.SettingsHeader
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.SettingsRow
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.SettingsSwitchRow
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.CustomModalBottomSheet
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.CustomModalBottomSheetItem
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.SortBottomSheet
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.l10n.Locales
import com.livefast.eattrash.raccoonforlemmy.core.l10n.toLanguageFlag
import com.livefast.eattrash.raccoonforlemmy.core.l10n.toLanguageName
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getDrawerCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getMainRouter
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.notifications.di.getNotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.utils.url.UrlOpeningMode
import com.livefast.eattrash.raccoonforlemmy.core.utils.url.toReadableName
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.ListingType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toIcon
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toReadableName
import com.livefast.eattrash.raccoonforlemmy.unit.about.AboutDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    model: SettingsMviModel = getViewModel<SettingsViewModel>(),
    scrollState: ScrollState = rememberScrollState(),
) {
    val uiState by model.uiState.collectAsState()
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)
    val notificationCenter = remember { getNotificationCenter() }
    val drawerCoordinator = remember { getDrawerCoordinator() }
    val navigationCoordinator = remember { getNavigationCoordinator() }
    val mainRouter = remember { getMainRouter() }
    var infoDialogOpened by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val uriHandler = LocalUriHandler.current
    var defaultListingTypeBottomSheetOpened by remember { mutableStateOf(false) }
    var urlOpeningBottomSheetOpened by remember { mutableStateOf(false) }
    var languageBottomSheetOpened by remember { mutableStateOf(false) }
    var sortPostBottomSheetOpened by remember { mutableStateOf(false) }
    var sortCommentsBottomSheetOpened by remember { mutableStateOf(false) }
    var sortCommentsProfileBottomSheetOpened by remember { mutableStateOf(false) }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        modifier = modifier,
        topBar = {
            TopAppBar(
                windowInsets = topAppBarState.toWindowInsets(),
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    if (navigationCoordinator.canPop.value) {
                        IconButton(
                            onClick = {
                                navigationCoordinator.pop()
                            },
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = LocalStrings.current.actionGoBack,
                            )
                        }
                    } else {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerCoordinator.toggleDrawer()
                                }
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = LocalStrings.current.actionOpenSideMenu,
                            )
                        }
                    }
                },
                title = {
                    Text(
                        modifier = Modifier.padding(horizontal = Spacing.s),
                        text = LocalStrings.current.navigationSettings,
                        style = MaterialTheme.typography.titleMedium,
                    )
                },
            )
        },
    ) { padding ->
        Box(
            modifier =
            Modifier
                .padding(
                    top = padding.calculateTopPadding(),
                ).nestedScroll(scrollBehavior.nestedScrollConnection),
        ) {
            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(scrollState),
            ) {
                SettingsHeader(
                    icon = Icons.Default.Style,
                    title = LocalStrings.current.settingsSectionAppearance,
                )

                // language
                SettingsRow(
                    title = LocalStrings.current.settingsLanguage,
                    annotatedValue =
                    buildAnnotatedString {
                        with(uiState.lang) {
                            append(toLanguageFlag())
                            append("  ")
                            append(toLanguageName())
                        }
                    },
                    onTap = {
                        languageBottomSheetOpened = true
                    },
                )

                // colors and fonts
                SettingsRow(
                    title = LocalStrings.current.settingsColorsAndFonts,
                    disclosureIndicator = true,
                    onTap = {
                        mainRouter.openColorAndFont()
                    },
                )

                // content view configuration
                SettingsRow(
                    title = LocalStrings.current.settingsConfigureContent,
                    disclosureIndicator = true,
                    onTap = {
                        mainRouter.openConfigureContentView()
                    },
                )

                SettingsHeader(
                    icon = Icons.Default.SettingsApplications,
                    title = LocalStrings.current.settingsSectionGeneral,
                )

                // default listing type
                SettingsRow(
                    title = LocalStrings.current.settingsDefaultListingType,
                    value = uiState.defaultListingType.toReadableName(),
                    onTap = {
                        defaultListingTypeBottomSheetOpened = true
                    },
                )

                // default post sort type
                SettingsRow(
                    title = LocalStrings.current.settingsDefaultPostSortType,
                    value = uiState.defaultPostSortType.toReadableName(),
                    onTap = {
                        sortPostBottomSheetOpened = true
                    },
                )

                // default comment sort type
                SettingsRow(
                    title = LocalStrings.current.settingsDefaultCommentSortType,
                    value = uiState.defaultCommentSortType.toReadableName(),
                    onTap = {
                        sortCommentsBottomSheetOpened = true
                    },
                )
                SettingsRow(
                    title = LocalStrings.current.settingsDefaultCommentSortTypeProfile,
                    value = uiState.defaultCommentSortTypeProfile.toReadableName(),
                    onTap = {
                        sortCommentsProfileBottomSheetOpened = true
                    },
                )

                if (uiState.isLogged) {
                    // swipe actions
                    SettingsSwitchRow(
                        title = LocalStrings.current.settingsEnableSwipeActions,
                        value = uiState.enableSwipeActions,
                        onChangeValue = { value ->
                            model.reduce(
                                SettingsMviModel.Intent.ChangeEnableSwipeActions(value),
                            )
                        },
                    )
                    SettingsRow(
                        title = LocalStrings.current.settingsConfigureSwipeActions,
                        disclosureIndicator = true,
                        onTap = {
                            mainRouter.openConfigureSwipeActions()
                        },
                    )
                }

                // URL open
                SettingsRow(
                    title = LocalStrings.current.settingsOpenUrlExternal,
                    value = uiState.urlOpeningMode.toReadableName(),
                    onTap = {
                        urlOpeningBottomSheetOpened = true
                    },
                )

                // advanced settings
                SettingsRow(
                    title = LocalStrings.current.settingsAdvanced,
                    disclosureIndicator = true,
                    onTap = {
                        mainRouter.openAdvancedSettings()
                    },
                )

                if (uiState.isLogged) {
                    SettingsHeader(
                        icon = Icons.Default.AdminPanelSettings,
                        title = LocalStrings.current.settingsSectionAccount,
                    )

                    // web preferences
                    SettingsRow(
                        title = LocalStrings.current.settingsWebPreferences,
                        disclosureIndicator = true,
                        onTap = {
                            mainRouter.openAccountSettings()
                        },
                    )

                    // uploaded media
                    if (uiState.supportsMediaList) {
                        SettingsRow(
                            title = LocalStrings.current.settingsMediaList,
                            disclosureIndicator = true,
                            onTap = {
                                mainRouter.openMediaList()
                            },
                        )
                    }

                    // bans and filters
                    SettingsRow(
                        title = LocalStrings.current.settingsManageBan,
                        disclosureIndicator = true,
                        onTap = {
                            mainRouter.openManageBans()
                        },
                    )

                    if (uiState.supportsHiddenPosts) {
                        SettingsRow(
                            title = LocalStrings.current.settingsHiddenPosts,
                            disclosureIndicator = true,
                            onTap = {
                                mainRouter.openHidden()
                            },
                        )
                    }

                    // user tags
                    SettingsRow(
                        title = LocalStrings.current.userTagsTitle,
                        disclosureIndicator = true,
                        onTap = {
                            mainRouter.openUserTags()
                        },
                    )
                }

                SettingsHeader(
                    icon = Icons.Default.Explicit,
                    title = LocalStrings.current.settingsSectionNsfw,
                )

                // NSFW options
                SettingsSwitchRow(
                    title = LocalStrings.current.settingsIncludeNsfw,
                    value = uiState.includeNsfw,
                    onChangeValue = { value ->
                        model.reduce(SettingsMviModel.Intent.ChangeIncludeNsfw(value))
                    },
                )
                SettingsSwitchRow(
                    title = LocalStrings.current.settingsBlurNsfw,
                    value = uiState.blurNsfw,
                    onChangeValue = { value ->
                        model.reduce(SettingsMviModel.Intent.ChangeBlurNsfw(value))
                    },
                )

                SettingsHeader(
                    icon = Icons.Default.BugReport,
                    title = LocalStrings.current.settingsSectionDebug,
                )

                // enable crash report
                SettingsSwitchRow(
                    title = LocalStrings.current.settingsEnableCrashReport,
                    value = uiState.crashReportEnabled,
                    onChangeValue = { value ->
                        model.reduce(SettingsMviModel.Intent.ChangeCrashReportEnabled(value))
                    },
                )

                // about
                SettingsRow(
                    title = LocalStrings.current.settingsAbout,
                    value = "",
                    disclosureIndicator = true,
                    onTap = {
                        infoDialogOpened = true
                    },
                )

                // user manual
                SettingsRow(
                    title = LocalStrings.current.settingsUserManual,
                    value = "",
                    disclosureIndicator = true,
                    onTap = {
                        uriHandler.openUri(SettingsConstants.USER_MANUAL_URL)
                    },
                )

                Spacer(modifier = Modifier.height(Spacing.xxxl))
            }
        }
    }

    if (infoDialogOpened) {
        AboutDialog(
            onDismiss = {
                infoDialogOpened = false
            },
        )
    }

    if (defaultListingTypeBottomSheetOpened) {
        val values =
            buildList {
                if (uiState.isLogged) {
                    this += ListingType.Subscribed
                }
                this += ListingType.All
                this += ListingType.Local
            }
        CustomModalBottomSheet(
            title = LocalStrings.current.inboxListingTypeTitle,
            items =
            values.map { value ->
                CustomModalBottomSheetItem(
                    label = value.toReadableName(),
                    trailingContent = {
                        Icon(
                            modifier = Modifier.size(IconSize.m),
                            imageVector = value.toIcon(),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground,
                        )
                    },
                )
            },
            onSelect = { index ->
                defaultListingTypeBottomSheetOpened = false
                if (index != null) {
                    model.reduce(SettingsMviModel.Intent.ChangeFeedType(value = values[index]))
                }
            },
        )
    }

    if (urlOpeningBottomSheetOpened) {
        val values =
            buildList {
                this += UrlOpeningMode.Internal
                if (uiState.customTabsEnabled) {
                    this += UrlOpeningMode.CustomTabs
                }
                this += UrlOpeningMode.External
            }
        CustomModalBottomSheet(
            title = LocalStrings.current.settingsOpenUrlExternal,
            items =
            values.map { value ->
                CustomModalBottomSheetItem(label = value.toReadableName())
            },
            onSelect = { index ->
                urlOpeningBottomSheetOpened = false
                if (index != null) {
                    model.reduce(SettingsMviModel.Intent.ChangeUrlOpeningMode(value = values[index]))
                }
            },
        )
    }

    if (languageBottomSheetOpened) {
        val values = Locales.ALL
        CustomModalBottomSheet(
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            title = LocalStrings.current.settingsLanguage,
            items =
            values.map { value ->
                CustomModalBottomSheetItem(
                    label =
                    buildString {
                        with(value) {
                            append(toLanguageFlag())
                            append("  ")
                            append(toLanguageName())
                        }
                    },
                )
            },
            onSelect = { index ->
                languageBottomSheetOpened = false
                if (index != null) {
                    model.reduce(SettingsMviModel.Intent.ChangeLanguage(value = values[index]))
                }
            },
        )
    }

    if (sortPostBottomSheetOpened) {
        SortBottomSheet(
            values = uiState.availableSortTypesForPosts,
            expandTop = true,
            onSelect = { value ->
                sortPostBottomSheetOpened = false
                if (value != null) {
                    model.reduce(SettingsMviModel.Intent.ChangePostSortType(value = value))
                }
            },
        )
    }

    if (sortCommentsBottomSheetOpened) {
        SortBottomSheet(
            values = uiState.availableSortTypesForComments,
            expandTop = false,
            onSelect = { value ->
                sortCommentsBottomSheetOpened = false
                if (value != null) {
                    model.reduce(SettingsMviModel.Intent.ChangeCommentSortType(value = value))
                }
            },
        )
    }
    if (sortCommentsProfileBottomSheetOpened) {
        SortBottomSheet(
            values = uiState.availableSortTypesForCommentsInProfile,
            expandTop = false,
            onSelect = { value ->
                sortCommentsProfileBottomSheetOpened = false
                if (value != null) {
                    model.reduce(SettingsMviModel.Intent.ChangeCommentSortTypeProfile(value = value))
                }
            },
        )
    }
}
