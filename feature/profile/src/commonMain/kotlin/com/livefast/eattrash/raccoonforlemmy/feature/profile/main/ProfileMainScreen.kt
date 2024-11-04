package com.livefast.eattrash.raccoonforlemmy.feature.profile.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuOpen
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Dimensions
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.IconSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.ModeratorZoneAction
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.di.getFabNestedScrollConnection
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.toIcon
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.toInt
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.toModeratorZoneAction
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.toReadableName
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.CustomModalBottomSheet
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.CustomModalBottomSheetItem
import com.livefast.eattrash.raccoonforlemmy.core.l10n.messages.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getDrawerCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.notifications.di.getNotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.persistence.di.getSettingsRepository
import com.livefast.eattrash.raccoonforlemmy.core.utils.toLocalPixel
import com.livefast.eattrash.raccoonforlemmy.feature.profile.menu.ProfileSideMenuScreen
import com.livefast.eattrash.raccoonforlemmy.feature.profile.notlogged.ProfileNotLoggedScreen
import com.livefast.eattrash.raccoonforlemmy.unit.drafts.DraftsScreen
import com.livefast.eattrash.raccoonforlemmy.unit.editcommunity.EditCommunityScreen
import com.livefast.eattrash.raccoonforlemmy.unit.filteredcontents.FilteredContentsScreen
import com.livefast.eattrash.raccoonforlemmy.unit.filteredcontents.FilteredContentsType
import com.livefast.eattrash.raccoonforlemmy.unit.filteredcontents.toInt
import com.livefast.eattrash.raccoonforlemmy.unit.manageaccounts.ManageAccountsScreen
import com.livefast.eattrash.raccoonforlemmy.unit.managesubscriptions.ManageSubscriptionsScreen
import com.livefast.eattrash.raccoonforlemmy.unit.modlog.ModlogScreen
import com.livefast.eattrash.raccoonforlemmy.unit.myaccount.ProfileLoggedScreen
import com.livefast.eattrash.raccoonforlemmy.unit.reportlist.ReportListScreen
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

