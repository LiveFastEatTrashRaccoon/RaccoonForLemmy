package com.livefast.eattrash.raccoonforlemmy.unit.drawer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.livefast.eattrash.raccoonforlemmy.core.appearance.di.getThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.SearchField
import com.livefast.eattrash.raccoonforlemmy.core.l10n.messages.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.DrawerEvent
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getDrawerCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.notifications.di.getNotificationCenter
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.ListingType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.readableHandle
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.readableName
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toIcon
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toReadableName
import com.livefast.eattrash.raccoonforlemmy.unit.drawer.components.DrawerCommunityItem
import com.livefast.eattrash.raccoonforlemmy.unit.drawer.components.DrawerHeader
import com.livefast.eattrash.raccoonforlemmy.unit.drawer.components.DrawerShortcut
import com.livefast.eattrash.raccoonforlemmy.unit.login.LoginBottomSheet
import com.livefast.eattrash.raccoonforlemmy.unit.manageaccounts.ManageAccountsBottomSheet
import com.livefast.eattrash.raccoonforlemmy.unit.selectinstance.SelectInstanceBottomSheet
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

object ModalDrawerContent : Tab {
    override val options: TabOptions
        @Composable get() {
            return TabOptions(0u, "")
        }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val model = getScreenModel<ModalDrawerMviModel>()
        val uiState by model.uiState.collectAsState()
        val coordinator = remember { getDrawerCoordinator() }
        val themeRepository = remember { getThemeRepository() }
        val scope = rememberCoroutineScope()
        val navigationCoordinator = remember { getNavigationCoordinator() }
        val notificationCenter = remember { getNotificationCenter() }
        val focusManager = LocalFocusManager.current
        val keyboardScrollConnection =
            remember {
                object : NestedScrollConnection {
                    override fun onPreScroll(
                        available: Offset,
                        source: NestedScrollSource,
                    ): Offset {
                        focusManager.clearFocus()
                        return Offset.Zero
                    }
                }
            }
        var selectInstanceBottomSheetOpened by remember { mutableStateOf(false) }
        var manageAccountsBottomSheetOpened by remember { mutableStateOf(false) }

        var uiFontSizeWorkaround by remember { mutableStateOf(true) }
        LaunchedEffect(themeRepository) {
            themeRepository.uiFontScale
                .drop(1)
                .onEach {
                    uiFontSizeWorkaround = false
                    delay(50)
                    uiFontSizeWorkaround = true
                }.launchIn(this)
        }
        if (!uiFontSizeWorkaround) {
            return
        }

        LaunchedEffect(notificationCenter) {
            notificationCenter
                .subscribe(NotificationCenterEvent.InstanceSelected::class)
                .onEach {
                    // closes the navigation drawer after instance change
                    coordinator.closeDrawer()
                }.launchIn(this)
        }

