package com.livefast.eattrash.raccoonforlemmy.unit.userdetail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.PostLayout
import com.livefast.eattrash.raccoonforlemmy.core.appearance.di.getThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.toWindowInsets
import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.getViewModel
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.CustomDropDown
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.FloatingActionButtonMenu
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.FloatingActionButtonMenuItem
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.ProgressHud
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.SectionSelector
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.SwipeAction
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.SwipeActionCard
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.CommentCard
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.CommentCardPlaceholder
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.Option
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.OptionId
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.PostCard
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.PostCardPlaceholder
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.UserDetailSection
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.UserHeader
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.di.getFabNestedScrollConnection
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.AssignUserTagBottomSheet
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.CustomModalBottomSheet
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.CustomModalBottomSheetItem
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.EditUserTagDialog
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.SortBottomSheet
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getMainRouter
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.ActionOnSwipe
import com.livefast.eattrash.raccoonforlemmy.core.persistence.di.getSettingsRepository
import com.livefast.eattrash.raccoonforlemmy.core.utils.ValidationError
import com.livefast.eattrash.raccoonforlemmy.core.utils.VoteAction
import com.livefast.eattrash.raccoonforlemmy.core.utils.di.getClipboardHelper
import com.livefast.eattrash.raccoonforlemmy.core.utils.toIcon
import com.livefast.eattrash.raccoonforlemmy.core.utils.toLocalDp
import com.livefast.eattrash.raccoonforlemmy.core.utils.toModifier
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommentModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.readableHandle
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.readableName
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toIcon
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toReadableName
import com.livefast.eattrash.raccoonforlemmy.unit.moderatewithreason.ModerateWithReasonAction
import com.livefast.eattrash.raccoonforlemmy.unit.moderatewithreason.toInt
import com.livefast.eattrash.raccoonforlemmy.unit.rawcontent.RawContentDialog
import com.livefast.eattrash.raccoonforlemmy.unit.userdetail.di.UserDetailMviModelParams
import com.livefast.eattrash.raccoonforlemmy.unit.userinfo.UserInfoScreen
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(userId: Long, modifier: Modifier = Modifier, otherInstance: String = "") {
    val model: UserDetailMviModel =
        getViewModel<UserDetailViewModel>(UserDetailMviModelParams(userId = userId, otherInstance = otherInstance))
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
    val themeRepository = remember { getThemeRepository() }
    val upVoteColor by themeRepository.upVoteColor.collectAsState()
    val downVoteColor by themeRepository.downVoteColor.collectAsState()
    val replyColor by themeRepository.replyColor.collectAsState()
    val saveColor by themeRepository.saveColor.collectAsState()
    val defaultUpvoteColor = MaterialTheme.colorScheme.primary
    val defaultReplyColor = MaterialTheme.colorScheme.secondary
    val defaultSaveColor = MaterialTheme.colorScheme.secondaryContainer
    val defaultDownVoteColor = MaterialTheme.colorScheme.tertiary
    val navigationCoordinator = remember { getNavigationCoordinator() }
    var rawContent by remember { mutableStateOf<Any?>(null) }
    val settingsRepository = remember { getSettingsRepository() }
    val settings by settingsRepository.currentSettings.collectAsState()
    val mainRouter = remember { getMainRouter() }
    val clipboard = LocalClipboard.current
    val clipboardHelper = remember { getClipboardHelper(clipboard) }
    var shareBottomSheetUrls by remember { mutableStateOf<List<String>?>(null) }
    var sortBottomSheetOpened by remember { mutableStateOf(false) }
    var defaultSortBottomSheetOpened by remember { mutableStateOf(false) }
    var copyPostBottomSheet by remember { mutableStateOf<PostModel?>(null) }
    var manageUserTagsBottomSheetOpened by remember { mutableStateOf(false) }
    var addNewUserTagDialogOpen by remember { mutableStateOf(false) }
    var addNewUserTagTitleError by remember { mutableStateOf<ValidationError?>(null) }

    LaunchedEffect(model) {
        model.effects
            .onEach { effect ->
                when (effect) {
                    is UserDetailMviModel.Effect.Error -> {
                        snackbarHostState.showSnackbar(effect.message ?: genericError)
                    }

                    UserDetailMviModel.Effect.Success -> {
                        snackbarHostState.showSnackbar(successMessage)
                    }

                    UserDetailMviModel.Effect.BackToTop -> {
                        scope.launch {
                            runCatching {
                                lazyListState.scrollToItem(0)
                                topAppBarState.heightOffset = 0f
                                topAppBarState.contentOffset = 0f
                            }
                        }
                    }

                    is UserDetailMviModel.Effect.OpenDetail ->
                        mainRouter.openPostDetail(
                            post = PostModel(id = effect.postId),
                            highlightCommentId = effect.commentId,
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
            val userName = uiState.user.readableName(uiState.preferNicknames)
            TopAppBar(
                windowInsets = topAppBarState.toWindowInsets(),
                scrollBehavior = scrollBehavior,
                title = {
                    Text(
                        modifier = Modifier.padding(horizontal = Spacing.s),
                        text = userName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium,
                    )
                },
                actions = {
                    // sort button
                    IconButton(
                        onClick = {
                            sortBottomSheetOpened = true
                        },
                    ) {
                        val sortType =
                            when (uiState.section) {
                                UserDetailSection.Comments -> uiState.commentSortType
                                UserDetailSection.Posts -> uiState.postSortType
                            }
                        Icon(
                            imageVector = sortType.toIcon(),
                            contentDescription = sortType.toReadableName(),
                        )
                    }

                    // options menu
                    Box {
                        val options =
                            buildList {
                                this +=
                                    Option(
                                        OptionId.SetCustomSort,
                                        LocalStrings.current.communitySetCustomSort,
                                    )
                                this +=
                                    Option(
                                        OptionId.ExploreInstance,
                                        buildString {
                                            append(LocalStrings.current.navigationSearch)
                                            append(" ")
                                            append(uiState.user.host)
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
                                            LocalStrings.current.blockActionUser,
                                        )
                                    this +=
                                        Option(
                                            OptionId.BlockInstance,
                                            LocalStrings.current.communityDetailBlockInstance,
                                        )
                                }
                                if (uiState.isAdmin) {
                                    this +=
                                        Option(
                                            OptionId.Purge,
                                            LocalStrings.current.adminActionPurge,
                                        )
                                }
                                if (uiState.isLogged) {
                                    this +=
                                        Option(
                                            OptionId.ManageTags,
                                            LocalStrings.current.manageUserTagsTitle,
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
                                contentDescription = LocalStrings.current.actionOpenOptionMenu,
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
                                            OptionId.BlockInstance -> {
                                                model.reduce(UserDetailMviModel.Intent.BlockInstance)
                                            }

                                            OptionId.Block -> {
                                                model.reduce(UserDetailMviModel.Intent.Block)
                                            }

                                            OptionId.Share -> {
                                                val urls =
                                                    buildList {
                                                        if (uiState.user.host != uiState.instance) {
                                                            this +=
                                                                "https://${uiState.instance}/u/${uiState.user.readableHandle}"
                                                        }
                                                        this +=
                                                            "https://${uiState.user.host}/u/${uiState.user.name}"
                                                    }
                                                if (urls.size == 1) {
                                                    model.reduce(
                                                        UserDetailMviModel.Intent.Share(urls.first()),
                                                    )
                                                } else {
                                                    shareBottomSheetUrls = urls
                                                }
                                            }

                                            OptionId.ExploreInstance -> {
                                                mainRouter.openExplore(uiState.user.host)
                                            }

                                            OptionId.Purge -> {
                                                mainRouter.openModerateWithReason(
                                                    actionId = ModerateWithReasonAction.PurgeUser.toInt(),
                                                    contentId = uiState.user.id,
                                                )
                                            }

                                            OptionId.ManageTags -> {
                                                manageUserTagsBottomSheetOpened = true
                                            }

                                            OptionId.SetCustomSort -> {
                                                defaultSortBottomSheetOpened = true
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
                                navigationCoordinator.pop()
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
                        if (uiState.isLogged && !isOnOtherInstance) {
                            this +=
                                FloatingActionButtonMenuItem(
                                    icon = Icons.AutoMirrored.Default.Chat,
                                    text = LocalStrings.current.actionChat,
                                    onSelected = {
                                        mainRouter.openChat(userId)
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
        PullToRefreshBox(
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
                ).nestedScroll(fabNestedScrollConnection),
            isRefreshing = uiState.refreshing,
            onRefresh = {
                model.reduce(UserDetailMviModel.Intent.Refresh)
            },
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = lazyListState,
            ) {
                item {
                    UserHeader(
                        user = uiState.user,
                        autoLoadImages = uiState.autoLoadImages,
                        onOpenImage = { url ->
                            mainRouter.openImage(
                                url = url,
                                source = uiState.user.readableHandle,
                            )
                        },
                        onInfo = {
                            navigationCoordinator.openSideMenu {
                                UserInfoScreen(
                                    userId = uiState.user.id,
                                    username = uiState.user.name,
                                    otherInstance = otherInstanceName,
                                )
                            }
                        },
                    )
                }
                item {
                    SectionSelector(
                        modifier = Modifier.padding(bottom = Spacing.s),
                        titles =
                        listOf(
                            LocalStrings.current.profileSectionPosts,
                            LocalStrings.current.profileSectionComments,
                        ),
                        currentSection =
                        when (uiState.section) {
                            UserDetailSection.Comments -> 1
                            else -> 0
                        },
                        onSectionSelected = { idx ->
                            val section =
                                when (idx) {
                                    1 -> UserDetailSection.Comments
                                    else -> UserDetailSection.Posts
                                }
                            model.reduce(
                                UserDetailMviModel.Intent.ChangeSection(section),
                            )
                        },
                    )
                    Spacer(modifier = Modifier.height(Spacing.xs))
                }
                if (uiState.section == UserDetailSection.Posts) {
                    if (uiState.posts.isEmpty() && uiState.loading && uiState.initial) {
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
                                        backgroundColor =
                                        upVoteColor
                                            ?: defaultUpvoteColor,
                                        onTriggered = {
                                            model.reduce(
                                                UserDetailMviModel.Intent.UpVotePost(post.id),
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
                                                UserDetailMviModel.Intent.DownVotePost(
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
                                        backgroundColor =
                                        replyColor
                                            ?: defaultReplyColor,
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
                                        backgroundColor =
                                        saveColor
                                            ?: defaultSaveColor,
                                        onTriggered = {
                                            model.reduce(
                                                UserDetailMviModel.Intent.SavePost(id = post.id),
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
                                model.reduce(UserDetailMviModel.Intent.HapticIndication)
                            },
                            swipeToStartActions = uiState.actionsOnSwipeToStartPosts.toSwipeActions(),
                            swipeToEndActions =
                            if (uiState.isLogged && !isOnOtherInstance) {
                                uiState.actionsOnSwipeToEndPosts.toSwipeActions()
                            } else {
                                emptyList()
                            },
                            content = {
                                PostCard(
                                    post = post,
                                    hideAuthor = true,
                                    postLayout = uiState.postLayout,
                                    limitBodyHeight = true,
                                    fullHeightImage = uiState.fullHeightImages,
                                    fullWidthImage = uiState.fullWidthImages,
                                    blurNsfw = uiState.blurNsfw,
                                    voteFormat = uiState.voteFormat,
                                    autoLoadImages = uiState.autoLoadImages,
                                    preferNicknames = uiState.preferNicknames,
                                    showScores = uiState.showScores,
                                    actionButtonsActive = uiState.isLogged,
                                    downVoteEnabled = uiState.downVoteEnabled,
                                    onClick = {
                                        model.reduce(
                                            UserDetailMviModel.Intent.WillOpenDetail(
                                                postId = post.id,
                                            ),
                                        )
                                    },
                                    onDoubleClick =
                                    {
                                        model.reduce(
                                            UserDetailMviModel.Intent.UpVotePost(
                                                id = post.id,
                                                feedback = true,
                                            ),
                                        )
                                    }.takeIf { uiState.doubleTapActionEnabled },
                                    onUpVote =
                                    {
                                        model.reduce(
                                            UserDetailMviModel.Intent.UpVotePost(id = post.id),
                                        )
                                    }.takeIf { uiState.isLogged && !isOnOtherInstance },
                                    onDownVote =
                                    {
                                        model.reduce(
                                            UserDetailMviModel.Intent.DownVotePost(post.id),
                                        )
                                    }.takeIf { uiState.isLogged && !isOnOtherInstance },
                                    onSave =
                                    {
                                        model.reduce(
                                            UserDetailMviModel.Intent.SavePost(post.id),
                                        )
                                    }.takeIf { uiState.isLogged && !isOnOtherInstance },
                                    onOpenCommunity = { community, instance ->
                                        mainRouter.openCommunityDetail(
                                            community = community,
                                            otherInstance = instance,
                                        )
                                    },
                                    onOpenCreator = { user, instance ->
                                        mainRouter.openUserDetail(
                                            user = user,
                                            otherInstance = instance,
                                        )
                                    },
                                    onReply =
                                    {
                                        model.reduce(
                                            UserDetailMviModel.Intent.WillOpenDetail(
                                                postId = post.id,
                                            ),
                                        )
                                    }.takeIf { uiState.isLogged && !isOnOtherInstance },
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
                                                OptionId.Share,
                                                LocalStrings.current.postActionShare,
                                            )
                                        this +=
                                            Option(
                                                OptionId.Copy,
                                                LocalStrings.current.actionCopyClipboard,
                                            )
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
                                    },
                                    onSelectOption = { optionId ->
                                        when (optionId) {
                                            OptionId.Report -> {
                                                mainRouter.openModerateWithReason(
                                                    actionId = ModerateWithReasonAction.ReportPost.toInt(),
                                                    contentId = post.id,
                                                )
                                            }

                                            OptionId.CrossPost -> {
                                                mainRouter.openCreatePost(
                                                    crossPost = post,
                                                    forceCommunitySelection = true,
                                                )
                                            }

                                            OptionId.SeeRaw -> {
                                                rawContent = post
                                            }

                                            OptionId.Share -> {
                                                val urls =
                                                    listOfNotNull(
                                                        post.originalUrl,
                                                        "https://${uiState.instance}/post/${post.id}",
                                                    ).distinct()
                                                if (urls.size == 1) {
                                                    model.reduce(
                                                        UserDetailMviModel.Intent.Share(urls.first()),
                                                    )
                                                } else {
                                                    shareBottomSheetUrls = urls
                                                }
                                            }

                                            OptionId.Copy -> {
                                                val texts =
                                                    listOfNotNull(
                                                        post.title.takeIf { it.isNotBlank() },
                                                        post.text.takeIf { it.isNotBlank() },
                                                    ).distinct()
                                                if (texts.size == 1) {
                                                    scope.launch {
                                                        clipboardHelper.setText(texts.first())
                                                    }
                                                } else {
                                                    copyPostBottomSheet = post
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

                    if (uiState.posts.isEmpty() && !uiState.loading && !uiState.initial) {
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
                } else {
                    if (uiState.comments.isEmpty() && uiState.loading && uiState.initial) {
                        items(5) {
                            CommentCardPlaceholder(
                                modifier = Modifier.padding(horizontal = Spacing.xs),
                            )
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = Spacing.xxxs),
                                thickness = 0.25.dp,
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
                                        backgroundColor =
                                        upVoteColor
                                            ?: defaultUpvoteColor,
                                        onTriggered = {
                                            model.reduce(
                                                UserDetailMviModel.Intent.UpVoteComment(
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
                                                UserDetailMviModel.Intent.DownVoteComment(
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
                                        backgroundColor =
                                        replyColor
                                            ?: defaultReplyColor,
                                        onTriggered = {
                                            mainRouter.openReply(
                                                originalPost = PostModel(id = comment.postId),
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
                                        backgroundColor =
                                        saveColor
                                            ?: defaultSaveColor,
                                        onTriggered = {
                                            model.reduce(
                                                UserDetailMviModel.Intent.SaveComment(
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
                                model.reduce(UserDetailMviModel.Intent.HapticIndication)
                            },
                            swipeToStartActions = uiState.actionsOnSwipeToStartComments.toSwipeActions(),
                            swipeToEndActions =
                            if (uiState.isLogged && !isOnOtherInstance) {
                                uiState.actionsOnSwipeToEndComments.toSwipeActions()
                            } else {
                                emptyList()
                            },
                            content = {
                                CommentCard(
                                    modifier = Modifier.background(MaterialTheme.colorScheme.background),
                                    comment = comment,
                                    voteFormat = uiState.voteFormat,
                                    autoLoadImages = uiState.autoLoadImages,
                                    preferNicknames = uiState.preferNicknames,
                                    showScores = uiState.showScores,
                                    hideCommunity = false,
                                    hideAuthor = true,
                                    indentAmount = 0,
                                    downVoteEnabled = uiState.downVoteEnabled,
                                    actionButtonsActive = uiState.isLogged,
                                    onClick = {
                                        model.reduce(
                                            UserDetailMviModel.Intent.WillOpenDetail(
                                                postId = comment.postId,
                                                commentId = comment.id,
                                            ),
                                        )
                                    },
                                    onImageClick = { url ->
                                        mainRouter.openImage(
                                            url = url,
                                            source = comment.community?.readableHandle.orEmpty(),
                                        )
                                    },
                                    onDoubleClick =
                                    {
                                        model.reduce(
                                            UserDetailMviModel.Intent.UpVoteComment(
                                                id = comment.id,
                                                feedback = true,
                                            ),
                                        )
                                    }.takeIf { uiState.doubleTapActionEnabled },
                                    onSave =
                                    {
                                        model.reduce(
                                            UserDetailMviModel.Intent.SaveComment(comment.id),
                                        )
                                    }.takeIf { uiState.isLogged && !isOnOtherInstance },
                                    onUpVote =
                                    {
                                        model.reduce(
                                            UserDetailMviModel.Intent.UpVoteComment(
                                                comment.id,
                                            ),
                                        )
                                    }.takeIf { uiState.isLogged && !isOnOtherInstance },
                                    onDownVote =
                                    {
                                        model.reduce(
                                            UserDetailMviModel.Intent.DownVoteComment(
                                                comment.id,
                                            ),
                                        )
                                    }.takeIf { uiState.isLogged && !isOnOtherInstance },
                                    onReply =
                                    {
                                        mainRouter.openReply(
                                            originalPost = PostModel(id = comment.postId),
                                            originalComment = comment,
                                        )
                                    }.takeIf { uiState.isLogged && !isOnOtherInstance },
                                    onOpenCommunity = { community, instance ->
                                        mainRouter.openCommunityDetail(
                                            community = community,
                                            otherInstance = instance,
                                        )
                                    },
                                    onOpenCreator = { user, instance ->
                                        mainRouter.openUserDetail(
                                            user = user,
                                            otherInstance = instance,
                                        )
                                    },
                                    options =
                                    buildList {
                                        Option(
                                            OptionId.Share,
                                            LocalStrings.current.postActionShare,
                                        )
                                        this +=
                                            Option(
                                                OptionId.SeeRaw,
                                                LocalStrings.current.postActionSeeRaw,
                                            )
                                        if (uiState.isLogged && !isOnOtherInstance) {
                                            this +=
                                                Option(
                                                    OptionId.Report,
                                                    LocalStrings.current.postActionReport,
                                                )
                                        }
                                    },
                                    onSelectOption = { optionId ->
                                        when (optionId) {
                                            OptionId.Report -> {
                                                mainRouter.openModerateWithReason(
                                                    actionId = ModerateWithReasonAction.ReportComment.toInt(),
                                                    contentId = comment.id,
                                                )
                                            }

                                            OptionId.SeeRaw -> {
                                                rawContent = comment
                                            }

                                            OptionId.Share -> {
                                                val urls =
                                                    listOfNotNull(
                                                        comment.originalUrl,
                                                        "https://${uiState.instance}/comment/${comment.id}",
                                                    ).distinct()
                                                if (urls.size == 1) {
                                                    model.reduce(
                                                        UserDetailMviModel.Intent.Share(
                                                            urls.first(),
                                                        ),
                                                    )
                                                } else {
                                                    shareBottomSheetUrls = urls
                                                }
                                            }

                                            else -> Unit
                                        }
                                    },
                                )
                            },
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = Spacing.xxxs),
                            thickness = 0.25.dp,
                        )
                    }

                    if (uiState.comments.isEmpty() && !uiState.loading && !uiState.initial) {
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
                }
                item {
                    if (!uiState.initial && !uiState.loading && !uiState.refreshing && uiState.canFetchMore) {
                        if (settings.infiniteScrollEnabled) {
                            model.reduce(UserDetailMviModel.Intent.LoadNextPage)
                        } else {
                            Row(
                                modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(top = Spacing.s),
                                horizontalArrangement = Arrangement.Center,
                            ) {
                                Button(
                                    onClick = {
                                        model.reduce(UserDetailMviModel.Intent.LoadNextPage)
                                    },
                                ) {
                                    Text(
                                        text =
                                        if (uiState.section == UserDetailSection.Posts) {
                                            LocalStrings.current.postListLoadMorePosts
                                        } else {
                                            LocalStrings.current.postDetailLoadMoreComments
                                        },
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
                            mainRouter.openReply(
                                originalPost = PostModel(id = content.id),
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

    shareBottomSheetUrls?.also { values ->
        CustomModalBottomSheet(
            title = LocalStrings.current.postActionShare,
            items =
            values.map { value ->
                CustomModalBottomSheetItem(label = value)
            },
            onSelect = { index ->
                shareBottomSheetUrls = null
                if (index != null) {
                    model.reduce(UserDetailMviModel.Intent.Share(url = values[index]))
                }
            },
        )
    }

    if (sortBottomSheetOpened || defaultSortBottomSheetOpened) {
        SortBottomSheet(
            values = uiState.availableSortTypes,
            expandTop = uiState.section == UserDetailSection.Posts,
            onSelect = { value ->
                val wasDefaultSortBottomSheetOpened = defaultSortBottomSheetOpened
                sortBottomSheetOpened = false
                defaultSortBottomSheetOpened = false
                if (value != null) {
                    model.reduce(
                        UserDetailMviModel.Intent.ChangeSortType(
                            value = value,
                            saveAsDefault = wasDefaultSortBottomSheetOpened,
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
            onSelect = { index ->
                copyPostBottomSheet = null
                if (index != null) {
                    scope.launch {
                        clipboardHelper.setText(texts[index])
                    }
                }
            },
        )
    }

    if (manageUserTagsBottomSheetOpened) {
        AssignUserTagBottomSheet(
            tags = uiState.availableUserTags,
            initiallyCheckedIds = uiState.currentUserTagIds,
            onDismiss = {
                manageUserTagsBottomSheetOpened = false
            },
            onSelect = { ids ->
                manageUserTagsBottomSheetOpened = false
                model.reduce(UserDetailMviModel.Intent.UpdateTags(ids))
            },
            onAddNewTag = {
                addNewUserTagDialogOpen = true
            },
        )
    }

    if (addNewUserTagDialogOpen) {
        val forbiddenTagNames =
            buildList {
                addAll(
                    listOf(
                        LocalStrings.current.defaultTagAdmin,
                        LocalStrings.current.defaultTagBot,
                        LocalStrings.current.defaultTagCurrentUser,
                        LocalStrings.current.defaultTagModerator,
                        LocalStrings.current.defaultTagOriginalPoster,
                    ).map { it.lowercase() },
                )
                addAll(
                    uiState.availableUserTags.map { it.name.lowercase() },
                )
            }
        EditUserTagDialog(
            title = LocalStrings.current.buttonAdd,
            titleError = addNewUserTagTitleError,
            value = "",
            onClose = { name, color ->
                addNewUserTagTitleError =
                    if (name?.lowercase() in forbiddenTagNames) {
                        ValidationError.InvalidField
                    } else {
                        null
                    }
                if (addNewUserTagTitleError == null) {
                    addNewUserTagDialogOpen = false
                    if (name != null) {
                        model.reduce(
                            UserDetailMviModel.Intent.AddUserTag(
                                name = name,
                                color = color?.toArgb(),
                            ),
                        )
                    }
                }
            },
        )
    }
}
