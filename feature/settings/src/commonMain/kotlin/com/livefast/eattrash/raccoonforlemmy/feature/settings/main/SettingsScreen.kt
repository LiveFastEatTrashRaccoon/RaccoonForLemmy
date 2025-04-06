package com.livefast.eattrash.raccoonforlemmy.feature.settings.main

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
import androidx.compose.runtime.LaunchedEffect
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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.kodein.rememberScreenModel
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.IconSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.toWindowInsets
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
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.notifications.di.getNotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.utils.url.UrlOpeningMode
import com.livefast.eattrash.raccoonforlemmy.core.utils.url.toInt
import com.livefast.eattrash.raccoonforlemmy.core.utils.url.toReadableName
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.ListingType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toIcon
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toReadableName
import com.livefast.eattrash.raccoonforlemmy.feature.settings.advanced.AdvancedSettingsScreen
import com.livefast.eattrash.raccoonforlemmy.feature.settings.colors.SettingsColorAndFontScreen
import com.livefast.eattrash.raccoonforlemmy.unit.about.AboutDialog
import com.livefast.eattrash.raccoonforlemmy.unit.accountsettings.AccountSettingsScreen
import com.livefast.eattrash.raccoonforlemmy.unit.configurecontentview.ConfigureContentViewScreen
import com.livefast.eattrash.raccoonforlemmy.unit.configureswipeactions.ConfigureSwipeActionsScreen
import com.livefast.eattrash.raccoonforlemmy.unit.filteredcontents.FilteredContentsScreen
import com.livefast.eattrash.raccoonforlemmy.unit.filteredcontents.FilteredContentsType
import com.livefast.eattrash.raccoonforlemmy.unit.filteredcontents.toInt
import com.livefast.eattrash.raccoonforlemmy.unit.manageban.ManageBanScreen
import com.livefast.eattrash.raccoonforlemmy.unit.medialist.MediaListScreen
import com.livefast.eattrash.raccoonforlemmy.unit.usertags.list.UserTagsScreen
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SettingsScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val model: SettingsMviModel = rememberScreenModel()
        val uiState by model.uiState.collectAsState()
        val topAppBarState = rememberTopAppBarState()
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)
        val notificationCenter = remember { getNotificationCenter() }
        val drawerCoordinator = remember { getDrawerCoordinator() }
        val navigationCoordinator = remember { getNavigationCoordinator() }
        val scrollState = rememberScrollState()
        var infoDialogOpened by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        val uriHandler = LocalUriHandler.current
        var defaultListingTypeBottomSheetOpened by remember { mutableStateOf(false) }
        var urlOpeningBottomSheetOpened by remember { mutableStateOf(false) }
        var languageBottomSheetOpened by remember { mutableStateOf(false) }
        var sortPostBottomSheetOpened by remember { mutableStateOf(false) }
        var sortCommentsBottomSheetOpened by remember { mutableStateOf(false) }

        LaunchedEffect(notificationCenter) {
            notificationCenter
                .subscribe(NotificationCenterEvent.CloseDialog::class)
                .onEach {
                    infoDialogOpened = false
                }.launchIn(this)
        }

        Scaffold(
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            topBar = {
                TopAppBar(
                    windowInsets = topAppBarState.toWindowInsets(),
                    scrollBehavior = scrollBehavior,
                    navigationIcon = {
                        if (navigationCoordinator.canPop.value) {
                            IconButton(
                                onClick = {
                                    navigationCoordinator.popScreen()
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
                            navigationCoordinator.pushScreen(SettingsColorAndFontScreen())
                        },
                    )

                    // content view configuration
                    SettingsRow(
                        title = LocalStrings.current.settingsConfigureContent,
                        disclosureIndicator = true,
                        onTap = {
                            navigationCoordinator.pushScreen(ConfigureContentViewScreen())
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

                    if (uiState.isLogged) {
                        // swipe actions
                        SettingsSwitchRow(
                            title = LocalStrings.current.settingsEnableSwipeActions,
                            value = uiState.enableSwipeActions,
                            onValueChanged = { value ->
                                model.reduce(
                                    SettingsMviModel.Intent.ChangeEnableSwipeActions(value),
                                )
                            },
                        )
                        SettingsRow(
                            title = LocalStrings.current.settingsConfigureSwipeActions,
                            disclosureIndicator = true,
                            onTap = {
                                val screen = ConfigureSwipeActionsScreen()
                                navigationCoordinator.pushScreen(screen)
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
                            val screen = AdvancedSettingsScreen()
                            navigationCoordinator.pushScreen(screen)
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
                                val screen = AccountSettingsScreen()
                                navigationCoordinator.pushScreen(screen)
                            },
                        )

                        // uploaded media
                        if (uiState.supportsMediaList) {
                            SettingsRow(
                                title = LocalStrings.current.settingsMediaList,
                                disclosureIndicator = true,
                                onTap = {
                                    navigationCoordinator.pushScreen(MediaListScreen())
                                },
                            )
                        }

                        // bans and filters
                        SettingsRow(
                            title = LocalStrings.current.settingsManageBan,
                            disclosureIndicator = true,
                            onTap = {
                                val screen = ManageBanScreen()
                                navigationCoordinator.pushScreen(screen)
                            },
                        )

                        if (uiState.supportsHiddenPosts) {
                            SettingsRow(
                                title = LocalStrings.current.settingsHiddenPosts,
                                disclosureIndicator = true,
                                onTap = {
                                    val screen =
                                        FilteredContentsScreen(
                                            type = FilteredContentsType.Hidden.toInt(),
                                        )
                                    navigationCoordinator.pushScreen(screen)
                                },
                            )
                        }

                        // user tags
                        SettingsRow(
                            title = LocalStrings.current.userTagsTitle,
                            disclosureIndicator = true,
                            onTap = {
                                navigationCoordinator.pushScreen(UserTagsScreen())
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
                        onValueChanged = { value ->
                            model.reduce(SettingsMviModel.Intent.ChangeIncludeNsfw(value))
                        },
                    )
                    SettingsSwitchRow(
                        title = LocalStrings.current.settingsBlurNsfw,
                        value = uiState.blurNsfw,
                        onValueChanged = { value ->
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
                        onValueChanged = { value ->
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
            AboutDialog().Content()
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
                onSelected = { index ->
                    defaultListingTypeBottomSheetOpened = false
                    if (index != null) {
                        notificationCenter.send(
                            NotificationCenterEvent.ChangeFeedType(
                                value = values[index],
                                screenKey = "settings",
                            ),
                        )
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
                onSelected = { index ->
                    urlOpeningBottomSheetOpened = false
                    if (index != null) {
                        notificationCenter.send(
                            NotificationCenterEvent.ChangeUrlOpeningMode(
                                value = values[index].toInt(),
                            ),
                        )
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
                onSelected = { index ->
                    languageBottomSheetOpened = false
                    if (index != null) {
                        notificationCenter.send(
                            NotificationCenterEvent.ChangeLanguage(
                                value = values[index],
                            ),
                        )
                    }
                },
            )
        }

        if (sortPostBottomSheetOpened) {
            SortBottomSheet(
                values = uiState.availableSortTypesForPosts,
                expandTop = true,
                onSelected = { value ->
                    sortPostBottomSheetOpened = false
                    if (value != null) {
                        notificationCenter.send(
                            NotificationCenterEvent.ChangeSortType(
                                value = value,
                                screenKey = "settings",
                            ),
                        )
                    }
                },
            )
        }

        if (sortCommentsBottomSheetOpened) {
            SortBottomSheet(
                values = uiState.availableSortTypesForComments,
                expandTop = false,
                onSelected = { value ->
                    sortCommentsBottomSheetOpened = false
                    if (value != null) {
                        notificationCenter.send(
                            NotificationCenterEvent.ChangeCommentSortType(
                                value = value,
                                screenKey = "settings",
                            ),
                        )
                    }
                },
            )
        }
    }
}
