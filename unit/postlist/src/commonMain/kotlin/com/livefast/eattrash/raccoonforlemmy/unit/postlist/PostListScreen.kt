package com.livefast.eattrash.raccoonforlemmy.unit.postlist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.SyncDisabled
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.kodein.rememberScreenModel
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.PostLayout
import com.livefast.eattrash.raccoonforlemmy.core.appearance.di.getThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.IconSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.FloatingActionButtonMenu
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.FloatingActionButtonMenuItem
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.SwipeAction
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.SwipeActionCard
import com.livefast.eattrash.raccoonforlemmy.core.commonui.detailopener.api.getDetailOpener
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.BlockActionType
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.Option
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.OptionId
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.PostCard
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.PostCardPlaceholder
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.di.getFabNestedScrollConnection
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.toReadableName
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.CustomModalBottomSheet
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.CustomModalBottomSheetItem
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.SortBottomSheet
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.TabNavigationSection
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getDrawerCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.notifications.di.getNotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.ActionOnSwipe
import com.livefast.eattrash.raccoonforlemmy.core.persistence.di.getSettingsRepository
import com.livefast.eattrash.raccoonforlemmy.core.utils.VoteAction
import com.livefast.eattrash.raccoonforlemmy.core.utils.keepscreenon.rememberKeepScreenOn
import com.livefast.eattrash.raccoonforlemmy.core.utils.toIcon
import com.livefast.eattrash.raccoonforlemmy.core.utils.toModifier
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.ListingType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.readableHandle
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.readableName
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toIcon
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toReadableName
import com.livefast.eattrash.raccoonforlemmy.unit.moderatewithreason.ModerateWithReasonAction
import com.livefast.eattrash.raccoonforlemmy.unit.moderatewithreason.ModerateWithReasonScreen
import com.livefast.eattrash.raccoonforlemmy.unit.moderatewithreason.toInt
import com.livefast.eattrash.raccoonforlemmy.unit.postlist.components.PostsTopBar
import com.livefast.eattrash.raccoonforlemmy.unit.rawcontent.RawContentDialog
import com.livefast.eattrash.raccoonforlemmy.unit.selectinstance.SelectInstanceBottomSheet
import com.livefast.eattrash.raccoonforlemmy.unit.zoomableimage.ZoomableImageScreen
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class PostListScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val model: PostListMviModel = rememberScreenModel()
        val uiState by model.uiState.collectAsState()
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
        val lazyListState = rememberLazyListState()
        val drawerCoordinator = remember { getDrawerCoordinator() }
        val scope = rememberCoroutineScope()
        val notificationCenter = remember { getNotificationCenter() }
        var rawContent by remember { mutableStateOf<Any?>(null) }
        val settingsRepository = remember { getSettingsRepository() }
        val settings by settingsRepository.currentSettings.collectAsState()
        val keepScreenOn = rememberKeepScreenOn()
        val detailOpener = remember { getDetailOpener() }
        val connection = navigationCoordinator.getBottomBarScrollConnection()
        val bottomNavigationInset =
            with(LocalDensity.current) {
                WindowInsets.navigationBars.getBottom(this).toDp()
            }
        val clipboardManager = LocalClipboardManager.current
        var itemIdToDelete by remember { mutableStateOf<Long?>(null) }
        var listingTypeBottomSheetOpened by remember { mutableStateOf(false) }
        var shareBottomSheetUrls by remember { mutableStateOf<List<String>?>(null) }
        var blockBottomSheetItems by remember {
            mutableStateOf<List<Triple<BlockActionType, Long?, String?>>?>(null)
        }
        var sortBottomSheetOpened by remember { mutableStateOf(false) }
        var copyPostBottomSheet by remember { mutableStateOf<PostModel?>(null) }
        var selectInstanceBottomSheetOpened by remember { mutableStateOf(false) }

        LaunchedEffect(navigationCoordinator) {
            navigationCoordinator.onDoubleTabSelection
                .onEach { section ->
                    if (section == TabNavigationSection.Home) {
                        runCatching {
                            lazyListState.scrollToItem(0)
                            topAppBarState.heightOffset = 0f
                            topAppBarState.contentOffset = 0f
                        }
                    }
                }.launchIn(this)
        }
        LaunchedEffect(model) {
            model.effects
                .onEach { effect ->
                    when (effect) {
                        PostListMviModel.Effect.BackToTop -> {
                            runCatching {
                                lazyListState.scrollToItem(0)
                                topAppBarState.heightOffset = 0f
                                topAppBarState.contentOffset = 0f
                            }
                        }

                        is PostListMviModel.Effect.ZombieModeTick -> {
                            runCatching {
                                if (effect.index >= 0) {
                                    lazyListState.animateScrollBy(
                                        value = settings.zombieModeScrollAmount,
                                        animationSpec = tween(350),
                                    )
                                }
                            }
                        }

                        is PostListMviModel.Effect.OpenDetail ->
                            detailOpener.openPostDetail(effect.post)
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

        Scaffold(
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            topBar = {
                PostsTopBar(
                    currentInstance = uiState.instance,
                    listingType = uiState.listingType,
                    sortType = uiState.sortType,
                    scrollBehavior = scrollBehavior,
                    topAppBarState = topAppBarState,
                    onHamburgerTapped = {
                        scope.launch {
                            drawerCoordinator.toggleDrawer()
                        }
                    },
                    onSelectListingType = {
                        listingTypeBottomSheetOpened = true
                    },
                    onSelectInstance =
                        {
                            selectInstanceBottomSheetOpened = true
                        }.takeIf { !uiState.isLogged },
                    onSelectSortType = {
                        sortBottomSheetOpened = true
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
                        modifier =
                            Modifier.padding(
                                bottom = Spacing.xxxl + Spacing.s + bottomNavigationInset,
                            ),
                        items =
                            buildList {
                                if (uiState.zombieModeActive) {
                                    this +=
                                        FloatingActionButtonMenuItem(
                                            icon = Icons.Default.SyncDisabled,
                                            text = LocalStrings.current.actionDeactivateZombieMode,
                                            onSelected = {
                                                model.reduce(PostListMviModel.Intent.PauseZombieMode)
                                            },
                                        )
                                } else {
                                    this +=
                                        FloatingActionButtonMenuItem(
                                            icon = Icons.Default.Sync,
                                            text = LocalStrings.current.actionActivateZombieMode,
                                            onSelected = {
                                                model.reduce(
                                                    PostListMviModel.Intent.StartZombieMode(
                                                        -1,
                                                    ),
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
                                if (uiState.isLogged) {
                                    this +=
                                        FloatingActionButtonMenuItem(
                                            icon = Icons.Default.ClearAll,
                                            text = LocalStrings.current.actionClearRead,
                                            onSelected = {
                                                model.reduce(PostListMviModel.Intent.ClearRead)
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
                                                    forceCommunitySelection = true,
                                                )
                                            },
                                        )
                                }
                            },
                    )
                }
            },
        ) { padding ->
            if (uiState.currentUserId != null) {
                PullToRefreshBox(
                    modifier =
                        Modifier
                            .padding(
                                top = padding.calculateTopPadding(),
                            ).then(
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
                            ).nestedScroll(fabNestedScrollConnection),
                    isRefreshing = uiState.refreshing,
                    onRefresh = {
                        model.reduce(PostListMviModel.Intent.Refresh())
                    },
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = lazyListState,
                        userScrollEnabled = !uiState.zombieModeActive,
                    ) {
                        if (uiState.posts.isEmpty() && uiState.initial && uiState.loading) {
                            items(5) {
                                PostCardPlaceholder(
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
                            // isLogged is added to the key to force swipe action refresh
                            key = {
                                it.id.toString() + (
                                    it.updateDate ?: it.publishDate
                                ) + uiState.isLogged
                            },
                        ) { post ->
                            LaunchedEffect(post.id) {
                                if (settings.markAsReadWhileScrolling && !post.read) {
                                    model.reduce(PostListMviModel.Intent.MarkAsRead(post.id))
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
                                                        contentDescription = LocalStrings.current.actionUpvote,
                                                        tint = Color.White,
                                                    )
                                                },
                                                backgroundColor = upVoteColor ?: defaultUpvoteColor,
                                                onTriggered = {
                                                    model.reduce(
                                                        PostListMviModel.Intent.UpVotePost(
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
                                                        contentDescription = LocalStrings.current.actionDownvote,
                                                        tint = Color.White,
                                                    )
                                                },
                                                backgroundColor =
                                                    downVoteColor
                                                        ?: defaultDownVoteColor,
                                                onTriggered = {
                                                    model.reduce(
                                                        PostListMviModel.Intent.DownVotePost(
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
                                                        contentDescription = LocalStrings.current.actionReply,
                                                        tint = Color.White,
                                                    )
                                                },
                                                backgroundColor = replyColor ?: defaultReplyColor,
                                                onTriggered = {
                                                    detailOpener.openReply(originalPost = post)
                                                },
                                            )

                                        ActionOnSwipe.Save ->
                                            SwipeAction(
                                                swipeContent = {
                                                    Icon(
                                                        imageVector = Icons.Default.Bookmark,
                                                        contentDescription = LocalStrings.current.actionAddToBookmarks,
                                                        tint = Color.White,
                                                    )
                                                },
                                                backgroundColor = saveColor ?: defaultSaveColor,
                                                onTriggered = {
                                                    model.reduce(
                                                        PostListMviModel.Intent.SavePost(
                                                            post.id,
                                                        ),
                                                    )
                                                },
                                            )

                                        ActionOnSwipe.Edit ->
                                            SwipeAction(
                                                swipeContent = {
                                                    Icon(
                                                        imageVector = Icons.Default.Edit,
                                                        contentDescription = LocalStrings.current.postActionEdit,
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
                                    model.reduce(PostListMviModel.Intent.HapticIndication)
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
                                        post = post,
                                        postLayout = uiState.postLayout,
                                        limitBodyHeight = true,
                                        isCurrentUser = post.creator?.id == uiState.currentUserId,
                                        showBot = true,
                                        fullHeightImage = uiState.fullHeightImages,
                                        fullWidthImage = uiState.fullWidthImages,
                                        voteFormat = uiState.voteFormat,
                                        autoLoadImages = uiState.autoLoadImages,
                                        preferNicknames = uiState.preferNicknames,
                                        showScores = uiState.showScores,
                                        actionButtonsActive = uiState.isLogged,
                                        blurNsfw = uiState.blurNsfw,
                                        fadeRead = uiState.fadeReadPosts,
                                        showUnreadComments = uiState.showUnreadComments,
                                        downVoteEnabled = uiState.downVoteEnabled,
                                        botTagColor = uiState.botTagColor,
                                        meTagColor = uiState.meTagColor,
                                        onClick = {
                                            model.reduce(PostListMviModel.Intent.WillOpenDetail(post.id))
                                        },
                                        onDoubleClick =
                                            {
                                                model.reduce(
                                                    PostListMviModel.Intent.UpVotePost(post.id),
                                                )
                                            }.takeIf { uiState.doubleTapActionEnabled && uiState.isLogged },
                                        onOpenCommunity = { community, instance ->
                                            detailOpener.openCommunityDetail(
                                                community = community,
                                                otherInstance = instance,
                                            )
                                        },
                                        onOpenCreator = { user, instance ->
                                            detailOpener.openUserDetail(
                                                user = user,
                                                otherInstance = instance,
                                            )
                                        },
                                        onUpVote = {
                                            if (uiState.isLogged) {
                                                model.reduce(
                                                    PostListMviModel.Intent.UpVotePost(post.id),
                                                )
                                            }
                                        },
                                        onDownVote = {
                                            if (uiState.isLogged) {
                                                model.reduce(
                                                    PostListMviModel.Intent.DownVotePost(
                                                        post.id,
                                                    ),
                                                )
                                            }
                                        },
                                        onSave = {
                                            if (uiState.isLogged) {
                                                model.reduce(PostListMviModel.Intent.SavePost(post.id))
                                            }
                                        },
                                        onReply = {
                                            if (uiState.isLogged) {
                                                model.reduce(
                                                    PostListMviModel.Intent.WillOpenDetail(post.id),
                                                )
                                            }
                                        },
                                        onOpenImage = { url ->
                                            model.reduce(PostListMviModel.Intent.MarkAsRead(post.id))
                                            navigationCoordinator.pushScreen(
                                                ZoomableImageScreen(
                                                    url = url,
                                                    source = post.community?.readableHandle.orEmpty(),
                                                ),
                                            )
                                        },
                                        onOpenVideo = { url ->
                                            navigationCoordinator.pushScreen(
                                                ZoomableImageScreen(
                                                    url = url,
                                                    isVideo = true,
                                                    source = post.community?.readableHandle.orEmpty(),
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
                                                if (uiState.isLogged) {
                                                    this +=
                                                        Option(
                                                            OptionId.ToggleRead,
                                                            LocalStrings.current.actionToggleRead,
                                                        )
                                                    this +=
                                                        Option(
                                                            OptionId.Hide,
                                                            LocalStrings.current.postActionHide,
                                                        )
                                                    this +=
                                                        Option(
                                                            OptionId.Block,
                                                            LocalStrings.current.communityDetailBlock,
                                                        )
                                                }
                                                this +=
                                                    Option(
                                                        OptionId.SeeRaw,
                                                        LocalStrings.current.postActionSeeRaw,
                                                    )
                                                if (uiState.isLogged) {
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
                                                if (post.creator?.id == uiState.currentUserId) {
                                                    this +=
                                                        Option(
                                                            OptionId.Edit,
                                                            LocalStrings.current.postActionEdit,
                                                        )
                                                    this +=
                                                        Option(
                                                            OptionId.Delete,
                                                            LocalStrings.current.commentActionDelete,
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
                                                        PostListMviModel.Intent.Hide(post.id),
                                                    )

                                                OptionId.Share -> {
                                                    val urls =
                                                        listOfNotNull(
                                                            post.originalUrl,
                                                            "https://${uiState.instance}/post/${post.id}",
                                                        ).distinct()
                                                    if (urls.size == 1) {
                                                        model.reduce(
                                                            PostListMviModel.Intent.Share(urls.first()),
                                                        )
                                                    } else {
                                                        shareBottomSheetUrls = urls
                                                    }
                                                }

                                                OptionId.Block -> {
                                                    blockBottomSheetItems =
                                                        buildList {
                                                            val userName =
                                                                post.creator?.readableName(
                                                                    uiState.preferNicknames,
                                                                )
                                                            val userId = post.creator?.id
                                                            val communityName =
                                                                post.community?.readableName(
                                                                    uiState.preferNicknames,
                                                                )
                                                            val communityId = post.community?.id
                                                            val instanceName = post.community?.host
                                                            val instanceId =
                                                                post.community?.instanceId
                                                            val userInstanceName =
                                                                post.creator?.host
                                                            val userInstanceId =
                                                                post.creator?.instanceId
                                                            if (userName != null && userId != null) {
                                                                this +=
                                                                    Triple(
                                                                        first = BlockActionType.User,
                                                                        second = userId,
                                                                        third = userName,
                                                                    )
                                                            }
                                                            if (communityName != null && communityId != null) {
                                                                this +=
                                                                    Triple(
                                                                        first = BlockActionType.Community,
                                                                        second = communityId,
                                                                        third = communityName,
                                                                    )
                                                            }
                                                            if (instanceName != null && instanceId != null) {
                                                                this +=
                                                                    Triple(
                                                                        first = BlockActionType.Instance,
                                                                        second = instanceId,
                                                                        third = instanceName,
                                                                    )
                                                            }
                                                            if (userInstanceName != null &&
                                                                userInstanceId != null &&
                                                                userInstanceName != instanceName
                                                            ) {
                                                                this +=
                                                                    Triple(
                                                                        first = BlockActionType.Instance,
                                                                        second = userInstanceId,
                                                                        third = userInstanceName,
                                                                    )
                                                            }
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

                                                OptionId.ToggleRead -> {
                                                    model.reduce(
                                                        PostListMviModel.Intent.ToggleRead(post.id),
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
                                    model.reduce(PostListMviModel.Intent.LoadNextPage)
                                } else {
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(top = Spacing.s),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Button(
                                            onClick = {
                                                model.reduce(PostListMviModel.Intent.LoadNextPage)
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
                        if (uiState.posts.isEmpty() && !uiState.initial && !uiState.loading) {
                            item {
                                Text(
                                    modifier = Modifier.fillMaxWidth().padding(top = Spacing.xs),
                                    textAlign = TextAlign.Center,
                                    text = LocalStrings.current.messageEmptyList,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onBackground,
                                )
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(Spacing.xxxl))
                        }
                    }
                }
            } else if (!uiState.initial) {
                Column(
                    modifier = Modifier.padding(padding),
                    verticalArrangement = Arrangement.spacedBy(Spacing.s),
                ) {
                    Text(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(top = Spacing.xs),
                        textAlign = TextAlign.Center,
                        text = LocalStrings.current.messageGenericError,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    Button(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        onClick = {
                            model.reduce(PostListMviModel.Intent.Refresh(hardReset = true))
                        },
                    ) {
                        Text(LocalStrings.current.buttonRetry)
                    }
                }
            }
        }

        if (rawContent != null) {
            when (val content = rawContent) {
                is PostModel -> {
                    RawContentDialog(
                        title = content.title,
                        publishDate = content.publishDate,
                        updateDate = content.updateDate,
                        url = content.url,
                        text = content.text,
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
                            model.reduce(PostListMviModel.Intent.DeletePost(itemId))
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

        if (listingTypeBottomSheetOpened) {
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
                    listingTypeBottomSheetOpened = false
                    if (index != null) {
                        notificationCenter.send(
                            NotificationCenterEvent.ChangeFeedType(
                                value = values[index],
                                screenKey = "postList",
                            ),
                        )
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

        blockBottomSheetItems?.also { values ->
            CustomModalBottomSheet(
                title = LocalStrings.current.communityDetailBlock,
                items =
                    values.map { value ->
                        CustomModalBottomSheetItem(
                            label =
                                buildString {
                                    append(value.first.toReadableName())
                                    val additionalText = value.third
                                    if (!additionalText.isNullOrEmpty()) {
                                        append("\n")
                                        append("(")
                                        append(additionalText)
                                        append(")")
                                    }
                                },
                        )
                    },
                onSelected = { index ->
                    blockBottomSheetItems = null
                    if (index != null) {
                        val value = values[index]
                        val event =
                            when (value.first) {
                                BlockActionType.Community ->
                                    NotificationCenterEvent.BlockActionSelected(
                                        communityId = value.second,
                                    )

                                BlockActionType.Instance ->
                                    NotificationCenterEvent.BlockActionSelected(
                                        instanceId = value.second,
                                    )

                                BlockActionType.User ->
                                    NotificationCenterEvent.BlockActionSelected(
                                        userId = value.second,
                                    )
                            }
                        notificationCenter.send(event)
                    }
                },
            )
        }

        if (sortBottomSheetOpened) {
            SortBottomSheet(
                values = uiState.availableSortTypes,
                expandTop = true,
                onSelected = { value ->
                    sortBottomSheetOpened = false
                    if (value != null) {
                        notificationCenter.send(
                            NotificationCenterEvent.ChangeSortType(
                                value = value,
                                screenKey = "postList",
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
    }
}