        ModalDrawerSheet {
            DrawerHeader(
                user = uiState.user,
                instance = uiState.instance,
                autoLoadImages = uiState.autoLoadImages,
                onOpenChangeInstance = {
                    selectInstanceBottomSheetOpened = true
                },
                onOpenSwitchAccount = {
                    manageAccountsBottomSheetOpened = true
                },
            )

            HorizontalDivider(
                modifier =
                    Modifier
                        .padding(
                            top = Spacing.s,
                            bottom = Spacing.s,
                        ),
            )

            if (uiState.user != null) {
                PullToRefreshBox(
                    modifier =
                        Modifier
                            .weight(1f)
                            .nestedScroll(keyboardScrollConnection),
                    isRefreshing = uiState.refreshing,
                    onRefresh = {
                        model.reduce(ModalDrawerMviModel.Intent.Refresh)
                    },
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(horizontal = Spacing.xxs),
                        verticalArrangement = Arrangement.spacedBy(Spacing.xxs),
                    ) {
                        item {
                            SearchField(
                                modifier =
                                    Modifier
                                        .scale(0.95f)
                                        .padding(
                                            horizontal = Spacing.xxs,
                                            vertical = Spacing.xxs,
                                        ).fillMaxWidth(),
                                hint = LocalStrings.current.exploreSearchPlaceholder,
                                value = uiState.searchText,
                                onValueChange = { value ->
                                    model.reduce(ModalDrawerMviModel.Intent.SetSearch(value))
                                },
                                onClear = {
                                    model.reduce(ModalDrawerMviModel.Intent.SetSearch(""))
                                },
                            )
                        }

                        if (!uiState.isFiltering) {
                            val listingTypes =
                                listOf(
                                    ListingType.Subscribed,
                                    ListingType.All,
                                    ListingType.Local,
                                )
                            for (listingType in listingTypes) {
                                item {
                                    DrawerShortcut(
                                        title = listingType.toReadableName(),
                                        icon = listingType.toIcon(),
                                        onSelected = {
                                            scope.launch {
                                                focusManager.clearFocus()
                                                navigationCoordinator.popUntilRoot()
                                                coordinator.toggleDrawer()
                                                delay(50)
                                                coordinator.sendEvent(
                                                    DrawerEvent.ChangeListingType(listingType),
                                                )
                                            }
                                        },
                                    )
                                }
                            }
                            if (uiState.isSettingsVisible) {
                                item {
                                    DrawerShortcut(
                                        title = LocalStrings.current.navigationSettings,
                                        icon = Icons.Default.Settings,
                                        onSelected = {
                                            scope.launch {
                                                focusManager.clearFocus()
                                                navigationCoordinator.popUntilRoot()
                                                coordinator.toggleDrawer()
                                                delay(50)

                                                coordinator.sendEvent(DrawerEvent.OpenSettings)
                                            }
                                        },
                                    )
                                }
                            }
                        }

                        items(
                            items = uiState.multiCommunities,
                            key = { it.communityIds.joinToString() },
                        ) { community ->
                            DrawerCommunityItem(
                                title = community.name,
                                url = community.icon,
                                autoLoadImages = uiState.autoLoadImages,
                                onSelected = {
                                    focusManager.clearFocus()
                                    scope.launch {
                                        coordinator.sendEvent(
                                            DrawerEvent.OpenMultiCommunity(community),
                                        )
                                        coordinator.toggleDrawer()
                                    }
                                },
                            )
                        }

                        items(
                            items = uiState.favorites,
                            key = { "${it.id}-favorite" },
                        ) { community ->
                            DrawerCommunityItem(
                                title = community.readableName(uiState.preferNicknames),
                                subtitle = community.readableHandle,
                                url = community.icon,
                                favorite = true,
                                autoLoadImages = uiState.autoLoadImages,
                                onSelected = {
                                    scope.launch {
                                        focusManager.clearFocus()
                                        coordinator.toggleDrawer()
                                        coordinator.sendEvent(
                                            DrawerEvent.OpenCommunity(community),
                                        )
                                    }
                                },
                                onToggleFavorite =
                                    {
                                        model.reduce(
                                            ModalDrawerMviModel.Intent.ToggleFavorite(community.id),
                                        )
                                    }.takeIf { uiState.enableToggleFavorite },
                            )
                        }

                        items(
                            items = uiState.communities,
                            key = { "${it.id}-community" },
                        ) { community ->
                            DrawerCommunityItem(
                                title = community.readableName(uiState.preferNicknames),
                                subtitle = community.readableHandle,
                                url = community.icon,
                                favorite = false,
                                autoLoadImages = uiState.autoLoadImages,
                                onSelected = {
                                    scope.launch {
                                        focusManager.clearFocus()
                                        coordinator.toggleDrawer()
                                        coordinator.sendEvent(
                                            DrawerEvent.OpenCommunity(community),
                                        )
                                    }
                                },
                                onToggleFavorite =
                                    {
                                        model.reduce(
                                            ModalDrawerMviModel.Intent.ToggleFavorite(community.id),
                                        )
                                    }.takeIf { uiState.enableToggleFavorite },
                            )
                        }
                    }
                }
            } else {
                Text(
                    modifier = Modifier.padding(horizontal = Spacing.s, vertical = Spacing.s),
                    text = LocalStrings.current.sidebarNotLoggedMessage,
                    style = MaterialTheme.typography.bodySmall,
                )

                Text(
                    modifier = Modifier.padding(horizontal = Spacing.s, vertical = Spacing.s),
                    text = LocalStrings.current.homeListingTitle,
                    style = MaterialTheme.typography.titleMedium,
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = Spacing.xxs),
                    verticalArrangement = Arrangement.spacedBy(Spacing.xxs),
                ) {
                    for (listingType in listOf(
                        ListingType.All,
                        ListingType.Local,
                    )) {
                        item {
                            DrawerShortcut(
                                title = listingType.toReadableName(),
                                icon = listingType.toIcon(),
                                onSelected = {
                                    scope.launch {
                                        coordinator.toggleDrawer()
                                        navigationCoordinator.popUntilRoot()
                                        coordinator.toggleDrawer()
                                        delay(50)
                                        coordinator.sendEvent(
                                            DrawerEvent.ChangeListingType(listingType),
                                        )
                                    }
                                },
                            )
                        }
                    }

                    item {
                        DrawerShortcut(
                            title = LocalStrings.current.navigationSettings,
                            icon = Icons.Default.Settings,
                            onSelected = {
                                scope.launch {
                                    focusManager.clearFocus()
                                    navigationCoordinator.popUntilRoot()
                                    coordinator.toggleDrawer()
                                    delay(50)

                                    coordinator.sendEvent(DrawerEvent.OpenSettings)
                                }
                            },
                        )
                    }
                }
            }
        }

        if (selectInstanceBottomSheetOpened) {
            SelectInstanceBottomSheet(
                parent = this,
                state = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                onSelected = { instance ->
                    selectInstanceBottomSheetOpened = false
                    if (instance != null) {
                        notificationCenter.send(NotificationCenterEvent.InstanceSelected(instance))
                    }
                },
            )
        }

        if (manageAccountsBottomSheetOpened) {
            ManageAccountsBottomSheet(
                parent = this,
                onDismiss = { openLogin ->
                    manageAccountsBottomSheetOpened = false
                    if (openLogin) {
                        navigationCoordinator.pushScreen(LoginBottomSheet())
                    }
                },
            )
        }
    }
}
