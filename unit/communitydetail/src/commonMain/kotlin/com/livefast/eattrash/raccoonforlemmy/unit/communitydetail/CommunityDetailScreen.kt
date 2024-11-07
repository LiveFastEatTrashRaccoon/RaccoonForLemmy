package com.livefast.eattrash.raccoonforlemmy.unit.communitydetail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.SyncDisabled
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Pending
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.PostLayout
import com.livefast.eattrash.raccoonforlemmy.core.appearance.di.getThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Dimensions
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.CustomDropDown
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.FloatingActionButtonMenu
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.FloatingActionButtonMenuItem
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.ProgressHud
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.SearchField
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.SwipeAction
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.SwipeActionCard
import com.livefast.eattrash.raccoonforlemmy.core.commonui.detailopener.api.getDetailOpener
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.CommunityHeader
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.IndicatorCallout
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.Option
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.OptionId
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.PostCard
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.PostCardPlaceholder
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.di.getFabNestedScrollConnection
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.CustomModalBottomSheet
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.CustomModalBottomSheetItem
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.SelectLanguageDialog
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.SortBottomSheet
import com.livefast.eattrash.raccoonforlemmy.core.l10n.messages.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.navigation.getScreenModel
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.notifications.di.getNotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.ActionOnSwipe
import com.livefast.eattrash.raccoonforlemmy.core.persistence.di.getSettingsRepository
import com.livefast.eattrash.raccoonforlemmy.core.utils.VoteAction
import com.livefast.eattrash.raccoonforlemmy.core.utils.keepscreenon.rememberKeepScreenOn
import com.livefast.eattrash.raccoonforlemmy.core.utils.toIcon
import com.livefast.eattrash.raccoonforlemmy.core.utils.toLocalDp
import com.livefast.eattrash.raccoonforlemmy.core.utils.toLocalPixel
import com.livefast.eattrash.raccoonforlemmy.core.utils.toModifier
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommentModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.containsId
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.readableHandle
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.readableName
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toIcon
import com.livefast.eattrash.raccoonforlemmy.unit.ban.BanUserScreen
import com.livefast.eattrash.raccoonforlemmy.unit.communityinfo.CommunityInfoScreen
import com.livefast.eattrash.raccoonforlemmy.unit.editcommunity.EditCommunityScreen
import com.livefast.eattrash.raccoonforlemmy.unit.explore.ExploreScreen
import com.livefast.eattrash.raccoonforlemmy.unit.instanceinfo.InstanceInfoScreen
import com.livefast.eattrash.raccoonforlemmy.unit.moderatewithreason.ModerateWithReasonAction
import com.livefast.eattrash.raccoonforlemmy.unit.moderatewithreason.ModerateWithReasonScreen
import com.livefast.eattrash.raccoonforlemmy.unit.moderatewithreason.toInt
import com.livefast.eattrash.raccoonforlemmy.unit.modlog.ModlogScreen
import com.livefast.eattrash.raccoonforlemmy.unit.rawcontent.RawContentDialog
import com.livefast.eattrash.raccoonforlemmy.unit.reportlist.ReportListScreen
import com.livefast.eattrash.raccoonforlemmy.unit.zoomableimage.ZoomableImageScreen
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import kotlin.math.roundToInt

