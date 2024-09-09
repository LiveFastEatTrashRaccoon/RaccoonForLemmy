package com.livefast.eattrash.raccoonforlemmy.feature.settings.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
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
import cafe.adriel.voyager.koin.getScreenModel
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.SettingsHeader
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.SettingsRow
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.SettingsSwitchRow
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.handleUrl
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.LanguageBottomSheet
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.ListingTypeBottomSheet
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.SortBottomSheet
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.UrlOpeningModeBottomSheet
import com.livefast.eattrash.raccoonforlemmy.core.l10n.messages.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getDrawerCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.notifications.di.getNotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.utils.toLanguageFlag
import com.livefast.eattrash.raccoonforlemmy.core.utils.toLanguageName
import com.livefast.eattrash.raccoonforlemmy.core.utils.url.UrlOpeningMode
import com.livefast.eattrash.raccoonforlemmy.core.utils.url.getCustomTabsHelper
import com.livefast.eattrash.raccoonforlemmy.core.utils.url.toReadableName
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toInt
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
import com.livefast.eattrash.raccoonforlemmy.unit.web.WebViewScreen
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SettingsScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val model = getScreenModel<SettingsMviModel>()
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
        val customTabsHelper = remember { getCustomTabsHelper() }

        LaunchedEffect(notificationCenter) {
            notificationCenter
                .subscribe(NotificationCenterEvent.CloseDialog::class)
                .onEach {
                    infoDialogOpened = false
                }.launchIn(this)
        }

        Scaffold(
            topBar = {
                TopAppBar(
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
                                    contentDescription = null,
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
                                    contentDescription = null,
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
                        ).navigationBarsPadding()
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(Spacing.xs),
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
                            val sheet = LanguageBottomSheet()
                            navigationCoordinator.showBottomSheet(sheet)
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
                            val sheet =
                                ListingTypeBottomSheet(
                                    isLogged = uiState.isLogged,
                                    screenKey = "settings",
                                )
                            navigationCoordinator.showBottomSheet(sheet)
                        },
                    )

                    // default post sort type
                    SettingsRow(
                        title = LocalStrings.current.settingsDefaultPostSortType,
                        value = uiState.defaultPostSortType.toReadableName(),
                        onTap = {
                            val sheet =
                                SortBottomSheet(
                                    values = uiState.availableSortTypesForPosts.map { it.toInt() },
                                    expandTop = true,
                                    screenKey = "settings",
                                )
                            navigationCoordinator.showBottomSheet(sheet)
                        },
                    )

                    // default comment sort type
                    SettingsRow(
                        title = LocalStrings.current.settingsDefaultCommentSortType,
                        value = uiState.defaultCommentSortType.toReadableName(),
                        onTap = {
                            val sheet =
                                SortBottomSheet(
                                    comments = true,
                                    values = uiState.availableSortTypesForComments.map { it.toInt() },
                                    screenKey = "settings",
                                )
                            navigationCoordinator.showBottomSheet(sheet)
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
                            val screen =
                                UrlOpeningModeBottomSheet(
                                    values =
                                        buildList {
                                            this += UrlOpeningMode.Internal
                                            if (uiState.customTabsEnabled) {
                                                this += UrlOpeningMode.CustomTabs
                                            }
                                            this += UrlOpeningMode.External
                                        },
                                )
                            navigationCoordinator.showBottomSheet(screen)
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

                        if (uiState.supportsMediaList) {
                            // uploaded media
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
                            navigationCoordinator.handleUrl(
                                url = "https://example.com",
                                openingMode = uiState.urlOpeningMode,
                                uriHandler = uriHandler,
                                customTabsHelper = customTabsHelper,
                                onOpenWeb = { url ->
                                    navigationCoordinator.pushScreen(WebViewScreen(url))
                                },
                            )
                        },
                    )

                    Spacer(modifier = Modifier.height(Spacing.xxxl))
                }
            }
        }

        if (infoDialogOpened) {
            AboutDialog().Content()
        }
    }
}
