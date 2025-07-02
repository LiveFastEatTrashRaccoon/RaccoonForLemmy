package com.livefast.eattrash.raccoonforlemmy.unit.manageban

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import cafe.adriel.voyager.core.screen.Screen
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.toWindowInsets
import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.getViewModel
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.FloatingActionButtonMenu
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.FloatingActionButtonMenuItem
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.SearchField
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.SectionSelector
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.CommunityItem
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.CommunityItemPlaceholder
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.Option
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.OptionId
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.UserItem
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.di.getFabNestedScrollConnection
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.EditTextualInfoDialog
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.persistence.di.getSettingsRepository
import com.livefast.eattrash.raccoonforlemmy.unit.manageban.components.ManageBanItem
import com.livefast.eattrash.raccoonforlemmy.unit.manageban.components.ManageBanItemPlaceholder
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ManageBanScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val model: ManageBanMviModel = getViewModel<ManageBanViewModel>()
        val uiState by model.uiState.collectAsState()
        val navigationCoordinator = remember { getNavigationCoordinator() }
        val topAppBarState = rememberTopAppBarState()
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)
        val fabNestedScrollConnection = remember { getFabNestedScrollConnection() }
        val isFabVisible by fabNestedScrollConnection.isFabVisible.collectAsState()
        val snackbarHostState = remember { SnackbarHostState() }
        val settingsRepository = remember { getSettingsRepository() }
        val settings by settingsRepository.currentSettings.collectAsState()
        val lazyListState = rememberLazyListState()
        val focusManager = LocalFocusManager.current
        val keyboardScrollConnection =
            remember {
                object : NestedScrollConnection {
                    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                        focusManager.clearFocus()
                        return Offset.Zero
                    }
                }
            }
        val successMessage = LocalStrings.current.messageOperationSuccessful
        val errorMessage = LocalStrings.current.messageGenericError
        val scope = rememberCoroutineScope()
        var addDomainDialogOpen by remember { mutableStateOf(false) }
        var addStopWordDialogOpen by remember { mutableStateOf(false) }

        LaunchedEffect(model) {
            model.effects
                .onEach { evt ->
                    when (evt) {
                        is ManageBanMviModel.Effect.Failure -> {
                            snackbarHostState.showSnackbar(evt.message ?: errorMessage)
                        }

                        ManageBanMviModel.Effect.Success -> {
                            snackbarHostState.showSnackbar(successMessage)
                        }

                        ManageBanMviModel.Effect.BackToTop -> {
                            runCatching {
                                lazyListState.scrollToItem(0)
                                topAppBarState.heightOffset = 0f
                                topAppBarState.contentOffset = 0f
                            }
                        }
                    }
                }.launchIn(this)
        }

        Scaffold(
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            topBar = {
                TopAppBar(
                    windowInsets = topAppBarState.toWindowInsets(),
                    scrollBehavior = scrollBehavior,
                    title = {
                        Text(
                            modifier = Modifier.padding(horizontal = Spacing.s),
                            text = LocalStrings.current.settingsManageBan,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    },
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
                        }
                    },
                )
            },
            snackbarHost = {
                SnackbarHost(snackbarHostState) { data ->
                    Snackbar(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        snackbarData = data,
                    )
                }
            },
            floatingActionButton = {
                AnimatedVisibility(
                    visible = isFabVisible,
                    enter =
                    slideInVertically(
                        initialOffsetY = { it * 2 },
                    ),
                    exit =
                    slideOutVertically(
                        targetOffsetY = { it * 2 },
                    ),
                ) {
                    FloatingActionButtonMenu(
                        items =
                        buildList {
                            this +=
                                FloatingActionButtonMenuItem(
                                    icon = Icons.Default.ExpandLess,
                                    text = LocalStrings.current.actionBackToTop,
                                    onSelected = {
                                        scope.launch {
                                            runCatching {
                                                lazyListState.scrollToItem(0)
                                                topAppBarState.heightOffset = 0f
                                                topAppBarState.contentOffset = 0f
                                            }
                                        }
                                    },
                                )
                            when (uiState.section) {
                                ManageBanSection.Domains ->
                                    this +=
                                        FloatingActionButtonMenuItem(
                                            icon = Icons.Default.AddCircle,
                                            text = LocalStrings.current.buttonAdd,
                                            onSelected = {
                                                addDomainDialogOpen = true
                                            },
                                        )

                                ManageBanSection.StopWords ->
                                    this +=
                                        FloatingActionButtonMenuItem(
                                            icon = Icons.Default.AddCircle,
                                            text = LocalStrings.current.buttonAdd,
                                            onSelected = {
                                                addStopWordDialogOpen = true
                                            },
                                        )

                                else -> Unit
                            }
                        },
                    )
                }
            },
        ) { padding ->
            Column(
                modifier =
                Modifier
                    .padding(
                        top = padding.calculateTopPadding(),
                    ).then(
                        if (settings.hideNavigationBarWhileScrolling) {
                            Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
                        } else {
                            Modifier
                        },
                    ),
                verticalArrangement = Arrangement.spacedBy(Spacing.s),
            ) {
                SearchField(
                    modifier =
                    Modifier
                        .padding(
                            horizontal = Spacing.xs,
                            vertical = Spacing.s,
                        ).fillMaxWidth(),
                    hint = LocalStrings.current.exploreSearchPlaceholder,
                    value = uiState.searchText,
                    onValueChange = { value ->
                        model.reduce(ManageBanMviModel.Intent.SetSearch(value))
                    },
                    onClear = {
                        model.reduce(ManageBanMviModel.Intent.SetSearch(""))
                    },
                )

                SectionSelector(
                    modifier = Modifier.padding(vertical = Spacing.xs),
                    titles =
                    listOf(
                        LocalStrings.current.exploreResultTypeUsers,
                        LocalStrings.current.exploreResultTypeCommunities,
                        LocalStrings.current.settingsManageBanSectionInstances,
                        LocalStrings.current.settingsManageBanSectionDomains,
                        LocalStrings.current.settingsManageBanSectionStopWords,
                    ),
                    scrollable = true,
                    currentSection = uiState.section.toInt(),
                    onSectionSelected = { idx ->
                        val section = idx.toManageBanSection()
                        model.reduce(ManageBanMviModel.Intent.ChangeSection(section))
                    },
                )

                PullToRefreshBox(
                    modifier =
                    Modifier
                        .then(
                            if (settings.hideNavigationBarWhileScrolling) {
                                Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
                            } else {
                                Modifier
                            },
                        ).nestedScroll(keyboardScrollConnection),
                    isRefreshing = uiState.refreshing,
                    onRefresh = {
                        model.reduce(ManageBanMviModel.Intent.Refresh)
                    },
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = lazyListState,
                        verticalArrangement = Arrangement.spacedBy(Spacing.s),
                    ) {
                        when (uiState.section) {
                            ManageBanSection.Users -> {
                                if (uiState.bannedUsers.isEmpty()) {
                                    if (uiState.initial) {
                                        items(5) {
                                            CommunityItemPlaceholder()
                                        }
                                    } else {
                                        item {
                                            Text(
                                                modifier =
                                                Modifier
                                                    .fillMaxWidth()
                                                    .padding(top = Spacing.xs),
                                                textAlign = TextAlign.Center,
                                                text = LocalStrings.current.messageEmptyList,
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = MaterialTheme.colorScheme.onBackground,
                                            )
                                        }
                                    }
                                } else {
                                    items(
                                        items = uiState.bannedUsers,
                                        key = { it.id },
                                    ) { user ->
                                        UserItem(
                                            user = user,
                                            autoLoadImages = uiState.autoLoadImages,
                                            preferNicknames = uiState.preferNicknames,
                                            options =
                                            buildList {
                                                this +=
                                                    Option(
                                                        OptionId.Unban,
                                                        LocalStrings.current.settingsManageBanActionUnban,
                                                    )
                                            },
                                            onSelectOption = { optionId ->
                                                when (optionId) {
                                                    OptionId.Unban -> {
                                                        model.reduce(
                                                            ManageBanMviModel.Intent.UnblockUser(
                                                                user.id,
                                                            ),
                                                        )
                                                    }

                                                    else -> Unit
                                                }
                                            },
                                        )
                                    }
                                }
                            }

                            ManageBanSection.Communities -> {
                                if (uiState.bannedCommunities.isEmpty()) {
                                    if (uiState.initial) {
                                        items(5) {
                                            CommunityItemPlaceholder()
                                        }
                                    } else {
                                        item {
                                            Text(
                                                modifier =
                                                Modifier
                                                    .fillMaxWidth()
                                                    .padding(top = Spacing.xs),
                                                textAlign = TextAlign.Center,
                                                text = LocalStrings.current.messageEmptyList,
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = MaterialTheme.colorScheme.onBackground,
                                            )
                                        }
                                    }
                                } else {
                                    items(
                                        items = uiState.bannedCommunities,
                                        key = { it.id },
                                    ) { community ->
                                        CommunityItem(
                                            community = community,
                                            autoLoadImages = uiState.autoLoadImages,
                                            preferNicknames = uiState.preferNicknames,
                                            options =
                                            buildList {
                                                this +=
                                                    Option(
                                                        OptionId.Unban,
                                                        LocalStrings.current.settingsManageBanActionUnban,
                                                    )
                                            },
                                            onSelectOption = { optionId ->
                                                when (optionId) {
                                                    OptionId.Unban -> {
                                                        model.reduce(
                                                            ManageBanMviModel.Intent.UnblockCommunity(
                                                                community.id,
                                                            ),
                                                        )
                                                    }

                                                    else -> Unit
                                                }
                                            },
                                        )
                                    }
                                }
                            }

                            ManageBanSection.Instances -> {
                                if (uiState.bannedInstances.isEmpty()) {
                                    if (uiState.initial) {
                                        items(5) {
                                            ManageBanItemPlaceholder()
                                        }
                                    } else {
                                        item {
                                            Text(
                                                modifier =
                                                Modifier
                                                    .fillMaxWidth()
                                                    .padding(top = Spacing.xs),
                                                textAlign = TextAlign.Center,
                                                text = LocalStrings.current.messageEmptyList,
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = MaterialTheme.colorScheme.onBackground,
                                            )
                                        }
                                    }
                                } else {
                                    items(
                                        items = uiState.bannedInstances,
                                        key = { it.id },
                                    ) { instance ->
                                        ManageBanItem(
                                            title = instance.domain,
                                            options =
                                            buildList {
                                                this +=
                                                    Option(
                                                        OptionId.Unban,
                                                        LocalStrings.current.settingsManageBanActionUnban,
                                                    )
                                            },
                                            onSelectOption = { optionId ->
                                                when (optionId) {
                                                    OptionId.Unban -> {
                                                        model.reduce(
                                                            ManageBanMviModel.Intent.UnblockInstance(
                                                                instance.id,
                                                            ),
                                                        )
                                                    }

                                                    else -> Unit
                                                }
                                            },
                                        )
                                    }
                                }
                            }

                            ManageBanSection.Domains -> {
                                if (uiState.blockedDomains.isEmpty()) {
                                    if (uiState.initial) {
                                        items(5) {
                                            ManageBanItemPlaceholder()
                                        }
                                    } else {
                                        item {
                                            Text(
                                                modifier =
                                                Modifier
                                                    .fillMaxWidth()
                                                    .padding(top = Spacing.xs),
                                                textAlign = TextAlign.Center,
                                                text = LocalStrings.current.messageEmptyList,
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = MaterialTheme.colorScheme.onBackground,
                                            )
                                        }
                                    }
                                } else {
                                    items(
                                        items = uiState.blockedDomains,
                                        key = { it },
                                    ) { domain ->
                                        ManageBanItem(
                                            title = domain,
                                            options =
                                            buildList {
                                                this +=
                                                    Option(
                                                        OptionId.Unban,
                                                        LocalStrings.current.settingsManageBanActionUnban,
                                                    )
                                            },
                                            onSelectOption = { optionId ->
                                                when (optionId) {
                                                    OptionId.Unban -> {
                                                        model.reduce(
                                                            ManageBanMviModel.Intent.UnblockDomain(
                                                                domain,
                                                            ),
                                                        )
                                                    }

                                                    else -> Unit
                                                }
                                            },
                                        )
                                    }
                                }
                            }

                            ManageBanSection.StopWords -> {
                                if (uiState.stopWords.isEmpty()) {
                                    if (uiState.initial) {
                                        items(5) {
                                            ManageBanItemPlaceholder()
                                        }
                                    } else {
                                        item {
                                            Text(
                                                modifier =
                                                Modifier
                                                    .fillMaxWidth()
                                                    .padding(top = Spacing.xs),
                                                textAlign = TextAlign.Center,
                                                text = LocalStrings.current.messageEmptyList,
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = MaterialTheme.colorScheme.onBackground,
                                            )
                                        }
                                    }
                                } else {
                                    items(
                                        items = uiState.stopWords,
                                        key = { it },
                                    ) { domain ->
                                        ManageBanItem(
                                            title = domain,
                                            options =
                                            buildList {
                                                this +=
                                                    Option(
                                                        OptionId.Unban,
                                                        LocalStrings.current.settingsManageBanActionUnban,
                                                    )
                                            },
                                            onSelectOption = { optionId ->
                                                when (optionId) {
                                                    OptionId.Unban -> {
                                                        model.reduce(
                                                            ManageBanMviModel.Intent.RemoveStopWord(
                                                                domain,
                                                            ),
                                                        )
                                                    }

                                                    else -> Unit
                                                }
                                            },
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (addDomainDialogOpen) {
            EditTextualInfoDialog(
                title = LocalStrings.current.buttonAdd,
                label = LocalStrings.current.settingsManageBanDomainPlaceholder,
                value = "",
                onClose = { newValue ->
                    addDomainDialogOpen = false
                    newValue?.also {
                        model.reduce(ManageBanMviModel.Intent.BlockDomain(it))
                    }
                },
            )
        }
        if (addStopWordDialogOpen) {
            EditTextualInfoDialog(
                title = LocalStrings.current.buttonAdd,
                label = LocalStrings.current.settingsManageBanStopWordPlaceholder,
                value = "",
                onClose = { newValue ->
                    addStopWordDialogOpen = false
                    newValue?.also {
                        model.reduce(ManageBanMviModel.Intent.AddStopWord(it))
                    }
                },
            )
        }
    }
}