class CommunityDetailScreen(
    private val communityId: Long,
    private val otherInstance: String = "",
) : Screen {
    override val key: ScreenKey
        get() = super.key + communityId.toString()

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val model =
            getScreenModel<CommunityDetailMviModel>(
                tag = communityId.toString(),
                parameters = { parametersOf(communityId, otherInstance) },
            )
        val uiState by model.uiState.collectAsState()
        val lazyListState = rememberLazyListState()
        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }
        val genericError = LocalStrings.current.messageGenericError
        val successMessage = LocalStrings.current.messageOperationSuccessful
        val isOnOtherInstance = remember { otherInstance.isNotEmpty() }
        val otherInstanceName = remember { otherInstance }
        val topAppBarState = rememberTopAppBarState()
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)
        val fabNestedScrollConnection = remember { getFabNestedScrollConnection() }
        val isFabVisible by fabNestedScrollConnection.isFabVisible.collectAsState()
        val navigationCoordinator = remember { getNavigationCoordinator() }
        val themeRepository = remember { getThemeRepository() }
        val upVoteColor by themeRepository.upVoteColor.collectAsState()
        val downVoteColor by themeRepository.downVoteColor.collectAsState()
        val replyColor by themeRepository.replyColor.collectAsState()
        val saveColor by themeRepository.saveColor.collectAsState()
        val defaultUpvoteColor = MaterialTheme.colorScheme.primary
        val defaultReplyColor = MaterialTheme.colorScheme.secondary
        val defaultSaveColor = MaterialTheme.colorScheme.secondaryContainer
        val defaultDownVoteColor = MaterialTheme.colorScheme.tertiary
        var rawContent by remember { mutableStateOf<Any?>(null) }
        val settingsRepository = remember { getSettingsRepository() }
        val settings by settingsRepository.currentSettings.collectAsState()
        val keepScreenOn = rememberKeepScreenOn()
        val detailOpener = remember { getDetailOpener() }
        val notificationCenter = remember { getNotificationCenter() }
        val clipboardManager = LocalClipboardManager.current
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
        var itemIdToDelete by remember { mutableStateOf<Long?>(null) }
        val statusBarInset =
            with(LocalDensity.current) {
                WindowInsets.statusBars.getTop(this)
            }
        var selectLanguageDialogOpen by remember { mutableStateOf(false) }
        var unsubscribeConfirmDialogOpen by remember { mutableStateOf(false) }
        var deleteConfirmDialogOpen by remember { mutableStateOf(false) }
        var shareBottomSheetUrls by remember { mutableStateOf<List<String>?>(null) }
        var sortBottomSheetOpened by remember { mutableStateOf(false) }
        var defaultSortBottomSheetOpened by remember { mutableStateOf(false) }
        var copyPostBottomSheet by remember { mutableStateOf<PostModel?>(null) }

        LaunchedEffect(model) {
            model.effects
                .onEach { effect ->
                    when (effect) {
                        is CommunityDetailMviModel.Effect.Error -> {
                            snackbarHostState.showSnackbar(effect.message ?: genericError)
                        }

                        CommunityDetailMviModel.Effect.Success -> {
                            snackbarHostState.showSnackbar(successMessage)
                        }

                        CommunityDetailMviModel.Effect.BackToTop -> {
                            runCatching {
                                lazyListState.scrollToItem(0)
                                topAppBarState.heightOffset = 0f
                                topAppBarState.contentOffset = 0f
                            }
                        }

                        is CommunityDetailMviModel.Effect.ZombieModeTick -> {
                            runCatching {
                                if (effect.index >= 0) {
                                    lazyListState.animateScrollBy(
                                        value = settings.zombieModeScrollAmount,
                                        animationSpec = tween(350),
                                    )
                                }
                            }
                        }

                        is CommunityDetailMviModel.Effect.Failure -> {
                            snackbarHostState.showSnackbar(effect.message ?: genericError)
                        }

                        CommunityDetailMviModel.Effect.Back -> navigationCoordinator.popScreen()
                    }
                }.launchIn(this)
        }
        LaunchedEffect(uiState.zombieModeActive) {
            if (uiState.zombieModeActive) {
                keepScreenOn.activate()
            } else {
                keepScreenOn.deactivate()
            }
        }
        LaunchedEffect(navigationCoordinator) {
            navigationCoordinator.globalMessage
                .onEach { message ->
                    snackbarHostState.showSnackbar(
                        message = message,
                    )
                }.launchIn(this)
        }

        Scaffold(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            topBar = {
                val maxTopInset = Dimensions.maxTopBarInset.toLocalPixel()
                var topInset by remember { mutableStateOf(maxTopInset) }
                snapshotFlow { topAppBarState.collapsedFraction }
                    .onEach {
                        topInset =
                            (maxTopInset * (1 - it)).let { insetValue ->
                                if (uiState.searching) {
                                    insetValue.coerceAtLeast(statusBarInset.toFloat())
                                } else {
                                    insetValue
                                }
                            }
                    }.launchIn(scope)

                TopAppBar(
                    windowInsets =
                        if (settings.edgeToEdge) {
                            WindowInsets(0, topInset.roundToInt(), 0, 0)
                        } else {
                            TopAppBarDefaults.windowInsets
                        },
                    scrollBehavior = scrollBehavior,
                    title = {
                        Text(
                            modifier = Modifier.padding(horizontal = Spacing.s),
                            text = uiState.community.readableName(uiState.preferNicknames),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    },
                    actions = {
                        // subscribe button
                        if (!isOnOtherInstance && uiState.isLogged) {
                            IconButton(
                                onClick = {
                                    when (uiState.community.subscribed) {
                                        false -> model.reduce(CommunityDetailMviModel.Intent.Subscribe)
                                        else -> {
                                            unsubscribeConfirmDialogOpen = true
                                        }
                                    }
                                },
                            ) {
                                Icon(
                                    imageVector =
                                        when (uiState.community.subscribed) {
                                            true -> Icons.Outlined.CheckCircle
                                            false -> Icons.Outlined.AddCircleOutline
                                            else -> Icons.Outlined.Pending
                                        },
                                    contentDescription = null,
                                )
                            }
                        }

                        // sort button
                        IconButton(
                            onClick = {
                                sortBottomSheetOpened = true
                            },
                        ) {
                            Icon(
                                imageVector = uiState.sortType.toIcon(),
                                contentDescription = null,
                            )
                        }

                        // options menu
                        Box {
                            val options =
                                buildList {
                                    if (!isOnOtherInstance) {
                                        this +=
                                            Option(
                                                OptionId.Search,
                                                if (uiState.searching) {
                                                    LocalStrings.current.actionExitSearch
                                                } else {
                                                    buildString {
                                                        append(LocalStrings.current.actionSearchInCommunity)
                                                    }
                                                },
                                            )
                                    }
                                    this +=
                                        Option(
                                            OptionId.SetCustomSort,
                                            LocalStrings.current.communitySetCustomSort,
                                        )
                                    if (uiState.isLogged) {
                                        this +=
                                            Option(
                                                OptionId.SetPreferredLanguage,
                                                LocalStrings.current.communitySetPreferredLanguage,
                                            )
                                    }
                                    this +=
                                        Option(
                                            OptionId.InfoInstance,
                                            LocalStrings.current.communityDetailInstanceInfo,
                                        )
                                    this +=
                                        Option(
                                            OptionId.ExploreInstance,
                                            buildString {
                                                append(LocalStrings.current.navigationSearch)
                                                append(" ")
                                                append(uiState.community.host)
                                                append(" (")
                                                append(LocalStrings.current.beta)
                                                append(")")
                                            },
                                        )
                                    this +=
                                        Option(
                                            OptionId.Share,
                                            LocalStrings.current.postActionShare,
                                        )
                                    if (uiState.isLogged) {
                                        this +=
                                            Option(
                                                OptionId.Block,
                                                LocalStrings.current.blockActionCommunity,
                                            )
                                        this +=
                                            Option(
                                                OptionId.BlockInstance,
                                                LocalStrings.current.communityDetailBlockInstance,
                                            )
                                    }
                                    if (uiState.currentUserId != null && otherInstanceName.isEmpty()) {
                                        this +=
                                            Option(
                                                OptionId.Favorite,
                                                if (uiState.community.favorite) {
                                                    LocalStrings.current.communityActionRemoveFavorite
                                                } else {
                                                    LocalStrings.current.communityActionAddFavorite
                                                },
                                            )
                                    }

                                    this +=
                                        Option(
                                            OptionId.ViewModlog,
                                            LocalStrings.current.communityActionViewModlog,
                                        )

                                    if (uiState.moderators.containsId(uiState.currentUserId)) {
                                        this +=
                                            Option(
                                                OptionId.OpenReports,
                                                LocalStrings.current.modActionOpenReports,
                                            )
                                        this +=
                                            Option(
                                                OptionId.Edit,
                                                LocalStrings.current.communityActionEdit,
                                            )
                                        this +=
                                            Option(
                                                OptionId.Delete,
                                                LocalStrings.current.commentActionDelete,
                                            )
                                    }
                                    if (uiState.isAdmin) {
                                        if (uiState.community.hidden) {
                                            this +=
                                                Option(
                                                    OptionId.Hide,
                                                    LocalStrings.current.postActionUnhide,
                                                )
                                        } else {
                                            this +=
                                                Option(
                                                    OptionId.Hide,
                                                    LocalStrings.current.postActionHide,
                                                )
                                        }
                                        this +=
                                            Option(
                                                OptionId.Purge,
                                                LocalStrings.current.adminActionPurge,
                                            )
                                    }
                                }
                            var optionsExpanded by remember { mutableStateOf(false) }
                            var optionsOffset by remember { mutableStateOf(Offset.Zero) }
                            IconButton(
                                modifier =
                                    Modifier
                                        .onGloballyPositioned {
                                            optionsOffset = it.positionInParent()
                                        },
                                onClick = {
                                    optionsExpanded = true
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = null,
                                )
                            }

                            CustomDropDown(
                                expanded = optionsExpanded,
                                onDismiss = {
                                    optionsExpanded = false
                                },
                                offset =
                                    DpOffset(
                                        x = optionsOffset.x.toLocalDp(),
                                        y = optionsOffset.y.toLocalDp(),
                                    ),
                            ) {
                                options.forEach { option ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(option.text)
                                        },
                                        onClick = {
                                            optionsExpanded = false
                                            when (option.id) {
                                                OptionId.BlockInstance ->
                                                    model.reduce(
                                                        CommunityDetailMviModel.Intent.BlockInstance,
                                                    )

                                                OptionId.Block ->
                                                    model.reduce(
                                                        CommunityDetailMviModel.Intent.Block,
                                                    )

                                                OptionId.InfoInstance -> {
                                                    navigationCoordinator.pushScreen(
                                                        InstanceInfoScreen(
                                                            url = uiState.community.instanceUrl,
                                                        ),
                                                    )
                                                }

                                                OptionId.OpenReports -> {
                                                    val screen =
                                                        ReportListScreen(
                                                            communityId = uiState.community.id,
                                                        )
                                                    navigationCoordinator.pushScreen(screen)
                                                }

                                                OptionId.Favorite -> {
                                                    model.reduce(
                                                        CommunityDetailMviModel.Intent.ToggleFavorite,
                                                    )
                                                }

                                                OptionId.ViewModlog -> {
                                                    val screen =
                                                        ModlogScreen(
                                                            communityId = uiState.community.id,
                                                        )
                                                    navigationCoordinator.pushScreen(screen)
                                                }

                                                OptionId.Share -> {
                                                    val urls =
                                                        buildList {
                                                            if (uiState.community.host != uiState.instance) {
                                                                this += "https://${uiState.instance}/c/${uiState.community.readableHandle}"
                                                            }
                                                            this += "https://${uiState.community.host}/c/${uiState.community.name}"
                                                        }
                                                    if (urls.size == 1) {
                                                        model.reduce(
                                                            CommunityDetailMviModel.Intent.Share(
                                                                urls.first(),
                                                            ),
                                                        )
                                                    } else {
                                                        shareBottomSheetUrls = urls
                                                    }
                                                }

                                                OptionId.SetCustomSort -> {
                                                    defaultSortBottomSheetOpened = true
                                                }

                                                OptionId.Search -> {
                                                    model.reduce(
                                                        CommunityDetailMviModel.Intent.ChangeSearching(
                                                            !uiState.searching,
                                                        ),
                                                    )
                                                }

                                                OptionId.ExploreInstance -> {
                                                    val screen =
                                                        ExploreScreen(otherInstance = uiState.community.host)
                                                    navigationCoordinator.pushScreen(screen)
                                                }

                                                OptionId.Edit -> {
                                                    val screen =
                                                        EditCommunityScreen(uiState.community.id)
                                                    navigationCoordinator.pushScreen(screen)
                                                }

                                                OptionId.Delete -> {
                                                    deleteConfirmDialogOpen = true
                                                }

                                                OptionId.Hide -> {
                                                    if (uiState.community.hidden) {
                                                        model.reduce(CommunityDetailMviModel.Intent.UnhideCommunity)
                                                    } else {
                                                        val screen =
                                                            ModerateWithReasonScreen(
                                                                actionId = ModerateWithReasonAction.HideCommunity.toInt(),
                                                                contentId = uiState.community.id,
                                                            )
                                                        navigationCoordinator.pushScreen(screen)
                                                    }
                                                }

                                                OptionId.Purge -> {
                                                    val screen =
                                                        ModerateWithReasonScreen(
                                                            actionId = ModerateWithReasonAction.PurgeCommunity.toInt(),
                                                            contentId = uiState.community.id,
                                                        )
                                                    navigationCoordinator.pushScreen(screen)
                                                }

                                                OptionId.SetPreferredLanguage -> {
                                                    selectLanguageDialogOpen = true
                                                }

                                                else -> Unit
                                            }
                                        },
                                    )
                                }
                            }
                        }
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
                                    contentDescription = null,
                                )
                            }
                        }
                    },
                )
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
                                if (uiState.zombieModeActive) {
                                    this +=
                                        FloatingActionButtonMenuItem(
                                            icon = Icons.Default.SyncDisabled,
                                            text = LocalStrings.current.actionDeactivateZombieMode,
                                            onSelected = {
                                                model.reduce(CommunityDetailMviModel.Intent.PauseZombieMode)
                                            },
                                        )
                                } else {
                                    this +=
                                        FloatingActionButtonMenuItem(
                                            icon = Icons.Default.Sync,
                                            text = LocalStrings.current.actionActivateZombieMode,
                                            onSelected = {
                                                model.reduce(
                                                    CommunityDetailMviModel.Intent.StartZombieMode(-1),
                                                )
                                            },
                                        )
                                }
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
                                if (uiState.isLogged && !isOnOtherInstance) {
                                    this +=
                                        FloatingActionButtonMenuItem(
                                            icon = Icons.Default.ClearAll,
                                            text = LocalStrings.current.actionClearRead,
                                            onSelected = {
                                                model.reduce(CommunityDetailMviModel.Intent.ClearRead)
                                                scope.launch {
                                                    runCatching {
                                                        lazyListState.scrollToItem(0)
                                                        topAppBarState.heightOffset = 0f
                                                        topAppBarState.contentOffset = 0f
                                                    }
                                                }
                                            },
                                        )
                                    this +=
                                        FloatingActionButtonMenuItem(
                                            icon = Icons.Default.Create,
                                            text = LocalStrings.current.actionCreatePost,
                                            onSelected = {
                                                detailOpener.openCreatePost(
                                                    communityId = uiState.community.id,
                                                )
                                            },
                                        )
                                }
                            },
                    )
                }
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
        ) { padding ->
            if (uiState.currentUserId != null) {
                Column(
                    modifier =
                        Modifier.padding(
                            top = padding.calculateTopPadding(),
                        ),
                ) {
                    if (uiState.searching) {
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
                                model.reduce(CommunityDetailMviModel.Intent.SetSearch(value))
                            },
                            onClear = {
                                model.reduce(CommunityDetailMviModel.Intent.SetSearch(""))
                            },
                        )
                    }

                    PullToRefreshBox(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .then(
                                    if (settings.hideNavigationBarWhileScrolling) {
                                        Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
                                    } else {
                                        Modifier
                                    },
                                ).nestedScroll(fabNestedScrollConnection)
                                .nestedScroll(keyboardScrollConnection),
                        isRefreshing = uiState.refreshing,
                        onRefresh = {
                            model.reduce(CommunityDetailMviModel.Intent.Refresh)
                        },
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            state = lazyListState,
                            userScrollEnabled = !uiState.zombieModeActive,
                        ) {
                            item {
                                if (!uiState.searching) {
                                    CommunityHeader(
                                        modifier = Modifier.padding(bottom = Spacing.s),
                                        community = uiState.community,
                                        autoLoadImages = uiState.autoLoadImages,
                                        onOpenImage = { url ->
                                            navigationCoordinator.pushScreen(
                                                ZoomableImageScreen(
                                                    url = url,
                                                    source = uiState.community.readableHandle,
                                                ),
                                            )
                                        },
                                        onInfo = {
                                            val screen =
                                                CommunityInfoScreen(
                                                    communityId = uiState.community.id,
                                                    communityName = uiState.community.name,
                                                    otherInstance = otherInstanceName,
                                                )
                                            navigationCoordinator.openSideMenu(screen)
                                        },
                                    )
                                }
                            }
                            item {
                                if (!uiState.searching && uiState.notices.isNotEmpty()) {
                                    Column(
                                        modifier =
                                            Modifier.padding(
                                                start = Spacing.s,
                                                end = Spacing.s,
                                                bottom = Spacing.s,
                                            ),
                                        verticalArrangement = Arrangement.spacedBy(Spacing.xxs),
                                    ) {
                                        for (notice in uiState.notices) {
                                            IndicatorCallout(
                                                modifier = Modifier.fillMaxWidth(),
                                                text =
                                                    when (notice) {
                                                        CommunityNotices.LocalOnlyVisibility ->
                                                            LocalStrings.current.noticeCommunityLocalOnly

                                                        CommunityNotices.BannedUser -> LocalStrings.current.noticeBannedUser
                                                    },
                                            )
                                        }
                                    }
                                }
                            }
                            if (uiState.posts.isEmpty() && uiState.initial) {
                                items(5) {
                                    PostCardPlaceholder(
                                        modifier = Modifier.padding(horizontal = Spacing.xs),
                                        postLayout = uiState.postLayout,
                                    )
                                    if (uiState.postLayout != PostLayout.Card) {
                                        HorizontalDivider(modifier = Modifier.padding(vertical = Spacing.interItem))
                                    } else {
                                        Spacer(modifier = Modifier.height(Spacing.interItem))
                                    }
                                }
                            }
                            items(
                                items = uiState.posts,
                                key = { it.id.toString() + (it.updateDate ?: it.publishDate) },
                            ) { post ->
                                LaunchedEffect(post.id) {
                                    if (settings.markAsReadWhileScrolling && !post.read) {
                                        model.reduce(CommunityDetailMviModel.Intent.MarkAsRead(post.id))
                                    }
                                }

                                @Composable
                                fun List<ActionOnSwipe>.toSwipeActions(canEdit: Boolean): List<SwipeAction> =
                                    mapNotNull {
                                        when (it) {
                                            ActionOnSwipe.UpVote ->
                                                SwipeAction(
                                                    swipeContent = {
                                                        Icon(
                                                            modifier = VoteAction.UpVote.toModifier(),
                                                            imageVector = VoteAction.UpVote.toIcon(),
                                                            contentDescription = null,
                                                            tint = Color.White,
                                                        )
                                                    },
                                                    backgroundColor =
                                                        upVoteColor
                                                            ?: defaultUpvoteColor,
                                                    onTriggered = {
                                                        model.reduce(
                                                            CommunityDetailMviModel.Intent.UpVotePost(
                                                                post.id,
                                                            ),
                                                        )
                                                    },
                                                )

                                            ActionOnSwipe.DownVote ->
                                                SwipeAction(
                                                    swipeContent = {
                                                        Icon(
                                                            modifier = VoteAction.DownVote.toModifier(),
                                                            imageVector = VoteAction.DownVote.toIcon(),
                                                            contentDescription = null,
                                                            tint = Color.White,
                                                        )
                                                    },
                                                    backgroundColor =
                                                        downVoteColor
                                                            ?: defaultDownVoteColor,
                                                    onTriggered = {
                                                        model.reduce(
                                                            CommunityDetailMviModel.Intent.DownVotePost(
                                                                post.id,
                                                            ),
                                                        )
                                                    },
                                                ).takeIf { uiState.downVoteEnabled }

                                            ActionOnSwipe.Reply ->
                                                SwipeAction(
                                                    swipeContent = {
                                                        Icon(
                                                            imageVector = Icons.AutoMirrored.Default.Reply,
                                                            contentDescription = null,
                                                            tint = Color.White,
                                                        )
                                                    },
                                                    backgroundColor =
                                                        replyColor
                                                            ?: defaultReplyColor,
                                                    onTriggered = {
                                                        detailOpener.openReply(originalPost = post)
                                                    },
                                                )

                                            ActionOnSwipe.Save ->
                                                SwipeAction(
                                                    swipeContent = {
                                                        Icon(
                                                            imageVector = Icons.Default.Bookmark,
                                                            contentDescription = null,
                                                            tint = Color.White,
                                                        )
                                                    },
                                                    backgroundColor = saveColor ?: defaultSaveColor,
                                                    onTriggered = {
                                                        model.reduce(
                                                            CommunityDetailMviModel.Intent.SavePost(
                                                                id = post.id,
                                                            ),
                                                        )
                                                    },
                                                )

                                            ActionOnSwipe.Edit ->
                                                SwipeAction(
                                                    swipeContent = {
                                                        Icon(
                                                            imageVector = Icons.Default.Edit,
                                                            contentDescription = null,
                                                            tint = Color.White,
                                                        )
                                                    },
                                                    backgroundColor = MaterialTheme.colorScheme.tertiary,
                                                    onTriggered = {
                                                        detailOpener.openCreatePost(editedPost = post)
                                                    },
                                                ).takeIf { canEdit }

                                            else -> null
                                        }
                                    }

                                SwipeActionCard(
                                    modifier = Modifier.fillMaxWidth(),
                                    enabled = uiState.swipeActionsEnabled,
                                    onGestureBegin = {
                                        model.reduce(CommunityDetailMviModel.Intent.HapticIndication)
                                    },
                                    swipeToStartActions =
                                        uiState.actionsOnSwipeToStartPosts.toSwipeActions(
                                            canEdit = post.creator?.id == uiState.currentUserId,
                                        ),
                                    swipeToEndActions =
                                        uiState.actionsOnSwipeToEndPosts.toSwipeActions(
                                            canEdit = post.creator?.id == uiState.currentUserId,
                                        ),
                                    content = {
                                        PostCard(
                                            modifier = Modifier.padding(horizontal = Spacing.xs),
                                            post = post,
                                            isFromModerator = uiState.moderators.containsId(post.creator?.id),
                                            postLayout = uiState.postLayout,
                                            limitBodyHeight = true,
                                            fullHeightImage = uiState.fullHeightImages,
                                            fullWidthImage = uiState.fullWidthImages,
                                            voteFormat = uiState.voteFormat,
                                            autoLoadImages = uiState.autoLoadImages,
                                            preferNicknames = uiState.preferNicknames,
                                            showScores = uiState.showScores,
                                            actionButtonsActive = uiState.isLogged,
                                            downVoteEnabled = uiState.downVoteEnabled,
                                            highlightText = uiState.searchText,
                                            blurNsfw =
                                                when {
                                                    uiState.community.nsfw -> false
                                                    else -> uiState.blurNsfw
                                                },
                                            fadeRead = uiState.fadeReadPosts,
                                            showUnreadComments = uiState.showUnreadComments,
                                            onClick = {
                                                model.reduce(
                                                    CommunityDetailMviModel.Intent.MarkAsRead(
                                                        post.id,
                                                    ),
                                                )
                                                model.reduce(CommunityDetailMviModel.Intent.WillOpenDetail)
                                                detailOpener.openPostDetail(
                                                    post = post,
                                                    otherInstance = otherInstanceName,
                                                    isMod = uiState.moderators.containsId(uiState.currentUserId),
                                                )
                                            },
                                            onDoubleClick =
                                                {
                                                    model.reduce(
                                                        CommunityDetailMviModel.Intent.UpVotePost(
                                                            id = post.id,
                                                            feedback = true,
                                                        ),
                                                    )
                                                }.takeIf { uiState.doubleTapActionEnabled && uiState.isLogged && !isOnOtherInstance },
                                            onOpenCreator = { user, instance ->
                                                detailOpener.openUserDetail(user, instance)
                                            },
                                            onUpVote =
                                                {
                                                    model.reduce(
                                                        CommunityDetailMviModel.Intent.UpVotePost(
                                                            id = post.id,
                                                        ),
                                                    )
                                                }.takeIf { uiState.isLogged && !isOnOtherInstance },
                                            onDownVote =
                                                {
                                                    model.reduce(
                                                        CommunityDetailMviModel.Intent.DownVotePost(
                                                            id = post.id,
                                                        ),
                                                    )
                                                }.takeIf { uiState.isLogged && !isOnOtherInstance },
                                            onSave =
                                                {
                                                    model.reduce(
                                                        CommunityDetailMviModel.Intent.SavePost(
                                                            id = post.id,
                                                        ),
                                                    )
                                                }.takeIf { uiState.isLogged && !isOnOtherInstance },
                                            onReply =
                                                {
                                                    model.reduce(
                                                        CommunityDetailMviModel.Intent.MarkAsRead(
                                                            post.id,
                                                        ),
                                                    )
                                                    model.reduce(CommunityDetailMviModel.Intent.WillOpenDetail)
                                                    detailOpener.openPostDetail(post)
                                                }.takeIf { uiState.isLogged && !isOnOtherInstance },
                                            onOpenImage = { url ->
                                                model.reduce(
                                                    CommunityDetailMviModel.Intent.MarkAsRead(
                                                        post.id,
                                                    ),
                                                )
                                                navigationCoordinator.pushScreen(
                                                    ZoomableImageScreen(
                                                        url = url,
                                                        source = uiState.community.readableHandle,
                                                    ),
                                                )
                                            },
                                            options =
                                                buildList {
                                                    this +=
                                                        Option(
                                                            OptionId.Share,
                                                            LocalStrings.current.postActionShare,
                                                        )
                                                    this +=
                                                        Option(
                                                            OptionId.Copy,
                                                            LocalStrings.current.actionCopyClipboard,
                                                        )
                                                    if (uiState.isLogged && !isOnOtherInstance) {
                                                        this +=
                                                            Option(
                                                                OptionId.Hide,
                                                                LocalStrings.current.postActionHide,
                                                            )
                                                    }
                                                    this +=
                                                        Option(
                                                            OptionId.SeeRaw,
                                                            LocalStrings.current.postActionSeeRaw,
                                                        )
                                                    if (uiState.isLogged && !isOnOtherInstance) {
                                                        this +=
                                                            Option(
                                                                OptionId.CrossPost,
                                                                LocalStrings.current.postActionCrossPost,
                                                            )
                                                        this +=
                                                            Option(
                                                                OptionId.Report,
                                                                LocalStrings.current.postActionReport,
                                                            )
                                                    }
                                                    if (post.creator?.id == uiState.currentUserId && !isOnOtherInstance) {
                                                        this +=
                                                            Option(
                                                                OptionId.Edit,
                                                                LocalStrings.current.postActionEdit,
                                                            )
                                                        if (post.deleted) {
                                                            this +=
                                                                Option(
                                                                    OptionId.Restore,
                                                                    LocalStrings.current.actionRestore,
                                                                )
                                                        } else {
                                                            this +=
                                                                Option(
                                                                    OptionId.Delete,
                                                                    LocalStrings.current.commentActionDelete,
                                                                )
                                                        }
                                                    }
                                                    if (uiState.moderators.containsId(uiState.currentUserId)) {
                                                        this +=
                                                            Option(
                                                                OptionId.FeaturePost,
                                                                if (post.featuredCommunity) {
                                                                    LocalStrings.current.modActionUnmarkAsFeatured
                                                                } else {
                                                                    LocalStrings.current.modActionMarkAsFeatured
                                                                },
                                                            )
                                                        this +=
                                                            Option(
                                                                OptionId.LockPost,
                                                                if (post.locked) {
                                                                    LocalStrings.current.modActionUnlock
                                                                } else {
                                                                    LocalStrings.current.modActionLock
                                                                },
                                                            )
                                                        this +=
                                                            Option(
                                                                OptionId.Remove,
                                                                LocalStrings.current.modActionRemove,
                                                            )
                                                        this +=
                                                            Option(
                                                                OptionId.BanUser,
                                                                if (post.creator?.banned == true) {
                                                                    LocalStrings.current.modActionAllow
                                                                } else {
                                                                    LocalStrings.current.modActionBan
                                                                },
                                                            )
                                                        post.creator?.id?.also { creatorId ->
                                                            if (uiState.currentUserId != creatorId) {
                                                                this +=
                                                                    Option(
                                                                        OptionId.AddMod,
                                                                        if (uiState.moderators.containsId(
                                                                                creatorId,
                                                                            )
                                                                        ) {
                                                                            LocalStrings.current.modActionRemoveMod
                                                                        } else {
                                                                            LocalStrings.current.modActionAddMod
                                                                        },
                                                                    )
                                                            }
                                                        }
                                                    }
                                                    if (uiState.isAdmin) {
                                                        this +=
                                                            Option(
                                                                OptionId.Purge,
                                                                LocalStrings.current.adminActionPurge,
                                                            )
                                                        post.creator?.also { creator ->
                                                            this +=
                                                                Option(
                                                                    OptionId.PurgeCreator,
                                                                    buildString {
                                                                        append(LocalStrings.current.adminActionPurge)
                                                                        append(" ")
                                                                        append(
                                                                            creator.readableName(
                                                                                uiState.preferNicknames,
                                                                            ),
                                                                        )
                                                                    },
                                                                )
                                                        }
                                                        this +=
                                                            Option(
                                                                OptionId.AdminFeaturePost,
                                                                if (post.featuredLocal) {
                                                                    LocalStrings.current.adminActionUnmarkAsFeatured
                                                                } else {
                                                                    LocalStrings.current.adminActionMarkAsFeatured
                                                                },
                                                            )
                                                    }
                                                },
                                            onOptionSelected = { optionId ->
                                                when (optionId) {
                                                    OptionId.Delete -> {
                                                        itemIdToDelete = post.id
                                                    }

                                                    OptionId.Edit -> {
                                                        detailOpener.openCreatePost(editedPost = post)
                                                    }

                                                    OptionId.Report -> {
                                                        val screen =
                                                            ModerateWithReasonScreen(
                                                                actionId = ModerateWithReasonAction.ReportPost.toInt(),
                                                                contentId = post.id,
                                                            )
                                                        navigationCoordinator.pushScreen(screen)
                                                    }

                                                    OptionId.CrossPost -> {
                                                        detailOpener.openCreatePost(
                                                            crossPost = post,
                                                            forceCommunitySelection = true,
                                                        )
                                                    }

                                                    OptionId.SeeRaw -> {
                                                        rawContent = post
                                                    }

                                                    OptionId.Hide ->
                                                        model.reduce(
                                                            CommunityDetailMviModel.Intent.Hide(post.id),
                                                        )

                                                    OptionId.Share -> {
                                                        val urls =
                                                            listOfNotNull(
                                                                post.originalUrl,
                                                                "https://${uiState.instance}/post/${post.id}",
                                                            ).distinct()
                                                        if (urls.size == 1) {
                                                            model.reduce(
                                                                CommunityDetailMviModel.Intent.Share(
                                                                    urls.first(),
                                                                ),
                                                            )
                                                        } else {
                                                            shareBottomSheetUrls = urls
                                                        }
                                                    }

                                                    OptionId.FeaturePost ->
                                                        model.reduce(
                                                            CommunityDetailMviModel.Intent.ModFeaturePost(
                                                                post.id,
                                                            ),
                                                        )

                                                    OptionId.AdminFeaturePost ->
                                                        model.reduce(
                                                            CommunityDetailMviModel.Intent.AdminFeaturePost(
                                                                post.id,
                                                            ),
                                                        )

                                                    OptionId.LockPost ->
                                                        model.reduce(
                                                            CommunityDetailMviModel.Intent.ModLockPost(
                                                                post.id,
                                                            ),
                                                        )

                                                    OptionId.Remove -> {
                                                        val screen =
                                                            ModerateWithReasonScreen(
                                                                actionId = ModerateWithReasonAction.RemovePost.toInt(),
                                                                contentId = post.id,
                                                            )
                                                        navigationCoordinator.pushScreen(screen)
                                                    }

                                                    OptionId.BanUser -> {
                                                        post.creator?.id?.also { userId ->
                                                            val screen =
                                                                BanUserScreen(
                                                                    userId = userId,
                                                                    communityId = uiState.community.id,
                                                                    newValue = post.creator?.banned != true,
                                                                    postId = post.id,
                                                                )
                                                            navigationCoordinator.pushScreen(screen)
                                                        }
                                                    }

                                                    OptionId.AddMod -> {
                                                        post.creator?.id?.also { userId ->
                                                            model.reduce(
                                                                CommunityDetailMviModel.Intent.ModToggleModUser(
                                                                    userId,
                                                                ),
                                                            )
                                                        }
                                                    }

                                                    OptionId.Copy -> {
                                                        val texts =
                                                            listOfNotNull(
                                                                post.title.takeIf { it.isNotBlank() },
                                                                post.text.takeIf { it.isNotBlank() },
                                                            ).distinct()
                                                        if (texts.size == 1) {
                                                            clipboardManager.setText(AnnotatedString(texts.first()))
                                                        } else {
                                                            copyPostBottomSheet = post
                                                        }
                                                    }

                                                    OptionId.Purge -> {
                                                        val screen =
                                                            ModerateWithReasonScreen(
                                                                actionId = ModerateWithReasonAction.PurgePost.toInt(),
                                                                contentId = post.id,
                                                            )
                                                        navigationCoordinator.pushScreen(screen)
                                                    }

                                                    OptionId.PurgeCreator -> {
                                                        post.creator?.id?.also { userId ->
                                                            val screen =
                                                                ModerateWithReasonScreen(
                                                                    actionId = ModerateWithReasonAction.PurgeUser.toInt(),
                                                                    contentId = userId,
                                                                )
                                                            navigationCoordinator.pushScreen(screen)
                                                        }
                                                    }

                                                    OptionId.Restore -> {
                                                        model.reduce(
                                                            CommunityDetailMviModel.Intent.RestorePost(
                                                                post.id,
                                                            ),
                                                        )
                                                    }

                                                    else -> Unit
                                                }
                                            },
                                        )
                                    },
                                )
                                if (uiState.postLayout != PostLayout.Card) {
                                    HorizontalDivider(modifier = Modifier.padding(vertical = Spacing.interItem))
                                } else {
                                    Spacer(modifier = Modifier.height(Spacing.interItem))
                                }
                            }
                            item {
                                if (!uiState.initial && !uiState.loading && !uiState.refreshing && uiState.canFetchMore) {
                                    if (settings.infiniteScrollEnabled) {
                                        model.reduce(CommunityDetailMviModel.Intent.LoadNextPage)
                                    } else {
                                        Row(
                                            modifier =
                                                Modifier
                                                    .fillMaxWidth()
                                                    .padding(top = Spacing.s),
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {
                                            Button(
                                                onClick = {
                                                    model.reduce(CommunityDetailMviModel.Intent.LoadNextPage)
                                                },
                                            ) {
                                                Text(
                                                    text = LocalStrings.current.postListLoadMorePosts,
                                                    style = MaterialTheme.typography.labelSmall,
                                                )
                                            }
                                        }
                                    }
                                }
                                if (uiState.loading && !uiState.refreshing) {
                                    Box(
                                        modifier = Modifier.fillMaxWidth().padding(Spacing.xs),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(25.dp),
                                            color = MaterialTheme.colorScheme.primary,
                                        )
                                    }
                                }
                            }
                            item {
                                Spacer(modifier = Modifier.height(Spacing.xxxl))
                            }
                        }

                        if (uiState.asyncInProgress) {
                            ProgressHud()
                        }
                    }
                }
            }
        }

        if (rawContent != null) {
            when (val content = rawContent) {
                is PostModel -> {
                    RawContentDialog(
                        title = content.title,
                        text = content.text,
                        publishDate = content.publishDate,
                        updateDate = content.updateDate,
                        url = content.url,
                        upVotes = content.upvotes,
                        downVotes = content.downvotes,
                        isLogged = uiState.isLogged,
                        onDismiss = {
                            rawContent = null
                        },
                        onQuote = { quotation ->
                            rawContent = null
                            if (quotation != null) {
                                detailOpener.openReply(
                                    originalPost = content,
                                    initialText =
                                        buildString {
                                            append("> ")
                                            append(quotation)
                                            append("\n\n")
                                        },
                                )
                            }
                        },
                    )
                }

                is CommentModel -> {
                    RawContentDialog(
                        text = content.text,
                        isLogged = uiState.isLogged,
                        publishDate = content.publishDate,
                        updateDate = content.updateDate,
                        upVotes = content.upvotes,
                        downVotes = content.downvotes,
                        onDismiss = {
                            rawContent = null
                        },
                        onQuote = { quotation ->
                            rawContent = null
                            if (quotation != null) {
                                detailOpener.openReply(
                                    originalComment = content,
                                    originalPost = PostModel(id = content.postId),
                                    initialText =
                                        buildString {
                                            append("> ")
                                            append(quotation)
                                            append("\n\n")
                                        },
                                )
                            }
                        },
                    )
                }
            }
        }

        itemIdToDelete?.also { itemId ->
            AlertDialog(
                onDismissRequest = {
                    itemIdToDelete = null
                },
                dismissButton = {
                    Button(
                        onClick = {
                            itemIdToDelete = null
                        },
                    ) {
                        Text(text = LocalStrings.current.buttonCancel)
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            model.reduce(CommunityDetailMviModel.Intent.DeletePost(itemId))
                            itemIdToDelete = null
                        },
                    ) {
                        Text(text = LocalStrings.current.buttonConfirm)
                    }
                },
                text = {
                    Text(text = LocalStrings.current.messageAreYouSure)
                },
            )
        }

        if (selectLanguageDialogOpen) {
            SelectLanguageDialog(
                languages = uiState.availableLanguages,
                currentLanguageId = uiState.currentPreferredLanguageId,
                onSelect = { langId ->
                    model.reduce(CommunityDetailMviModel.Intent.SelectPreferredLanguage(langId))
                    selectLanguageDialogOpen = false
                },
                onDismiss = {
                    selectLanguageDialogOpen = false
                },
            )
        }

        if (unsubscribeConfirmDialogOpen) {
            AlertDialog(
                onDismissRequest = {
                    unsubscribeConfirmDialogOpen = false
                },
                title = {
                    Text(
                        text = LocalStrings.current.communityActionUnsubscribe,
                        style = MaterialTheme.typography.titleMedium,
                    )
                },
                text = {
                    Text(text = LocalStrings.current.messageAreYouSure)
                },
                dismissButton = {
                    Button(
                        onClick = {
                            unsubscribeConfirmDialogOpen = false
                        },
                    ) {
                        Text(text = LocalStrings.current.buttonCancel)
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            unsubscribeConfirmDialogOpen = false
                            model.reduce(CommunityDetailMviModel.Intent.Unsubscribe)
                        },
                    ) {
                        Text(text = LocalStrings.current.buttonConfirm)
                    }
                },
            )
        }

        if (deleteConfirmDialogOpen) {
            AlertDialog(
                onDismissRequest = {
                    deleteConfirmDialogOpen = false
                },
                title = {
                    Text(
                        text = LocalStrings.current.commentActionDelete,
                        style = MaterialTheme.typography.titleMedium,
                    )
                },
                text = {
                    Text(text = LocalStrings.current.messageAreYouSure)
                },
                dismissButton = {
                    Button(
                        onClick = {
                            deleteConfirmDialogOpen = false
                        },
                    ) {
                        Text(text = LocalStrings.current.buttonCancel)
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            deleteConfirmDialogOpen = false
                            model.reduce(CommunityDetailMviModel.Intent.DeleteCommunity)
                        },
                    ) {
                        Text(text = LocalStrings.current.buttonConfirm)
                    }
                },
            )
        }

        shareBottomSheetUrls?.also { values ->
            CustomModalBottomSheet(
                title = LocalStrings.current.postActionShare,
                items =
                    values.map { value ->
                        CustomModalBottomSheetItem(label = value)
                    },
                onSelected = { index ->
                    shareBottomSheetUrls = null
                    if (index != null) {
                        notificationCenter.send(
                            NotificationCenterEvent.Share(url = values[index]),
                        )
                    }
                },
            )
        }

        if (sortBottomSheetOpened || defaultSortBottomSheetOpened) {
            SortBottomSheet(
                values = uiState.availableSortTypes,
                expandTop = true,
                onSelected = { value ->
                    val wasDefaultSortBottomSheetOpened = defaultSortBottomSheetOpened
                    sortBottomSheetOpened = false
                    defaultSortBottomSheetOpened = false
                    if (value != null) {
                        notificationCenter.send(
                            NotificationCenterEvent.ChangeSortType(
                                value = value,
                                defaultForCommunity = wasDefaultSortBottomSheetOpened,
                                screenKey = uiState.community.readableHandle,
                            ),
                        )
                    }
                },
            )
        }

        copyPostBottomSheet?.also { post ->
            val titleCanBeCopied = post.title.isNotBlank()
            val textCanBeCopied = post.text.isNotBlank()
            val texts = mutableListOf<String>()
            val values = mutableListOf<CustomModalBottomSheetItem>()
            if (titleCanBeCopied) {
                texts += post.title
                values += CustomModalBottomSheetItem(label = LocalStrings.current.copyTitle)
            }
            if (textCanBeCopied) {
                texts += post.text
                values += CustomModalBottomSheetItem(label = LocalStrings.current.copyText)
                if (titleCanBeCopied) {
                    texts +=
                        buildString {
                            append(post.title)
                            append("\n")
                            append(post.text)
                        }
                    values += CustomModalBottomSheetItem(label = LocalStrings.current.copyBoth)
                }
            }
            CustomModalBottomSheet(
                title = LocalStrings.current.actionCopyClipboard,
                items = values,
                onSelected = { index ->
                    copyPostBottomSheet = null
                    if (index != null) {
                        val text = texts[index]
                        clipboardManager.setText(AnnotatedString(text))
                    }
                },
            )
        }
    }
}
