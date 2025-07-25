package com.livefast.eattrash.raccoonforlemmy.unit.filteredcontents

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.PostLayout
import com.livefast.eattrash.raccoonforlemmy.core.appearance.di.getThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.toWindowInsets
import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.getViewModel
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.FloatingActionButtonMenu
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.FloatingActionButtonMenuItem
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.SectionSelector
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.SwipeAction
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.SwipeActionCard
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.Option
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.OptionId
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.PostCard
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.PostCardPlaceholder
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.di.getFabNestedScrollConnection
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.CustomModalBottomSheet
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.CustomModalBottomSheetItem
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getDrawerCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getMainRouter
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.notifications.di.getNotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.ActionOnSwipe
import com.livefast.eattrash.raccoonforlemmy.core.persistence.di.getSettingsRepository
import com.livefast.eattrash.raccoonforlemmy.core.utils.VoteAction
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.onClick
import com.livefast.eattrash.raccoonforlemmy.core.utils.toIcon
import com.livefast.eattrash.raccoonforlemmy.core.utils.toModifier
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommentModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.readableHandle
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.readableName
import com.livefast.eattrash.raccoonforlemmy.unit.filteredcontents.components.ModdedCommentCard
import com.livefast.eattrash.raccoonforlemmy.unit.filteredcontents.components.ModdedCommentPlaceholder
import com.livefast.eattrash.raccoonforlemmy.unit.filteredcontents.di.FilteredContentsMviModelParams
import com.livefast.eattrash.raccoonforlemmy.unit.moderatewithreason.ModerateWithReasonAction
import com.livefast.eattrash.raccoonforlemmy.unit.moderatewithreason.toInt
import com.livefast.eattrash.raccoonforlemmy.unit.rawcontent.RawContentDialog
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilteredContentsScreen(
    type: Int,
    modifier: Modifier = Modifier,
    model: FilteredContentsMviModel = getViewModel<FilteredContentsViewModel>(FilteredContentsMviModelParams(type)),
    lazyListState: LazyListState = rememberLazyListState(),
) {
    val uiState by model.uiState.collectAsState()
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)
    val fabNestedScrollConnection = remember { getFabNestedScrollConnection() }
    val isFabVisible by fabNestedScrollConnection.isFabVisible.collectAsState()
    val navigationCoordinator = remember { getNavigationCoordinator() }
    val drawerCoordinator = remember { getDrawerCoordinator() }
    val settingsRepository = remember { getSettingsRepository() }
    val settings by settingsRepository.currentSettings.collectAsState()
    val mainRouter = remember { getMainRouter() }
    val notificationCenter = remember { getNotificationCenter() }
    var rawContent by remember { mutableStateOf<Any?>(null) }
    val themeRepository = remember { getThemeRepository() }
    val upVoteColor by themeRepository.upVoteColor.collectAsState()
    val downVoteColor by themeRepository.downVoteColor.collectAsState()
    val replyColor by themeRepository.replyColor.collectAsState()
    val saveColor by themeRepository.saveColor.collectAsState()
    val defaultUpvoteColor = MaterialTheme.colorScheme.primary
    val defaultReplyColor = MaterialTheme.colorScheme.secondary
    val defaultSaveColor = MaterialTheme.colorScheme.secondaryContainer
    val defaultDownVoteColor = MaterialTheme.colorScheme.tertiary
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val navigationCanPop by navigationCoordinator.canPop.collectAsState()
    val isTopLevel = !navigationCanPop
    val connection =
        if (isTopLevel) {
            navigationCoordinator.getBottomBarScrollConnection()
        } else {
            null
        }
    val bottomNavigationInset =
        with(LocalDensity.current) {
            WindowInsets.navigationBars.getBottom(this).toDp()
        }
    var likedTypeBottomSheetOpened by remember { mutableStateOf(false) }

    LaunchedEffect(model) {
        model.effects
            .onEach { effect ->
                when (effect) {
                    FilteredContentsMviModel.Effect.BackToTop -> {
                        runCatching {
                            lazyListState.scrollToItem(0)
                            topAppBarState.heightOffset = 0f
                            topAppBarState.contentOffset = 0f
                        }
                    }

                    is FilteredContentsMviModel.Effect.OpenDetail ->
                        mainRouter.openPostDetail(
                            post = PostModel(id = effect.postId),
                            highlightCommentId = effect.commentId,
                            isMod = true,
                        )
                }
            }.launchIn(this)
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
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        modifier = modifier,
        topBar = {
            TopAppBar(
                windowInsets = topAppBarState.toWindowInsets(),
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    if (navigationCoordinator.canPop.value) {
                        IconButton(
                            onClick = {
                                navigationCoordinator.pop()
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
                    Column(modifier = Modifier.padding(horizontal = Spacing.s)) {
                        Text(
                            text =
                            when (uiState.contentsType) {
                                FilteredContentsType.Moderated ->
                                    LocalStrings.current.moderatorZoneActionContents

                                FilteredContentsType.Votes -> LocalStrings.current.profileUpvotesDownvotes
                                FilteredContentsType.Bookmarks ->
                                    LocalStrings.current.navigationDrawerTitleBookmarks

                                FilteredContentsType.Hidden -> LocalStrings.current.settingsHiddenPosts
                            },
                            style = MaterialTheme.typography.titleMedium,
                        )
                        if (uiState.contentsType == FilteredContentsType.Votes) {
                            val text =
                                when (uiState.liked) {
                                    true -> LocalStrings.current.actionUpvote
                                    else -> LocalStrings.current.actionDownvote
                                }
                            Text(
                                modifier =
                                Modifier.onClick(
                                    onClick = {
                                        likedTypeBottomSheetOpened = true
                                    },
                                ),
                                text = text,
                                style = MaterialTheme.typography.titleSmall,
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
                    modifier =
                    Modifier.then(
                        if (isTopLevel) {
                            Modifier.padding(
                                bottom = Spacing.xxxl + Spacing.s + bottomNavigationInset,
                            )
                        } else {
                            Modifier
                        },
                    ),
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
                model.reduce(FilteredContentsMviModel.Intent.Refresh)
            },
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = lazyListState,
            ) {
                item {
                    if (!uiState.isPostOnly) {
                        SectionSelector(
                            modifier = Modifier.padding(vertical = Spacing.s),
                            titles =
                            listOf(
                                LocalStrings.current.profileSectionPosts,
                                LocalStrings.current.profileSectionComments,
                            ),
                            currentSection =
                            when (uiState.section) {
                                FilteredContentsSection.Comments -> 1
                                else -> 0
                            },
                            onSectionSelected = {
                                val section =
                                    when (it) {
                                        1 -> FilteredContentsSection.Comments
                                        else -> FilteredContentsSection.Posts
                                    }
                                model.reduce(
                                    FilteredContentsMviModel.Intent.ChangeSection(
                                        section,
                                    ),
                                )
                            },
                        )
                    }
                }

                if (uiState.section == FilteredContentsSection.Posts) {
                    if (uiState.posts.isEmpty() && uiState.loading && uiState.initial) {
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
                    items(
                        items = uiState.posts,
                        key = {
                            it.id.toString() + (it.updateDate ?: it.publishDate)
                        },
                    ) { post ->

                        @Composable
                        fun List<ActionOnSwipe>.toSwipeActions(): List<SwipeAction> = mapNotNull {
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
                                                FilteredContentsMviModel.Intent.UpVotePost(
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
                                                FilteredContentsMviModel.Intent.DownVotePost(
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
                                            mainRouter.openReply(originalPost = post)
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
                                                FilteredContentsMviModel.Intent.SavePost(
                                                    post.id,
                                                ),
                                            )
                                        },
                                    )

                                else -> null
                            }
                        }
                        SwipeActionCard(
                            modifier = Modifier.fillMaxWidth(),
                            enabled = uiState.swipeActionsEnabled,
                            onGestureBegin = {
                                model.reduce(FilteredContentsMviModel.Intent.HapticIndication)
                            },
                            swipeToStartActions = uiState.actionsOnSwipeToStartPosts.toSwipeActions(),
                            swipeToEndActions = uiState.actionsOnSwipeToEndPosts.toSwipeActions(),
                            content = {
                                PostCard(
                                    post = post,
                                    postLayout = uiState.postLayout,
                                    limitBodyHeight = true,
                                    showBot = true,
                                    isCurrentUser = post.creator?.id == uiState.currentUserId,
                                    fullHeightImage = uiState.fullHeightImages,
                                    fullWidthImage = uiState.fullWidthImages,
                                    voteFormat = uiState.voteFormat,
                                    autoLoadImages = uiState.autoLoadImages,
                                    preferNicknames = uiState.preferNicknames,
                                    fadeRead = uiState.fadeReadPosts,
                                    showUnreadComments = uiState.showUnreadComments,
                                    downVoteEnabled = uiState.downVoteEnabled,
                                    botTagColor = uiState.botTagColor,
                                    meTagColor = uiState.meTagColor,
                                    onClick = {
                                        model.reduce(
                                            FilteredContentsMviModel.Intent.WillOpenDetail(
                                                postId = post.id,
                                            ),
                                        )
                                    },
                                    onOpenCommunity = { community, instance ->
                                        mainRouter.openCommunityDetail(
                                            community,
                                            instance,
                                        )
                                    },
                                    onOpenCreator = { user, instance ->
                                        mainRouter.openUserDetail(user, instance)
                                    },
                                    onUpVote = {
                                        model.reduce(
                                            FilteredContentsMviModel.Intent.UpVotePost(post.id),
                                        )
                                    },
                                    onDownVote = {
                                        model.reduce(
                                            FilteredContentsMviModel.Intent.DownVotePost(
                                                post.id,
                                            ),
                                        )
                                    },
                                    onSave = {
                                        model.reduce(
                                            FilteredContentsMviModel.Intent.SavePost(post.id),
                                        )
                                    },
                                    onReply = {
                                        model.reduce(
                                            FilteredContentsMviModel.Intent.WillOpenDetail(
                                                postId = post.id,
                                            ),
                                        )
                                    },
                                    onOpenImage = { url ->
                                        mainRouter.openImage(
                                            url = url,
                                            source = post.community?.readableHandle.orEmpty(),
                                        )
                                    },
                                    onOpenVideo = { url ->
                                        mainRouter.openImage(
                                            url = url,
                                            isVideo = true,
                                            source = post.community?.readableHandle.orEmpty(),
                                        )
                                    },
                                    options =
                                    buildList {
                                        this +=
                                            Option(
                                                OptionId.SeeRaw,
                                                LocalStrings.current.postActionSeeRaw,
                                            )
                                        if (uiState.contentsType == FilteredContentsType.Moderated) {
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
                                                    OptionId.BanUser,
                                                    if (post.creator?.banned == true) {
                                                        LocalStrings.current.modActionAllow
                                                    } else {
                                                        LocalStrings.current.modActionBan
                                                    },
                                                )
                                            this +=
                                                Option(
                                                    OptionId.Remove,
                                                    LocalStrings.current.modActionRemove,
                                                )
                                        }
                                        if (
                                            uiState.isAdmin &&
                                            uiState.contentsType == FilteredContentsType.Moderated
                                        ) {
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
                                                                creator.readableName(uiState.preferNicknames),
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
                                    onSelectOption = { optionId ->
                                        when (optionId) {
                                            OptionId.SeeRaw -> {
                                                rawContent = post
                                            }

                                            OptionId.FeaturePost ->
                                                model.reduce(
                                                    FilteredContentsMviModel.Intent.ModFeaturePost(
                                                        post.id,
                                                    ),
                                                )

                                            OptionId.AdminFeaturePost ->
                                                model.reduce(
                                                    FilteredContentsMviModel.Intent.AdminFeaturePost(
                                                        post.id,
                                                    ),
                                                )

                                            OptionId.LockPost ->
                                                model.reduce(
                                                    FilteredContentsMviModel.Intent.ModLockPost(
                                                        post.id,
                                                    ),
                                                )

                                            OptionId.Remove -> {
                                                mainRouter.openModerateWithReason(
                                                    actionId = ModerateWithReasonAction.RemovePost.toInt(),
                                                    contentId = post.id,
                                                )
                                            }

                                            OptionId.BanUser -> {
                                                post.creator?.id?.also { userId ->
                                                    post.community?.id?.also { communityId ->
                                                        mainRouter.openBanUser(
                                                            userId = userId,
                                                            communityId = communityId,
                                                            newValue = post.creator?.banned != true,
                                                            postId = post.id,
                                                        )
                                                    }
                                                }
                                            }

                                            OptionId.Purge -> {
                                                mainRouter.openModerateWithReason(
                                                    actionId = ModerateWithReasonAction.PurgePost.toInt(),
                                                    contentId = post.id,
                                                )
                                            }

                                            OptionId.PurgeCreator -> {
                                                post.creator?.id?.also { userId ->
                                                    mainRouter.openModerateWithReason(
                                                        actionId = ModerateWithReasonAction.PurgeUser.toInt(),
                                                        contentId = userId,
                                                    )
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
                } else {
                    if (uiState.comments.isEmpty() && uiState.loading && uiState.initial) {
                        items(5) {
                            ModdedCommentPlaceholder(postLayout = uiState.postLayout)
                            if (uiState.postLayout != PostLayout.Card) {
                                HorizontalDivider(modifier = Modifier.padding(vertical = Spacing.interItem))
                            } else {
                                Spacer(modifier = Modifier.height(Spacing.interItem))
                            }
                        }
                    }
                    if (uiState.comments.isEmpty() && !uiState.initial && !uiState.loading) {
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
                    items(
                        items = uiState.comments,
                        key = { it.id.toString() + (it.updateDate ?: it.publishDate) },
                    ) { comment ->

                        @Composable
                        fun List<ActionOnSwipe>.toSwipeActions(): List<SwipeAction> = mapNotNull {
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
                                                FilteredContentsMviModel.Intent.UpVoteComment(
                                                    comment.id,
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
                                                FilteredContentsMviModel.Intent.DownVoteComment(
                                                    comment.id,
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
                                            mainRouter.openReply(
                                                originalPost = PostModel(comment.postId),
                                                originalComment = comment,
                                            )
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
                                                FilteredContentsMviModel.Intent.SaveComment(
                                                    comment.id,
                                                ),
                                            )
                                        },
                                    )

                                else -> null
                            }
                        }

                        SwipeActionCard(
                            modifier = Modifier.fillMaxWidth(),
                            enabled = uiState.swipeActionsEnabled,
                            onGestureBegin = {
                                model.reduce(FilteredContentsMviModel.Intent.HapticIndication)
                            },
                            swipeToStartActions = uiState.actionsOnSwipeToStartComments.toSwipeActions(),
                            swipeToEndActions = uiState.actionsOnSwipeToEndComments.toSwipeActions(),
                            content = {
                                ModdedCommentCard(
                                    comment = comment,
                                    postLayout = uiState.postLayout,
                                    voteFormat = uiState.voteFormat,
                                    autoLoadImages = uiState.autoLoadImages,
                                    preferNicknames = uiState.preferNicknames,
                                    downVoteEnabled = uiState.downVoteEnabled,
                                    isCurrentUser = comment.creator?.id == uiState.currentUserId,
                                    onOpenUser = { user, instance ->
                                        mainRouter.openUserDetail(user, instance)
                                    },
                                    onOpen = {
                                        model.reduce(
                                            FilteredContentsMviModel.Intent.WillOpenDetail(
                                                postId = comment.postId,
                                                commentId = comment.id,
                                            ),
                                        )
                                    },
                                    onUpVote = {
                                        model.reduce(
                                            FilteredContentsMviModel.Intent.UpVoteComment(
                                                comment.id,
                                            ),
                                        )
                                    },
                                    onDownVote = {
                                        model.reduce(
                                            FilteredContentsMviModel.Intent.DownVoteComment(
                                                comment.id,
                                            ),
                                        )
                                    },
                                    onSave = {
                                        model.reduce(
                                            FilteredContentsMviModel.Intent.SaveComment(comment.id),
                                        )
                                    },
                                    onReply = {
                                        mainRouter.openReply(
                                            originalPost = PostModel(id = comment.postId),
                                            originalComment = comment,
                                        )
                                    },
                                    options =
                                    buildList {
                                        this +=
                                            Option(
                                                OptionId.SeeRaw,
                                                LocalStrings.current.postActionSeeRaw,
                                            )
                                        if (uiState.contentsType == FilteredContentsType.Moderated) {
                                            this +=
                                                Option(
                                                    OptionId.DistinguishComment,
                                                    if (comment.distinguished) {
                                                        LocalStrings.current.modActionUnmarkAsDistinguished
                                                    } else {
                                                        LocalStrings.current.modActionMarkAsDistinguished
                                                    },
                                                )
                                            this +=
                                                Option(
                                                    OptionId.BanUser,
                                                    if (comment.creator?.banned == true) {
                                                        LocalStrings.current.modActionAllow
                                                    } else {
                                                        LocalStrings.current.modActionBan
                                                    },
                                                )
                                            this +=
                                                Option(
                                                    OptionId.Remove,
                                                    LocalStrings.current.modActionRemove,
                                                )
                                        }
                                        if (
                                            uiState.isAdmin &&
                                            uiState.contentsType == FilteredContentsType.Moderated
                                        ) {
                                            this +=
                                                Option(
                                                    OptionId.Purge,
                                                    LocalStrings.current.adminActionPurge,
                                                )
                                            comment.creator?.also { creator ->
                                                this +=
                                                    Option(
                                                        OptionId.PurgeCreator,
                                                        buildString {
                                                            append(LocalStrings.current.adminActionPurge)
                                                            append(" ")
                                                            append(
                                                                creator.readableName(uiState.preferNicknames),
                                                            )
                                                        },
                                                    )
                                            }
                                        }
                                    },
                                    onSelectOption = { optionId ->
                                        when (optionId) {
                                            OptionId.Remove -> {
                                                mainRouter.openModerateWithReason(
                                                    actionId = ModerateWithReasonAction.RemoveComment.toInt(),
                                                    contentId = comment.id,
                                                )
                                            }

                                            OptionId.SeeRaw -> {
                                                rawContent = comment
                                            }

                                            OptionId.DistinguishComment ->
                                                model.reduce(
                                                    FilteredContentsMviModel.Intent.ModDistinguishComment(
                                                        comment.id,
                                                    ),
                                                )

                                            OptionId.BanUser -> {
                                                comment.creator?.id?.also { userId ->
                                                    comment.community?.id?.also { communityId ->
                                                        mainRouter.openBanUser(
                                                            userId = userId,
                                                            communityId = communityId,
                                                            newValue = comment.creator?.banned != true,
                                                            commentId = comment.id,
                                                        )
                                                    }
                                                }
                                            }

                                            OptionId.Purge -> {
                                                mainRouter.openModerateWithReason(
                                                    actionId = ModerateWithReasonAction.PurgeComment.toInt(),
                                                    contentId = comment.id,
                                                )
                                            }

                                            OptionId.PurgeCreator -> {
                                                comment.creator?.id?.also { userId ->
                                                    mainRouter.openModerateWithReason(
                                                        actionId = ModerateWithReasonAction.PurgeUser.toInt(),
                                                        contentId = userId,
                                                    )
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
                }

                item {
                    if (!uiState.initial && !uiState.loading && !uiState.refreshing && uiState.canFetchMore) {
                        model.reduce(FilteredContentsMviModel.Intent.LoadNextPage)
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
        }
    }

    if (rawContent != null) {
        when (val content = rawContent) {
            is PostModel -> {
                RawContentDialog(
                    title = content.title,
                    publishDate = content.publishDate,
                    updateDate = content.updateDate,
                    url = content.originalUrl,
                    text = content.text,
                    upVotes = content.upvotes,
                    downVotes = content.downvotes,
                    onDismiss = {
                        rawContent = null
                    },
                    onQuote = { quotation ->
                        rawContent = null
                        if (quotation != null) {
                            mainRouter.openReply(
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
                    publishDate = content.publishDate,
                    updateDate = content.updateDate,
                    text = content.text,
                    upVotes = content.upvotes,
                    downVotes = content.downvotes,
                    onDismiss = {
                        rawContent = null
                    },
                    onQuote = { quotation ->
                        rawContent = null
                        if (quotation != null) {
                            mainRouter.openReply(
                                originalPost = PostModel(id = content.postId),
                                originalComment = content,
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

    if (likedTypeBottomSheetOpened) {
        val values =
            listOf(
                LocalStrings.current.actionUpvote,
                LocalStrings.current.actionDownvote,
            )
        CustomModalBottomSheet(
            title = LocalStrings.current.inboxListingTypeTitle,
            items =
            values.map { value ->
                CustomModalBottomSheetItem(label = value)
            },
            onSelect = { index ->
                likedTypeBottomSheetOpened = false
                if (index != null) {
                    notificationCenter.send(
                        NotificationCenterEvent.ChangedLikedType(value = index == 0),
                    )
                }
            },
        )
    }
}