internal object ProfileMainScreen : Tab {
    override val options: TabOptions
        @Composable get() {
            return TabOptions(0u, "")
        }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val model = getScreenModel<ProfileMainMviModel>()
        val uiState by model.uiState.collectAsState()
        val topAppBarState = rememberTopAppBarState()
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)
        val drawerCoordinator = remember { getDrawerCoordinator() }
        val navigationCoordinator = remember { getNavigationCoordinator() }
        val settingsRepository = remember { getSettingsRepository() }
        val settings by settingsRepository.currentSettings.collectAsState()
        val scope = rememberCoroutineScope()
        val notificationCenter = remember { getNotificationCenter() }
        val fabNestedScrollConnection = remember { getFabNestedScrollConnection() }
        var logoutConfirmDialogOpened by remember { mutableStateOf(false) }
        var moderatorZoneBottomSheetOpened by remember { mutableStateOf(false) }

        LaunchedEffect(notificationCenter) {
            notificationCenter
                .subscribe(NotificationCenterEvent.ModeratorZoneActionSelected::class)
                .onEach {
                    val action = it.value.toModeratorZoneAction()
                    when (action) {
                        ModeratorZoneAction.GlobalModLog -> {
                            navigationCoordinator.pushScreen(ModlogScreen())
                        }

                        ModeratorZoneAction.GlobalReports -> {
                            navigationCoordinator.pushScreen(ReportListScreen())
                        }

                        ModeratorZoneAction.ModeratedContents -> {
                            val screen = FilteredContentsScreen(type = FilteredContentsType.Moderated.toInt())
                            navigationCoordinator.pushScreen(screen)
                        }
                    }
                }.launchIn(this)

            notificationCenter
                .subscribe(NotificationCenterEvent.ProfileSideMenuAction::class)
                .onEach { evt ->
                    when (evt) {
                        NotificationCenterEvent.ProfileSideMenuAction.ManageAccounts -> {
                            navigationCoordinator.showBottomSheet(ManageAccountsScreen())
                        }

                        NotificationCenterEvent.ProfileSideMenuAction.ManageSubscriptions -> {
                            navigationCoordinator.pushScreen(ManageSubscriptionsScreen())
                        }

                        NotificationCenterEvent.ProfileSideMenuAction.Bookmarks -> {
                            val screen =
                                FilteredContentsScreen(type = FilteredContentsType.Bookmarks.toInt())
                            navigationCoordinator.pushScreen(screen)
                        }

                        NotificationCenterEvent.ProfileSideMenuAction.Drafts -> {
                            navigationCoordinator.pushScreen(DraftsScreen())
                        }

                        NotificationCenterEvent.ProfileSideMenuAction.Votes -> {
                            val screen =
                                FilteredContentsScreen(type = FilteredContentsType.Votes.toInt())
                            navigationCoordinator.pushScreen(screen)
                        }

                        NotificationCenterEvent.ProfileSideMenuAction.ModeratorZone -> {
                            moderatorZoneBottomSheetOpened = true
                        }

                        NotificationCenterEvent.ProfileSideMenuAction.Logout -> {
                            logoutConfirmDialogOpened = true
                        }

                        NotificationCenterEvent.ProfileSideMenuAction.CreateCommunity -> {
                            navigationCoordinator.pushScreen(
                                EditCommunityScreen(),
                            )
                        }
                    }
                }.launchIn(this)
        }

        Scaffold(
            modifier = Modifier.padding(Spacing.xxs),
            topBar = {
                val maxTopInset = Dimensions.maxTopBarInset.toLocalPixel()
                var topInset by remember { mutableStateOf(maxTopInset) }
                snapshotFlow { topAppBarState.collapsedFraction }
                    .onEach {
                        topInset = maxTopInset * (1 - it)
                    }.launchIn(scope)

                TopAppBar(
                    windowInsets =
                        if (settings.edgeToEdge) {
                            WindowInsets(0, topInset.roundToInt(), 0, 0)
                        } else {
                            TopAppBarDefaults.windowInsets
                        },
                    scrollBehavior = scrollBehavior,
                    navigationIcon = {
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
                    },
                    title = {
                        Text(
                            modifier = Modifier.padding(horizontal = Spacing.s),
                            text = LocalStrings.current.navigationProfile,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    },
                    actions = {
                        if (uiState.logged == true) {
                            IconButton(
                                onClick = {
                                    navigationCoordinator.openSideMenu(
                                        ProfileSideMenuScreen(),
                                    )
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Default.MenuOpen,
                                    contentDescription = null,
                                )
                            }
                        }
                    },
                )
            },
        ) { padding ->
            Box(
                modifier =
                    Modifier
                        .padding(
                            top = padding.calculateTopPadding(),
                        ).nestedScroll(fabNestedScrollConnection)
                        .then(
                            if (settings.hideNavigationBarWhileScrolling) {
                                Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
                            } else {
                                Modifier
                            },
                        ),
                contentAlignment = Alignment.Center,
            ) {
                // wait until logging status is determined
                val logged = uiState.logged
                if (logged != null) {
                    val screens =
                        remember {
                            listOf(
                                ProfileNotLoggedScreen,
                                ProfileLoggedScreen,
                            )
                        }
                    val root = if (logged) screens[1] else screens[0]
                    TabNavigator(root) {
                        CurrentScreen()
                        val navigator = LocalTabNavigator.current
                        LaunchedEffect(model) {
                            model.uiState
                                .map { s -> s.logged }
                                .distinctUntilChanged()
                                .onEach { logged ->
                                    val index =
                                        when (logged) {
                                            true -> 1
                                            else -> 0
                                        }
                                    navigator.current = screens[index]
                                }.launchIn(this)
                        }
                    }
                }
            }
        }

        if (logoutConfirmDialogOpened) {
            AlertDialog(
                onDismissRequest = {
                    logoutConfirmDialogOpened = false
                },
                title = {
                    Text(
                        text = LocalStrings.current.actionLogout,
                        style = MaterialTheme.typography.titleMedium,
                    )
                },
                text = {
                    Text(text = LocalStrings.current.messageAreYouSure)
                },
                dismissButton = {
                    Button(
                        onClick = {
                            logoutConfirmDialogOpened = false
                        },
                    ) {
                        Text(text = LocalStrings.current.buttonCancel)
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            logoutConfirmDialogOpened = false
                            navigationCoordinator.closeSideMenu()
                            model.reduce(ProfileMainMviModel.Intent.Logout)
                        },
                    ) {
                        Text(text = LocalStrings.current.buttonConfirm)
                    }
                },
            )
        }

        if (moderatorZoneBottomSheetOpened) {
            val values =
                listOf(
                    ModeratorZoneAction.GlobalReports,
                    ModeratorZoneAction.GlobalModLog,
                    ModeratorZoneAction.ModeratedContents,
                )
            CustomModalBottomSheet(
                title = LocalStrings.current.moderatorZoneTitle,
                items =
                    values.map {
                        CustomModalBottomSheetItem(
                            label = it.toReadableName(),
                            trailingContent = {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(Spacing.s),
                                ) {
                                    Icon(
                                        modifier = Modifier.size(IconSize.m),
                                        imageVector = it.toIcon(),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onBackground,
                                    )
                                    Icon(
                                        imageVector = Icons.Default.ChevronRight,
                                        tint = MaterialTheme.colorScheme.onBackground,
                                        contentDescription = null,
                                    )
                                }
                            },
                        )
                    },
                onSelected = { index ->
                    moderatorZoneBottomSheetOpened = false
                    if (index != null) {
                        notificationCenter.send(
                            NotificationCenterEvent.ModeratorZoneActionSelected(values[index].toInt()),
                        )
                    }
                },
            )
        }
    }
}
