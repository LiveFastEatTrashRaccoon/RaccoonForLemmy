package com.livefast.eattrash.raccoonforlemmy.unit.postlist

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
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.ArrowCircleUp
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
import cafe.adriel.voyager.koin.getScreenModel
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.PostLayout
import com.livefast.eattrash.raccoonforlemmy.core.appearance.di.getThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.FloatingActionButtonMenu
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.FloatingActionButtonMenuItem
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.SwipeAction
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.SwipeActionCard
import com.livefast.eattrash.raccoonforlemmy.core.commonui.detailopener.api.getDetailOpener
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.Option
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.OptionId
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.PostCard
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.PostCardPlaceholder
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.di.getFabNestedScrollConnection
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.BlockBottomSheet
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.CopyPostBottomSheet
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.ListingTypeBottomSheet
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.ShareBottomSheet
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.SortBottomSheet
import com.livefast.eattrash.raccoonforlemmy.core.l10n.messages.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.TabNavigationSection
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getDrawerCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.ActionOnSwipe
import com.livefast.eattrash.raccoonforlemmy.core.persistence.di.getSettingsRepository
import com.livefast.eattrash.raccoonforlemmy.core.utils.keepscreenon.rememberKeepScreenOn
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.readableHandle
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.readableName
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toInt
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
        val model = getScreenModel<PostListMviModel>()
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

                        is PostListMviModel.Effect.TriggerCopy -> {
                            clipboardManager.setText(AnnotatedString(text = effect.text))
                        }
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
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            topBar = {
                PostsTopBar(
                    currentInstance = uiState.instance,
                    listingType = uiState.listingType,
                    sortType = uiState.sortType,
                    scrollBehavior = scrollBehavior,
                    topAppBarState = topAppBarState,
                    edgeToEdge = settings.edgeToEdge,
                    onHamburgerTapped = {
                        scope.launch {
                            drawerCoordinator.toggleDrawer()
                        }
                    },
                    onSelectListingType = {
                        val sheet =
                            ListingTypeBottomSheet(
                                isLogged = uiState.isLogged,
                                screenKey = "postList",
                            )
                        navigationCoordinator.showBottomSheet(sheet)
                    },
                    onSelectInstance =
                        {
                            navigationCoordinator.showBottomSheet(SelectInstanceBottomSheet())
                        }.takeIf { uiState.isLogged },
                    onSelectSortType = {
                        val sheet =
                            SortBottomSheet(
                                values = uiState.availableSortTypes.map { it.toInt() },
                                expandTop = true,
                                screenKey = "postList",
                            )
                        navigationCoordinator.showBottomSheet(sheet)
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
                                    it.updateDate
                                        ?: it.publishDate
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
                                                        imageVector = Icons.Default.ArrowCircleUp,
                                                        contentDescription = null,
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
                                                        imageVector = Icons.Default.ArrowCircleDown,
                                                        contentDescription = null,
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
                                                        contentDescription = null,
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
                                                        contentDescription = null,
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
                                        onClick = {
                                            model.reduce(PostListMviModel.Intent.MarkAsRead(post.id))
                                            model.reduce(PostListMviModel.Intent.WillOpenDetail)
                                            detailOpener.openPostDetail(post)
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
                                                model.reduce(PostListMviModel.Intent.MarkAsRead(post.id))
                                                model.reduce(PostListMviModel.Intent.WillOpenDetail)
                                                detailOpener.openPostDetail(post)
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
                                                        val screen = ShareBottomSheet(urls = urls)
                                                        navigationCoordinator.showBottomSheet(screen)
                                                    }
                                                }

                                                OptionId.Block -> {
                                                    val screen =
                                                        BlockBottomSheet(
                                                            userName =
                                                                post.creator?.readableName(
                                                                    uiState.preferNicknames,
                                                                ),
                                                            userId = post.creator?.id,
                                                            communityName =
                                                                post.community?.readableName(
                                                                    uiState.preferNicknames,
                                                                ),
                                                            communityId = post.community?.id,
                                                            instanceName = post.community?.host,
                                                            instanceId = post.community?.instanceId,
                                                            userInstanceName = post.creator?.host,
                                                            userInstanceId = post.creator?.instanceId,
                                                        )
                                                    navigationCoordinator.showBottomSheet(screen)
                                                }

                                                OptionId.Copy -> {
                                                    val texts =
                                                        listOfNotNull(
                                                            post.title.takeIf { it.isNotBlank() },
                                                            post.text.takeIf { it.isNotBlank() },
                                                        ).distinct()
                                                    if (texts.size == 1) {
                                                        model.reduce(
                                                            PostListMviModel.Intent.Copy(texts.first()),
                                                        )
                                                    } else {
                                                        val screen =
                                                            CopyPostBottomSheet(
                                                                title = post.title,
                                                                text = post.text,
                                                            )
                                                        navigationCoordinator.showBottomSheet(screen)
                                                    }
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
    }
}
