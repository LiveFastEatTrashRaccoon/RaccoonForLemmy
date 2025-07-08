package com.livefast.eattrash.raccoonforlemmy.feature.profile.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.IconSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.toWindowInsets
import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.getViewModel
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.ModeratorZoneAction
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.di.getFabNestedScrollConnection
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.toIcon
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.toInt
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.toModeratorZoneAction
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.toReadableName
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.CustomModalBottomSheet
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.CustomModalBottomSheetItem
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getDrawerCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getMainRouter
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.notifications.di.getNotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.persistence.di.getSettingsRepository
import com.livefast.eattrash.raccoonforlemmy.feature.profile.menu.ProfileSideMenu
import com.livefast.eattrash.raccoonforlemmy.feature.profile.notlogged.ProfileNotLoggedScreen
import com.livefast.eattrash.raccoonforlemmy.unit.manageaccounts.ManageAccountsBottomSheet
import com.livefast.eattrash.raccoonforlemmy.unit.myaccount.ProfileLoggedMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.myaccount.ProfileLoggedScreen
import com.livefast.eattrash.raccoonforlemmy.unit.myaccount.ProfileLoggedViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileMainScreen(
    modifier: Modifier = Modifier,
    model: ProfileMainMviModel = getViewModel<ProfileMainViewModel>(),
    loggedModel: ProfileLoggedMviModel = getViewModel<ProfileLoggedViewModel>(),
    loggedLazyListState: LazyListState = rememberLazyListState(),
) {
    val uiState by model.uiState.collectAsState()
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)
    val drawerCoordinator = remember { getDrawerCoordinator() }
    val navigationCoordinator = remember { getNavigationCoordinator() }
    val mainRouter = remember { getMainRouter() }
    val settingsRepository = remember { getSettingsRepository() }
    val settings by settingsRepository.currentSettings.collectAsState()
    val connection = navigationCoordinator.getBottomBarScrollConnection()
    val scope = rememberCoroutineScope()
    val notificationCenter = remember { getNotificationCenter() }
    val fabNestedScrollConnection = remember { getFabNestedScrollConnection() }
    var logoutConfirmDialogOpened by remember { mutableStateOf(false) }
    var moderatorZoneBottomSheetOpened by remember { mutableStateOf(false) }
    var manageAccountsBottomSheetOpened by remember { mutableStateOf(false) }

    LaunchedEffect(notificationCenter) {
        notificationCenter
            .subscribe(NotificationCenterEvent.ModeratorZoneActionSelected::class)
            .onEach {
                val action = it.value.toModeratorZoneAction()
                when (action) {
                    ModeratorZoneAction.GlobalModLog -> {
                        mainRouter.openModlog()
                    }

                    ModeratorZoneAction.GlobalReports -> {
                        mainRouter.openReports()
                    }

                    ModeratorZoneAction.ModeratedContents -> {
                        mainRouter.openModeratedContents()
                    }
                }
            }.launchIn(this)

        notificationCenter
            .subscribe(NotificationCenterEvent.ProfileSideMenuAction::class)
            .onEach { evt ->
                when (evt) {
                    NotificationCenterEvent.ProfileSideMenuAction.ManageAccounts -> {
                        manageAccountsBottomSheetOpened = true
                    }

                    NotificationCenterEvent.ProfileSideMenuAction.ManageSubscriptions -> {
                        mainRouter.openManageSubscriptions()
                    }

                    NotificationCenterEvent.ProfileSideMenuAction.Bookmarks -> {
                        mainRouter.openBookmarks()
                    }

                    NotificationCenterEvent.ProfileSideMenuAction.Drafts -> {
                        mainRouter.openDrafts()
                    }

                    NotificationCenterEvent.ProfileSideMenuAction.Votes -> {
                        mainRouter.openVotes()
                    }

                    NotificationCenterEvent.ProfileSideMenuAction.ModeratorZone -> {
                        moderatorZoneBottomSheetOpened = true
                    }

                    NotificationCenterEvent.ProfileSideMenuAction.Logout -> {
                        logoutConfirmDialogOpened = true
                    }

                    NotificationCenterEvent.ProfileSideMenuAction.CreateCommunity -> {
                        mainRouter.openEditCommunity()
                    }
                }
            }.launchIn(this)
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        modifier = modifier,
        topBar = {
            TopAppBar(
                windowInsets = topAppBarState.toWindowInsets(),
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
                            contentDescription = LocalStrings.current.actionOpenSideMenu,
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
                                navigationCoordinator.openSideMenu { ProfileSideMenu() }
                            },
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.MenuOpen,
                                contentDescription = LocalStrings.current.actionOpenSideMenu,
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
                    if (connection != null && settings.hideNavigationBarWhileScrolling) {
                        Modifier.nestedScroll(connection)
                    } else {
                        Modifier
                    },
                ).then(
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
            when (logged) {
                true -> ProfileLoggedScreen(
                    model = loggedModel,
                    lazyListState = loggedLazyListState,
                )

                false -> ProfileNotLoggedScreen()
                else -> Unit
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
            onSelect = { index ->
                moderatorZoneBottomSheetOpened = false
                if (index != null) {
                    notificationCenter.send(
                        NotificationCenterEvent.ModeratorZoneActionSelected(values[index].toInt()),
                    )
                }
            },
        )
    }

    if (manageAccountsBottomSheetOpened) {
        ManageAccountsBottomSheet(
            onDismiss = { openLogin ->
                manageAccountsBottomSheetOpened = false
                if (openLogin) {
                    mainRouter.openLogin()
                }
            },
        )
    }
}
