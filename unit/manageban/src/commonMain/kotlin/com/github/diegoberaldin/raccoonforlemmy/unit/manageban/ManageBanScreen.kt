package com.github.diegoberaldin.raccoonforlemmy.unit.manageban

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.FloatingActionButtonMenu
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.FloatingActionButtonMenuItem
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.SearchField
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.SectionSelector
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.lemmyui.CommunityItem
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.lemmyui.CommunityItemPlaceholder
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.lemmyui.Option
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.lemmyui.OptionId
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.lemmyui.UserItem
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.lemmyui.di.getFabNestedScrollConnection
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.modals.EditTextualInfoDialog
import com.github.diegoberaldin.raccoonforlemmy.core.l10n.messages.LocalStrings
import com.github.diegoberaldin.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.github.diegoberaldin.raccoonforlemmy.core.persistence.di.getSettingsRepository
import com.github.diegoberaldin.raccoonforlemmy.core.utils.compose.onClick
import com.github.diegoberaldin.raccoonforlemmy.core.utils.compose.rememberCallback
import com.github.diegoberaldin.raccoonforlemmy.core.utils.compose.rememberCallbackArgs
import com.github.diegoberaldin.raccoonforlemmy.unit.manageban.components.ManageBanItem
import com.github.diegoberaldin.raccoonforlemmy.unit.manageban.components.ManageBanItemPlaceholder
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ManageBanScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val model = getScreenModel<ManageBanMviModel>()
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
                    override fun onPreScroll(
                        available: Offset,
                        source: NestedScrollSource,
                    ): Offset {
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
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            topBar = {
                TopAppBar(
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
                            Image(
                                modifier =
                                    Modifier.onClick(
                                        onClick = {
                                            navigationCoordinator.popScreen()
                                        },
                                    ),
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                            )
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
                                        onSelected =
                                            rememberCallback {
                                                scope.launch {
                                                    runCatching {
                                                        lazyListState.scrollToItem(0)
                                                        topAppBarState.heightOffset = 0f
                                                        topAppBarState.contentOffset = 0f
                                                    }
                                                }
                                            },
                                    )
                                if (uiState.section == ManageBanSection.Domains) {
                                    this +=
                                        FloatingActionButtonMenuItem(
                                            icon = Icons.Default.AddCircle,
                                            text = LocalStrings.current.buttonAdd,
                                            onSelected =
                                                rememberCallback {
                                                    addDomainDialogOpen = true
                                                },
                                        )
                                }
                                if (uiState.section == ManageBanSection.StopWords) {
                                    this +=
                                        FloatingActionButtonMenuItem(
                                            icon = Icons.Default.AddCircle,
                                            text = LocalStrings.current.buttonAdd,
                                            onSelected =
                                                rememberCallback {
                                                    addStopWordDialogOpen = true
                                                },
                                        )
                                }
                            },
                    )
                }
            },
        ) { padding ->
            val pullRefreshState =
                rememberPullRefreshState(
                    refreshing = uiState.refreshing,
                    onRefresh =
                        rememberCallback(model) {
                            model.reduce(ManageBanMviModel.Intent.Refresh)
                        },
                )
            Column(
                modifier =
                    Modifier
                        .padding(
                            top = padding.calculateTopPadding(),
                        ).navigationBarsPadding()
                        .then(
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

                Box(
                    modifier =
                        Modifier
                            .then(
                                if (settings.hideNavigationBarWhileScrolling) {
                                    Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
                                } else {
                                    Modifier
                                },
                            ).nestedScroll(keyboardScrollConnection)
                            .pullRefresh(pullRefreshState),
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
                                            onOptionSelected =
                                                rememberCallbackArgs(user) { optionId ->
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
                                            onOptionSelected =
                                                rememberCallbackArgs(community) { optionId ->
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
                                            onOptionSelected =
                                                rememberCallbackArgs(instance) { optionId ->
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
                                            onOptionSelected =
                                                rememberCallbackArgs(domain) { optionId ->
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
                                            onOptionSelected =
                                                rememberCallbackArgs(domain) { optionId ->
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

                    PullRefreshIndicator(
                        refreshing = uiState.refreshing,
                        state = pullRefreshState,
                        modifier = Modifier.align(Alignment.TopCenter),
                        backgroundColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }
        }

        if (addDomainDialogOpen) {
            EditTextualInfoDialog(
                title = LocalStrings.current.buttonAdd,
                label = LocalStrings.current.settingsManageBanDomainPlaceholder,
                value = "",
                onClose =
                    rememberCallbackArgs(model) { newValue ->
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
                onClose =
                    rememberCallbackArgs(model) { newValue ->
                        addStopWordDialogOpen = false
                        newValue?.also {
                            model.reduce(ManageBanMviModel.Intent.AddStopWord(it))
                        }
                    },
            )
        }
    }
}
