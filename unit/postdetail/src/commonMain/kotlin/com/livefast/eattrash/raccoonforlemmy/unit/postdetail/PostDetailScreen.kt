package com.livefast.eattrash.raccoonforlemmy.unit.postdetail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.NavigateBefore
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.PostLayout
import com.livefast.eattrash.raccoonforlemmy.core.appearance.di.getThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.toWindowInsets
import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.getViewModel
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.CustomDropDown
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.FloatingActionButtonMenu
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.FloatingActionButtonMenuItem
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.SearchField
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.SwipeAction
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.SwipeActionCard
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.CollapsedCommentCard
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.CommentCard
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.CommentCardPlaceholder
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.Option
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.OptionId
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.PostCard
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.di.getFabNestedScrollConnection
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.CustomModalBottomSheet
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.CustomModalBottomSheetItem
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.SortBottomSheet
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getMainRouter
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.ActionOnSwipe
import com.livefast.eattrash.raccoonforlemmy.core.persistence.di.getSettingsRepository
import com.livefast.eattrash.raccoonforlemmy.core.utils.VoteAction
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.onClick
import com.livefast.eattrash.raccoonforlemmy.core.utils.datetime.toTimestamp
import com.livefast.eattrash.raccoonforlemmy.core.utils.toIcon
import com.livefast.eattrash.raccoonforlemmy.core.utils.toLocalDp
import com.livefast.eattrash.raccoonforlemmy.core.utils.toModifier
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommentModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.containsId
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.readableHandle
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.readableName
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toIcon
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toReadableName
import com.livefast.eattrash.raccoonforlemmy.unit.moderatewithreason.ModerateWithReasonAction
import com.livefast.eattrash.raccoonforlemmy.unit.moderatewithreason.toInt
import com.livefast.eattrash.raccoonforlemmy.unit.postdetail.di.PostDetailMviModelParams
import com.livefast.eattrash.raccoonforlemmy.unit.rawcontent.RawContentDialog
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(
    postId: Long,
    modifier: Modifier = Modifier,
    otherInstance: String = "",
    highlightCommentId: Long? = null,
    isMod: Boolean = false,
) {
    val model: PostDetailMviModel =
        getViewModel<PostDetailViewModel>(
            PostDetailMviModelParams(
                postId = postId,
                otherInstance = otherInstance,
                highlightCommentId = highlightCommentId ?: 0L,
                isModerator = isMod,
            ),
        )
    val uiState by model.uiState.collectAsState()
    val isOnOtherInstance = remember { otherInstance.isNotEmpty() }
    val otherInstanceName = remember { otherInstance }
    val commentIdToHighlight = remember { highlightCommentId }
    val navigationCoordinator = remember { getNavigationCoordinator() }
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
    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var rawContent by remember { mutableStateOf<Any?>(null) }
    val settingsRepository = remember { getSettingsRepository() }
    val settings by settingsRepository.currentSettings.collectAsState()
    val mainRouter = remember { getMainRouter() }
    val clipboardManager = LocalClipboardManager.current
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
    var postToDelete by remember { mutableStateOf<Unit?>(null) }
    var commentIdToDelete by remember { mutableStateOf<Long?>(null) }
    val bottomNavigationInsetPx =
        with(LocalDensity.current) {
            WindowInsets.navigationBars.getBottom(this)
        }
    var bottomBarHeightPx by remember { mutableFloatStateOf(0f) }
    var bottomBarOffsetHeightPx by remember { mutableFloatStateOf(0f) }
    val bottomNavigationInset =
        with(LocalDensity.current) {
            bottomNavigationInsetPx.toDp()
        }
    val buttonBarScrollConnection =
        remember {
            object : NestedScrollConnection {
                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                    val delta = available.y
                    val newOffset =
                        (bottomBarOffsetHeightPx + delta).coerceIn(
                            -(bottomBarHeightPx + bottomNavigationInsetPx),
                            0f,
                        )
                    bottomBarOffsetHeightPx = newOffset
                    return Offset.Zero
                }
            }
        }
    var shareBottomSheetUrls by remember { mutableStateOf<List<String>?>(null) }
    var sortBottomSheetOpened by remember { mutableStateOf(false) }
    var copyPostBottomSheet by remember { mutableStateOf<PostModel?>(null) }

    LaunchedEffect(model) {
        model.effects
            .onEach { effect ->
                when (effect) {
                    PostDetailMviModel.Effect.Close -> {
                        navigationCoordinator.pop()
                    }

                    is PostDetailMviModel.Effect.ScrollToComment -> {
                        runCatching {
                            lazyListState.scrollToItem(effect.index)
                        }
                    }

                    PostDetailMviModel.Effect.BackToTop -> {
                        runCatching {
                            lazyListState.scrollToItem(0)
                            topAppBarState.heightOffset = 0f
                            topAppBarState.contentOffset = 0f
                        }
                    }
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
                title = {
                    Text(
                        modifier = Modifier.padding(horizontal = Spacing.s),
                        text = uiState.post.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium,
                    )
                },
                actions = {
                    uiState.sortType?.also { sortType ->
                        IconButton(
                            onClick = {
                                sortBottomSheetOpened = true
                            },
                        ) {
                            Icon(
                                imageVector = sortType.toIcon(),
                                contentDescription = sortType.toReadableName(),
                            )
                        }
                    }

                    // options menu
                    Box {
                        val options =
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
                                if (uiState.post.creator?.id == uiState.currentUserId && !isOnOtherInstance) {
                                    this +=
                                        Option(
                                            OptionId.Edit,
                                            LocalStrings.current.postActionEdit,
                                        )
                                    if (uiState.post.deleted) {
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
                                if (uiState.isModerator) {
                                    this +=
                                        Option(
                                            OptionId.FeaturePost,
                                            if (uiState.post.featuredCommunity) {
                                                LocalStrings.current.modActionUnmarkAsFeatured
                                            } else {
                                                LocalStrings.current.modActionMarkAsFeatured
                                            },
                                        )
                                    this +=
                                        Option(
                                            OptionId.LockPost,
                                            if (uiState.post.locked) {
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
                                            if (uiState.post.creator?.banned == true) {
                                                LocalStrings.current.modActionAllow
                                            } else {
                                                LocalStrings.current.modActionBan
                                            },
                                        )
                                    uiState.post.creator?.id?.also { creatorId ->
                                        if (uiState.currentUserId != creatorId) {
                                            this +=
                                                Option(
                                                    OptionId.AddMod,
                                                    if (uiState.moderators.containsId(creatorId)) {
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
                                    uiState.post.creator?.also { creator ->
                                        this +=
                                            Option(
                                                OptionId.PurgeCreator,
                                                buildString {
                                                    append(LocalStrings.current.adminActionPurge)
                                                    append(" ")
                                                    append(creator.readableName(uiState.preferNicknames))
                                                },
                                            )
                                    }
                                    this +=
                                        Option(
                                            OptionId.AdminFeaturePost,
                                            if (uiState.post.featuredLocal) {
                                                LocalStrings.current.adminActionUnmarkAsFeatured
                                            } else {
                                                LocalStrings.current.adminActionMarkAsFeatured
                                            },
                                        )
                                }
                                this +=
                                    Option(
                                        OptionId.Search,
                                        if (uiState.searching) {
                                            LocalStrings.current.actionExitSearch
                                        } else {
                                            buildString {
                                                append(LocalStrings.current.actionSearchInComments)
                                            }
                                        },
                                    )
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
                                            OptionId.Delete -> {
                                                postToDelete = Unit
                                            }

                                            OptionId.Edit -> {
                                                mainRouter.openCreatePost(editedPost = uiState.post)
                                            }

                                            OptionId.Report -> {
                                                mainRouter.openModerateWithReason(
                                                    actionId = ModerateWithReasonAction.ReportPost.toInt(),
                                                    contentId = uiState.post.id,
                                                )
                                            }

                                            OptionId.CrossPost -> {
                                                mainRouter.openCreatePost(
                                                    crossPost = uiState.post,
                                                    forceCommunitySelection = true,
                                                )
                                            }

                                            OptionId.SeeRaw -> {
                                                rawContent = uiState.post
                                            }

                                            OptionId.Share -> {
                                                val urls =
                                                    listOfNotNull(
                                                        uiState.post.originalUrl,
                                                        "https://${uiState.instance}/post/${uiState.post.id}",
                                                    ).distinct()
                                                if (urls.size == 1) {
                                                    model.reduce(PostDetailMviModel.Intent.Share(urls.first()))
                                                } else {
                                                    shareBottomSheetUrls = urls
                                                }
                                            }

                                            OptionId.FeaturePost ->
                                                model.reduce(PostDetailMviModel.Intent.ModFeaturePost)

                                            OptionId.AdminFeaturePost ->
                                                model.reduce(PostDetailMviModel.Intent.AdminFeaturePost)

                                            OptionId.LockPost ->
                                                model.reduce(PostDetailMviModel.Intent.ModLockPost)

                                            OptionId.Remove -> {
                                                mainRouter.openModerateWithReason(
                                                    actionId = ModerateWithReasonAction.RemovePost.toInt(),
                                                    contentId = uiState.post.id,
                                                )
                                            }

                                            OptionId.BanUser -> {
                                                uiState.post.creator?.id?.also { userId ->
                                                    mainRouter.openBanUser(
                                                        userId = userId,
                                                        communityId = uiState.post.community?.id ?: 0,
                                                        newValue = uiState.post.creator?.banned != true,
                                                        postId = uiState.post.id,
                                                    )
                                                }
                                            }

                                            OptionId.AddMod -> {
                                                uiState.post.creator?.id?.also { userId ->
                                                    model.reduce(PostDetailMviModel.Intent.ModToggleModUser(userId))
                                                }
                                            }

                                            OptionId.Copy -> {
                                                val texts =
                                                    listOfNotNull(
                                                        uiState.post.title.takeIf { it.isNotBlank() },
                                                        uiState.post.text.takeIf { it.isNotBlank() },
                                                    ).distinct()
                                                if (texts.size == 1) {
                                                    clipboardManager.setText(AnnotatedString(texts.first()))
                                                } else {
                                                    copyPostBottomSheet = uiState.post
                                                }
                                            }

                                            OptionId.Search -> {
                                                model.reduce(
                                                    PostDetailMviModel.Intent.ChangeSearching(
                                                        !uiState.searching,
                                                    ),
                                                )
                                            }

                                            OptionId.Purge -> {
                                                mainRouter.openModerateWithReason(
                                                    actionId = ModerateWithReasonAction.PurgePost.toInt(),
                                                    contentId = uiState.post.id,
                                                )
                                            }

                                            OptionId.PurgeCreator -> {
                                                uiState.post.creator?.id?.also { userId ->
                                                    mainRouter.openModerateWithReason(
                                                        actionId = ModerateWithReasonAction.PurgeUser.toInt(),
                                                        contentId = userId,
                                                    )
                                                }
                                            }

                                            OptionId.Restore -> {
                                                model.reduce(PostDetailMviModel.Intent.RestorePost)
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
                    modifier =
                    Modifier.then(
                        if (uiState.enableButtonsToScrollBetweenComments) {
                            Modifier.padding(
                                bottom = Spacing.s + bottomNavigationInset,
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
                        if (uiState.isLogged && !isOnOtherInstance) {
                            this +=
                                FloatingActionButtonMenuItem(
                                    icon = Icons.AutoMirrored.Default.Reply,
                                    text = LocalStrings.current.actionReply,
                                    onSelected = {
                                        mainRouter.openReply(
                                            originalPost = uiState.post,
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
        if (uiState.isLogged && uiState.currentUserId == null) {
            return@Scaffold
        }
        Box(
            modifier =
            Modifier
                .padding(
                    top = padding.calculateTopPadding(),
                ),
        ) {
            Column(
                modifier =
                Modifier
                    .then(
                        if (uiState.enableButtonsToScrollBetweenComments) {
                            Modifier.nestedScroll(buttonBarScrollConnection)
                        } else {
                            Modifier
                        },
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
                            model.reduce(PostDetailMviModel.Intent.SetSearch(value))
                        },
                        onClear = {
                            model.reduce(PostDetailMviModel.Intent.SetSearch(""))
                        },
                    )
                }

                PullToRefreshBox(
                    modifier =
                    Modifier
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
                        model.reduce(PostDetailMviModel.Intent.Refresh)
                    },
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = lazyListState,
                    ) {
                        item {
                            if (uiState.post.title.isNotEmpty()) {
                                PostCard(
                                    post = uiState.post,
                                    isFromModerator =
                                    uiState.post.creator?.id.let { creatorId ->
                                        uiState.isModerator &&
                                            uiState.moderators.containsId(
                                                creatorId,
                                            )
                                    },
                                    postLayout =
                                    if (uiState.postLayout == PostLayout.Card) {
                                        uiState.postLayout
                                    } else {
                                        PostLayout.Full
                                    },
                                    fullHeightImage = uiState.fullHeightImages,
                                    fullWidthImage = uiState.fullWidthImages,
                                    includeFullBody = true,
                                    showBot = true,
                                    isCurrentUser = uiState.post.creator?.id == uiState.currentUserId,
                                    voteFormat = uiState.voteFormat,
                                    autoLoadImages = uiState.autoLoadImages,
                                    preferNicknames = uiState.preferNicknames,
                                    showScores = uiState.showScores,
                                    actionButtonsActive = uiState.isLogged,
                                    blurNsfw = false,
                                    downVoteEnabled = uiState.downVoteEnabled,
                                    highlightText = uiState.searchText,
                                    botTagColor = uiState.botTagColor,
                                    meTagColor = uiState.meTagColor,
                                    onOpenCommunity = { community, instance ->
                                        mainRouter.openCommunityDetail(community, instance)
                                    },
                                    onOpenCreator = { user, instance ->
                                        mainRouter.openUserDetail(user, instance)
                                    },
                                    onUpVote = {
                                        if (uiState.isLogged && !isOnOtherInstance) {
                                            model.reduce(
                                                PostDetailMviModel.Intent.UpVotePost(),
                                            )
                                        }
                                    },
                                    onDownVote = {
                                        if (uiState.isLogged && !isOnOtherInstance) {
                                            model.reduce(
                                                PostDetailMviModel.Intent.DownVotePost(),
                                            )
                                        }
                                    },
                                    onSave = {
                                        model.reduce(
                                            PostDetailMviModel.Intent.SavePost(
                                                post = uiState.post,
                                            ),
                                        )
                                    },
                                    onReply = {
                                        if (uiState.isLogged && !isOnOtherInstance) {
                                            mainRouter.openReply(
                                                originalPost = uiState.post,
                                            )
                                        }
                                    },
                                    onOpenImage = { url ->
                                        mainRouter.openImage(
                                            url = url,
                                            source =
                                            uiState.post.community
                                                ?.readableHandle
                                                .orEmpty(),
                                        )
                                    },
                                    onOpenVideo = { url ->
                                        mainRouter.openImage(
                                            url = url,
                                            isVideo = true,
                                            source =
                                            uiState.post.community
                                                ?.readableHandle
                                                .orEmpty(),
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
                        if (uiState.post.crossPosts.isNotEmpty()) {
                            item {
                                FlowRow(
                                    modifier =
                                    Modifier.padding(
                                        top = Spacing.xxs,
                                        bottom = Spacing.s,
                                        start = Spacing.s,
                                        end = Spacing.s,
                                    ),
                                    horizontalArrangement = Arrangement.spacedBy(Spacing.xxs),
                                ) {
                                    Text(
                                        text = LocalStrings.current.postDetailCrossPosts,
                                        style = MaterialTheme.typography.bodyMedium,
                                    )
                                    uiState.post.crossPosts.forEachIndexed { index, crossPost ->
                                        val community = crossPost.community
                                        if (community != null) {
                                            val string =
                                                buildAnnotatedString {
                                                    withStyle(
                                                        SpanStyle(textDecoration = TextDecoration.Underline),
                                                    ) {
                                                        append(community.readableName(uiState.preferNicknames))
                                                    }
                                                    if (index < uiState.post.crossPosts.lastIndex) {
                                                        append(", ")
                                                    }
                                                }
                                            Text(
                                                modifier =
                                                Modifier.onClick(
                                                    onClick = {
                                                        val post =
                                                            PostModel(
                                                                id = crossPost.id,
                                                                community = community,
                                                            )
                                                        mainRouter.openPostDetail(
                                                            post = post,
                                                            otherInstance = otherInstanceName,
                                                        )
                                                    },
                                                ),
                                                text = string,
                                                style = MaterialTheme.typography.bodyMedium,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        if (uiState.comments.isEmpty() && uiState.loading && uiState.initial) {
                            items(5) {
                                CommentCardPlaceholder()
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = Spacing.xxxs),
                                    thickness = 0.25.dp,
                                )
                            }
                        }
                        items(
                            items = uiState.comments.filter { it.visible },
                            key = { c -> c.id.toString() + (c.updateDate ?: c.publishDate) },
                        ) { comment ->
                            Column {
                                AnimatedContent(
                                    targetState = comment.expanded,
                                    transitionSpec = {
                                        fadeIn(animationSpec = tween(250)).togetherWith(fadeOut())
                                    },
                                ) {
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
                                                                contentDescription =
                                                                LocalStrings.current.actionUpvote,
                                                                tint = Color.White,
                                                            )
                                                        },
                                                        backgroundColor =
                                                        upVoteColor
                                                            ?: defaultUpvoteColor,
                                                        onTriggered = {
                                                            model.reduce(
                                                                PostDetailMviModel.Intent.UpVoteComment(
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
                                                                contentDescription =
                                                                LocalStrings.current.actionDownvote,
                                                                tint = Color.White,
                                                            )
                                                        },
                                                        backgroundColor =
                                                        downVoteColor
                                                            ?: defaultDownVoteColor,
                                                        onTriggered = {
                                                            model.reduce(
                                                                PostDetailMviModel.Intent.DownVoteComment(
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
                                                                contentDescription =
                                                                LocalStrings.current.actionReply,
                                                                tint = Color.White,
                                                            )
                                                        },
                                                        backgroundColor =
                                                        replyColor
                                                            ?: defaultReplyColor,
                                                        onTriggered = {
                                                            mainRouter.openReply(
                                                                originalPost = uiState.post,
                                                                originalComment = comment,
                                                            )
                                                        },
                                                    )

                                                ActionOnSwipe.Save ->
                                                    SwipeAction(
                                                        swipeContent = {
                                                            Icon(
                                                                imageVector = Icons.Default.Bookmark,
                                                                contentDescription =
                                                                LocalStrings.current.actionAddToBookmarks,
                                                                tint = Color.White,
                                                            )
                                                        },
                                                        backgroundColor =
                                                        saveColor
                                                            ?: defaultSaveColor,
                                                        onTriggered = {
                                                            model.reduce(
                                                                PostDetailMviModel.Intent.SaveComment(
                                                                    commentId = comment.id,
                                                                ),
                                                            )
                                                        },
                                                    )

                                                ActionOnSwipe.Edit ->
                                                    SwipeAction(
                                                        swipeContent = {
                                                            Icon(
                                                                imageVector = Icons.Default.Edit,
                                                                contentDescription =
                                                                LocalStrings.current.postActionEdit,
                                                                tint = Color.White,
                                                            )
                                                        },
                                                        backgroundColor = MaterialTheme.colorScheme.tertiary,
                                                        onTriggered = {
                                                            mainRouter.openReply(
                                                                originalPost = PostModel(id = comment.postId),
                                                                originalComment =
                                                                comment.parentId?.let { parentId ->
                                                                    CommentModel(id = parentId)
                                                                },
                                                                editedComment = comment,
                                                            )
                                                        },
                                                    ).takeIf { canEdit }

                                                else -> null
                                            }
                                        }

                                    if (comment.expanded) {
                                        SwipeActionCard(
                                            modifier = Modifier.fillMaxWidth(),
                                            enabled = uiState.swipeActionsEnabled,
                                            onGestureBegin = {
                                                model.reduce(PostDetailMviModel.Intent.HapticIndication)
                                            },
                                            swipeToStartActions =
                                            uiState.actionsOnSwipeToStartComments.toSwipeActions(
                                                canEdit = comment.creator?.id == uiState.currentUserId,
                                            ),
                                            swipeToEndActions =
                                            if (uiState.isLogged && !isOnOtherInstance) {
                                                uiState.actionsOnSwipeToEndComments.toSwipeActions(
                                                    canEdit = comment.creator?.id == uiState.currentUserId,
                                                )
                                            } else {
                                                emptyList()
                                            },
                                            content = {
                                                val commentTs =
                                                    with(comment) {
                                                        updateDate ?: publishDate
                                                    }?.toTimestamp()
                                                val lastSeenTs = uiState.lastSeenTimestamp
                                                val isAfterLastSeenTs = commentTs != null &&
                                                    lastSeenTs != null &&
                                                    commentTs > lastSeenTs
                                                val backgroundModifier =
                                                    when {
                                                        commentIdToHighlight == comment.id ||
                                                            (commentIdToHighlight == null && isAfterLastSeenTs)
                                                        ->
                                                            Modifier.background(
                                                                MaterialTheme.colorScheme
                                                                    .surfaceColorAtElevation(
                                                                        5.dp,
                                                                    ).copy(alpha = 0.75f),
                                                            )

                                                        else -> Modifier
                                                    }
                                                CommentCard(
                                                    modifier =
                                                    Modifier
                                                        .background(MaterialTheme.colorScheme.background)
                                                        .then(backgroundModifier),
                                                    comment = comment,
                                                    isOp = comment.creator?.id == uiState.post.creator?.id,
                                                    showBot = true,
                                                    isCurrentUser = comment.creator?.id == uiState.currentUserId,
                                                    isMod =
                                                    comment.creator?.id.let { id ->
                                                        uiState.moderators.containsId(id)
                                                    },
                                                    isAdmin =
                                                    comment.creator?.let { user ->
                                                        // comparison must be done by handle because
                                                        // IDs vary from one instance to another
                                                        uiState.admins.any { admin ->
                                                            admin.readableHandle == user.readableHandle
                                                        }
                                                    } ?: false,
                                                    indentAmount = uiState.commentIndentAmount,
                                                    barThickness = uiState.commentBarThickness,
                                                    voteFormat = uiState.voteFormat,
                                                    autoLoadImages = uiState.autoLoadImages,
                                                    preferNicknames = uiState.preferNicknames,
                                                    showScores = uiState.showScores,
                                                    actionButtonsActive = uiState.isLogged,
                                                    downVoteEnabled = uiState.downVoteEnabled,
                                                    highlightText = uiState.searchText,
                                                    adminTagColor = uiState.adminTagColor,
                                                    botTagColor = uiState.botTagColor,
                                                    meTagColor = uiState.meTagColor,
                                                    modTagColor = uiState.modTagColor,
                                                    opTagColor = uiState.opTagColor,
                                                    onToggleExpand = {
                                                        model.reduce(
                                                            PostDetailMviModel.Intent.ToggleExpandComment(
                                                                comment.id,
                                                            ),
                                                        )
                                                    },
                                                    onDoubleClick =
                                                    {
                                                        model.reduce(
                                                            PostDetailMviModel.Intent.UpVoteComment(
                                                                commentId = comment.id,
                                                                feedback = true,
                                                            ),
                                                        )
                                                    }.takeIf { uiState.doubleTapActionEnabled },
                                                    onUpVote =
                                                    {
                                                        model.reduce(
                                                            PostDetailMviModel.Intent.UpVoteComment(
                                                                commentId = comment.id,
                                                            ),
                                                        )
                                                    }.takeIf { uiState.isLogged && !isOnOtherInstance },
                                                    onDownVote =
                                                    {
                                                        model.reduce(
                                                            PostDetailMviModel.Intent.DownVoteComment(
                                                                commentId = comment.id,
                                                            ),
                                                        )
                                                    }.takeIf { uiState.isLogged && !isOnOtherInstance },
                                                    onSave =
                                                    {
                                                        model.reduce(
                                                            PostDetailMviModel.Intent.SaveComment(
                                                                commentId = comment.id,
                                                            ),
                                                        )
                                                    }.takeIf { uiState.isLogged && !isOnOtherInstance },
                                                    onReply = {
                                                        if (uiState.isLogged && !isOnOtherInstance) {
                                                            mainRouter.openReply(
                                                                originalPost = uiState.post,
                                                                originalComment = comment,
                                                            )
                                                        }
                                                    },
                                                    onOpenCreator = { user, instance ->
                                                        mainRouter.openUserDetail(
                                                            user,
                                                            instance,
                                                        )
                                                    },
                                                    onOpenCommunity = { community, instance ->
                                                        mainRouter.openCommunityDetail(
                                                            community,
                                                            instance,
                                                        )
                                                    },
                                                    onImageClick = { url ->
                                                        mainRouter.openImage(
                                                            url = url,
                                                            source =
                                                            uiState.post.community
                                                                ?.readableHandle
                                                                .orEmpty(),
                                                        )
                                                    },
                                                    options = buildList {
                                                        this +=
                                                            Option(
                                                                OptionId.Share,
                                                                LocalStrings.current.postActionShare,
                                                            )
                                                        this +=
                                                            Option(
                                                                OptionId.SeeRaw,
                                                                LocalStrings.current.postActionSeeRaw,
                                                            )
                                                        this +=
                                                            Option(
                                                                OptionId.Report,
                                                                LocalStrings.current.postActionReport,
                                                            )
                                                        if (comment.creator?.id == uiState.currentUserId) {
                                                            this +=
                                                                Option(
                                                                    OptionId.Edit,
                                                                    LocalStrings.current.postActionEdit,
                                                                )
                                                            if (comment.deleted) {
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
                                                        if (uiState.isModerator) {
                                                            this +=
                                                                Option(
                                                                    OptionId.DistinguishComment,
                                                                    if (comment.distinguished) {
                                                                        LocalStrings
                                                                            .current
                                                                            .modActionUnmarkAsDistinguished
                                                                    } else {
                                                                        LocalStrings
                                                                            .current
                                                                            .modActionMarkAsDistinguished
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
                                                                    if (comment.creator?.banned == true) {
                                                                        LocalStrings.current.modActionAllow
                                                                    } else {
                                                                        LocalStrings.current.modActionBan
                                                                    },
                                                                )
                                                            comment.creator?.id?.also { creatorId ->
                                                                if (uiState.currentUserId != creatorId) {
                                                                    this +=
                                                                        Option(
                                                                            OptionId.AddMod,
                                                                            if (uiState.moderators.containsId(
                                                                                    creatorId,
                                                                                )
                                                                            ) {
                                                                                LocalStrings
                                                                                    .current
                                                                                    .modActionRemoveMod
                                                                            } else {
                                                                                LocalStrings
                                                                                    .current
                                                                                    .modActionAddMod
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
                                                            comment.creator?.also { creator ->
                                                                this +=
                                                                    Option(
                                                                        OptionId.PurgeCreator,
                                                                        buildString {
                                                                            append(
                                                                                LocalStrings
                                                                                    .current
                                                                                    .adminActionPurge,
                                                                            )
                                                                            append(" ")
                                                                            append(
                                                                                creator.readableName(
                                                                                    uiState.preferNicknames,
                                                                                ),
                                                                            )
                                                                        },
                                                                    )
                                                            }
                                                        }
                                                    },
                                                    onSelectOption = { optionId ->
                                                        when (optionId) {
                                                            OptionId.Delete -> {
                                                                commentIdToDelete = comment.id
                                                            }

                                                            OptionId.Edit -> {
                                                                mainRouter.openReply(
                                                                    originalPost = PostModel(id = comment.postId),
                                                                    originalComment =
                                                                    comment.parentId?.let {
                                                                        CommentModel(id = it)
                                                                    },
                                                                    editedComment = comment,
                                                                )
                                                            }

                                                            OptionId.Report -> {
                                                                mainRouter.openModerateWithReason(
                                                                    actionId =
                                                                    ModerateWithReasonAction
                                                                        .ReportComment
                                                                        .toInt(),
                                                                    contentId = comment.id,
                                                                )
                                                            }

                                                            OptionId.SeeRaw -> {
                                                                rawContent = comment
                                                            }

                                                            OptionId.DistinguishComment ->
                                                                model.reduce(
                                                                    PostDetailMviModel.Intent.ModDistinguishComment(
                                                                        comment.id,
                                                                    ),
                                                                )

                                                            OptionId.Remove -> {
                                                                mainRouter.openModerateWithReason(
                                                                    actionId =
                                                                    ModerateWithReasonAction
                                                                        .RemoveComment
                                                                        .toInt(),
                                                                    contentId = comment.id,
                                                                )
                                                            }

                                                            OptionId.BanUser -> {
                                                                comment.creator?.id?.also { userId ->
                                                                    mainRouter.openBanUser(
                                                                        userId = userId,
                                                                        communityId = uiState.post.community?.id ?: 0,
                                                                        newValue = comment.creator?.banned != true,
                                                                        commentId = comment.id,
                                                                    )
                                                                }
                                                            }

                                                            OptionId.AddMod -> {
                                                                comment.creator?.id?.also { userId ->
                                                                    model.reduce(
                                                                        PostDetailMviModel.Intent.ModToggleModUser(
                                                                            userId,
                                                                        ),
                                                                    )
                                                                }
                                                            }

                                                            OptionId.Purge -> {
                                                                mainRouter.openModerateWithReason(
                                                                    actionId =
                                                                    ModerateWithReasonAction.PurgeComment.toInt(),
                                                                    contentId = comment.id,
                                                                )
                                                            }

                                                            OptionId.PurgeCreator -> {
                                                                comment.creator?.id?.also { userId ->
                                                                    mainRouter.openModerateWithReason(
                                                                        actionId =
                                                                        ModerateWithReasonAction.PurgeUser.toInt(),
                                                                        contentId = userId,
                                                                    )
                                                                }
                                                            }

                                                            OptionId.Share -> {
                                                                val urls =
                                                                    listOfNotNull(
                                                                        comment.originalUrl,
                                                                        buildString {
                                                                            append("https://")
                                                                            append(uiState.instance)
                                                                            append("/comment/")
                                                                            append(comment.id)
                                                                        },
                                                                    ).distinct()
                                                                if (urls.size == 1) {
                                                                    model.reduce(
                                                                        PostDetailMviModel.Intent.Share(urls.first()),
                                                                    )
                                                                } else {
                                                                    shareBottomSheetUrls = urls
                                                                }
                                                            }

                                                            OptionId.Restore -> {
                                                                model.reduce(
                                                                    PostDetailMviModel.Intent.RestoreComment(
                                                                        comment.id,
                                                                    ),
                                                                )
                                                            }

                                                            else -> Unit
                                                        }
                                                    },
                                                )
                                            },
                                        )
                                    } else {
                                        CollapsedCommentCard(
                                            comment = comment,
                                            isOp = comment.creator?.id == uiState.post.creator?.id,
                                            showBot = true,
                                            isCurrentUser = comment.creator?.id == uiState.currentUserId,
                                            isMod =
                                            comment.creator?.id.let { id ->
                                                uiState.moderators.containsId(id)
                                            },
                                            voteFormat = uiState.voteFormat,
                                            autoLoadImages = uiState.autoLoadImages,
                                            showScores = uiState.showScores,
                                            actionButtonsActive = uiState.isLogged,
                                            indentAmount = uiState.commentIndentAmount,
                                            barThickness = uiState.commentBarThickness,
                                            onToggleExpand = {
                                                model.reduce(
                                                    PostDetailMviModel.Intent.ToggleExpandComment(
                                                        comment.id,
                                                    ),
                                                )
                                            },
                                            onUpVote = {
                                                if (uiState.isLogged && !isOnOtherInstance) {
                                                    model.reduce(
                                                        PostDetailMviModel.Intent.UpVoteComment(
                                                            commentId = comment.id,
                                                        ),
                                                    )
                                                }
                                            },
                                            onDownVote = {
                                                if (uiState.isLogged && !isOnOtherInstance) {
                                                    model.reduce(
                                                        PostDetailMviModel.Intent.DownVoteComment(
                                                            commentId = comment.id,
                                                        ),
                                                    )
                                                }
                                            },
                                            onSave = {
                                                if (uiState.isLogged && !isOnOtherInstance) {
                                                    model.reduce(
                                                        PostDetailMviModel.Intent.SaveComment(
                                                            commentId = comment.id,
                                                        ),
                                                    )
                                                }
                                            },
                                            onReply = {
                                                if (uiState.isLogged && !isOnOtherInstance) {
                                                    mainRouter.openReply(
                                                        originalPost = uiState.post,
                                                        originalComment = comment,
                                                    )
                                                }
                                            },
                                            onOpenCreator = { user ->
                                                mainRouter.openUserDetail(
                                                    user,
                                                    otherInstanceName,
                                                )
                                            },
                                            options =
                                            buildList {
                                                this +=
                                                    Option(
                                                        OptionId.SeeRaw,
                                                        LocalStrings.current.postActionSeeRaw,
                                                    )
                                                this +=
                                                    Option(
                                                        OptionId.Report,
                                                        LocalStrings.current.postActionReport,
                                                    )
                                                if (comment.creator?.id == uiState.currentUserId) {
                                                    this +=
                                                        Option(
                                                            OptionId.Edit,
                                                            LocalStrings.current.postActionEdit,
                                                        )
                                                    if (comment.deleted) {
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
                                                if (uiState.isModerator) {
                                                    this +=
                                                        Option(
                                                            OptionId.DistinguishComment,
                                                            if (comment.distinguished) {
                                                                LocalStrings
                                                                    .current
                                                                    .modActionUnmarkAsDistinguished
                                                            } else {
                                                                LocalStrings
                                                                    .current
                                                                    .modActionMarkAsDistinguished
                                                            },
                                                        )
                                                    this +=
                                                        Option(
                                                            OptionId.Remove,
                                                            LocalStrings
                                                                .current
                                                                .modActionRemove,
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
                                                    comment.creator?.id?.also { creatorId ->
                                                        if (uiState.currentUserId != creatorId) {
                                                            this +=
                                                                Option(
                                                                    OptionId.AddMod,
                                                                    if (uiState.moderators.containsId(
                                                                            creatorId,
                                                                        )
                                                                    ) {
                                                                        LocalStrings
                                                                            .current
                                                                            .modActionRemoveMod
                                                                    } else {
                                                                        LocalStrings.current.modActionAddMod
                                                                    },
                                                                )
                                                        }
                                                    }
                                                }
                                            },
                                            onSelectOption = { optionId ->
                                                when (optionId) {
                                                    OptionId.Delete -> {
                                                        commentIdToDelete = comment.id
                                                    }

                                                    OptionId.Edit -> {
                                                        mainRouter.openReply(
                                                            originalPost = PostModel(id = comment.postId),
                                                            originalComment =
                                                            comment.parentId?.let {
                                                                CommentModel(id = it)
                                                            },
                                                            editedComment = comment,
                                                        )
                                                    }

                                                    OptionId.Report -> {
                                                        mainRouter.openModerateWithReason(
                                                            actionId = ModerateWithReasonAction
                                                                .ReportComment
                                                                .toInt(),
                                                            contentId = comment.id,
                                                        )
                                                    }

                                                    OptionId.SeeRaw -> {
                                                        rawContent = comment
                                                    }

                                                    OptionId.DistinguishComment ->
                                                        model.reduce(
                                                            PostDetailMviModel.Intent.ModDistinguishComment(
                                                                comment.id,
                                                            ),
                                                        )

                                                    OptionId.Remove -> {
                                                        mainRouter.openModerateWithReason(
                                                            actionId = ModerateWithReasonAction
                                                                .RemoveComment
                                                                .toInt(),
                                                            contentId = comment.id,
                                                        )
                                                    }

                                                    OptionId.BanUser -> {
                                                        comment.creator?.id?.also { userId ->
                                                            mainRouter.openBanUser(
                                                                userId = userId,
                                                                communityId = uiState.post.community?.id ?: 0,
                                                                newValue = comment.creator?.banned != true,
                                                                commentId = comment.id,
                                                            )
                                                        }
                                                    }

                                                    OptionId.AddMod -> {
                                                        comment.creator?.id?.also { userId ->
                                                            model.reduce(
                                                                PostDetailMviModel.Intent.ModToggleModUser(
                                                                    userId,
                                                                ),
                                                            )
                                                        }
                                                    }

                                                    OptionId.Restore -> {
                                                        model.reduce(
                                                            PostDetailMviModel.Intent.RestoreComment(
                                                                comment.id,
                                                            ),
                                                        )
                                                    }

                                                    else -> Unit
                                                }
                                            },
                                        )
                                    }
                                }

                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = Spacing.xxxs),
                                    thickness = 0.25.dp,
                                )

                                // load more button
                                if (comment.loadMoreButtonVisible && comment.expanded) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center,
                                    ) {
                                        Button(
                                            onClick = {
                                                model.reduce(
                                                    PostDetailMviModel.Intent.FetchMoreComments(
                                                        parentId = comment.id,
                                                    ),
                                                )
                                            },
                                        ) {
                                            Text(
                                                text =
                                                buildString {
                                                    append(LocalStrings.current.postDetailLoadMoreComments)
                                                    comment.comments
                                                        ?.takeIf { it > 0 }
                                                        ?.also { count ->
                                                            append(" (")
                                                            append(count)
                                                            append(")")
                                                        }
                                                },
                                                style = MaterialTheme.typography.labelSmall,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        item {
                            if (
                                !uiState.initial &&
                                !uiState.loading &&
                                !uiState.refreshing &&
                                uiState.canFetchMore
                            ) {
                                model.reduce(PostDetailMviModel.Intent.LoadNextPage)
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
                        if (uiState.comments.isEmpty() && !uiState.loading && !uiState.initial) {
                            item {
                                Column {
                                    if (uiState.post.comments == 0) {
                                        Text(
                                            modifier =
                                            Modifier
                                                .fillMaxWidth()
                                                .padding(top = Spacing.xs),
                                            textAlign = TextAlign.Center,
                                            text = LocalStrings.current.messageEmptyComments,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onBackground,
                                        )
                                    } else if (uiState.searching) {
                                        Text(
                                            modifier =
                                            Modifier
                                                .fillMaxWidth()
                                                .padding(top = Spacing.xs),
                                            textAlign = TextAlign.Center,
                                            text = LocalStrings.current.messageEmptyList,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onBackground,
                                        )
                                    } else {
                                        Text(
                                            modifier =
                                            Modifier
                                                .fillMaxWidth()
                                                .padding(top = Spacing.xs),
                                            textAlign = TextAlign.Center,
                                            text = LocalStrings.current.messageErrorLoadingComments,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onBackground,
                                        )
                                        Row {
                                            Spacer(modifier = Modifier.weight(1f))
                                            Button(
                                                onClick = {
                                                    model.reduce(PostDetailMviModel.Intent.Refresh)
                                                },
                                            ) {
                                                Text(
                                                    text = LocalStrings.current.buttonRetry,
                                                )
                                            }
                                            Spacer(modifier = Modifier.weight(1f))
                                        }
                                    }
                                }
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(Spacing.xxxl))
                        }
                    }
                }
            }

            if (uiState.enableButtonsToScrollBetweenComments) {
                Row(
                    modifier =
                    Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .onGloballyPositioned {
                            if (bottomBarHeightPx == 0f) {
                                bottomBarHeightPx = it.size.toSize().height
                            }
                        }.padding(bottom = bottomNavigationInset)
                        .offset {
                            IntOffset(
                                x = 0,
                                y = -bottomBarOffsetHeightPx.roundToInt(),
                            )
                        }.background(color = MaterialTheme.colorScheme.background.copy(alpha = 0.45f)),
                ) {
                    if (uiState.isNavigationSupported) {
                        Icon(
                            modifier =
                            Modifier
                                .weight(1f)
                                .padding(vertical = Spacing.s)
                                .onClick(
                                    onClick = {
                                        model.reduce(PostDetailMviModel.Intent.NavigatePrevious)
                                    },
                                ),
                            imageVector = Icons.AutoMirrored.Default.NavigateBefore,
                            contentDescription = LocalStrings.current.actionNavigateToPreviousPost,
                            tint = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                    Icon(
                        modifier =
                        Modifier
                            .weight(1f)
                            .padding(vertical = Spacing.s)
                            .onClick(
                                onClick = {
                                    val idx = lazyListState.firstVisibleItemIndex
                                    model.reduce(
                                        PostDetailMviModel.Intent.NavigatePreviousComment(idx),
                                    )
                                },
                            ),
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = LocalStrings.current.actionNavigateToPreviousComment,
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                    Icon(
                        modifier =
                        Modifier
                            .weight(1f)
                            .padding(vertical = Spacing.s)
                            .onClick(
                                onClick = {
                                    val idx = lazyListState.firstVisibleItemIndex
                                    model.reduce(
                                        PostDetailMviModel.Intent.NavigateNextComment(idx),
                                    )
                                },
                            ),
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = LocalStrings.current.actionNavigateToNextComment,
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                    if (uiState.isNavigationSupported) {
                        Icon(
                            modifier =
                            Modifier
                                .weight(1f)
                                .padding(vertical = Spacing.s)
                                .onClick(
                                    onClick = {
                                        model.reduce(PostDetailMviModel.Intent.NavigateNext)
                                    },
                                ),
                            imageVector = Icons.AutoMirrored.Default.NavigateNext,
                            contentDescription = LocalStrings.current.actionNavigateToNextPost,
                            tint = MaterialTheme.colorScheme.onBackground,
                        )
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
                    upVotes = content.upvotes,
                    downVotes = content.downvotes,
                    publishDate = content.publishDate,
                    updateDate = content.updateDate,
                    onDismiss = {
                        rawContent = null
                    },
                    onQuote = { quotation ->
                        rawContent = null
                        if (quotation != null) {
                            mainRouter.openReply(
                                originalPost = uiState.post,
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

    postToDelete?.also {
        AlertDialog(
            onDismissRequest = {
                postToDelete = null
            },
            dismissButton = {
                Button(
                    onClick = {
                        postToDelete = null
                    },
                ) {
                    Text(text = LocalStrings.current.buttonCancel)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        model.reduce(PostDetailMviModel.Intent.DeletePost)
                        postToDelete = null
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
    commentIdToDelete?.also { itemId ->
        AlertDialog(
            onDismissRequest = {
                commentIdToDelete = null
            },
            dismissButton = {
                Button(
                    onClick = {
                        commentIdToDelete = null
                    },
                ) {
                    Text(text = LocalStrings.current.buttonCancel)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        model.reduce(PostDetailMviModel.Intent.DeleteComment(itemId))
                        commentIdToDelete = null
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
                    model.reduce(PostDetailMviModel.Intent.Share(url = values[index]))
                }
            },
        )
    }

    if (sortBottomSheetOpened) {
        SortBottomSheet(
            values = uiState.availableSortTypes,
            expandTop = false,
            onSelect = { value ->
                sortBottomSheetOpened = false
                if (value != null) {
                    model.reduce(PostDetailMviModel.Intent.ChangeSortType(value = value))
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
                    val text = texts[index]
                    clipboardManager.setText(AnnotatedString(text))
                }
            },
        )
    }
}
