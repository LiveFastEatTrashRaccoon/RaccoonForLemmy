package com.livefast.eattrash.raccoonforlemmy.unit.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.PostLayout
import com.livefast.eattrash.raccoonforlemmy.core.appearance.di.getThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.IconSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.SearchField
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.SwipeAction
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.SwipeActionCard
import com.livefast.eattrash.raccoonforlemmy.core.commonui.detailopener.api.getDetailOpener
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.CommentCard
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.CommunityItem
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.PostCard
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.PostCardPlaceholder
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.UserItem
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.CustomModalBottomSheet
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.CustomModalBottomSheetItem
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.SortBottomSheet
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.TabNavigationSection
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getDrawerCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.navigation.getScreenModel
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.notifications.di.getNotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.ActionOnSwipe
import com.livefast.eattrash.raccoonforlemmy.core.persistence.di.getSettingsRepository
import com.livefast.eattrash.raccoonforlemmy.core.utils.VoteAction
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.onClick
import com.livefast.eattrash.raccoonforlemmy.core.utils.toIcon
import com.livefast.eattrash.raccoonforlemmy.core.utils.toModifier
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.ListingType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SearchResult
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.SearchResultType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.readableHandle
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toIcon
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toReadableName
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.uniqueIdentifier
import com.livefast.eattrash.raccoonforlemmy.unit.explore.components.ExploreTopBar
import com.livefast.eattrash.raccoonforlemmy.unit.zoomableimage.ZoomableImageScreen
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf

class ExploreScreen(
    private val otherInstance: String = "",
) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val model =
            getScreenModel<ExploreMviModel>(
                tag = otherInstance,
                parameters = { parametersOf(otherInstance) },
            )
        val uiState by model.uiState.collectAsState()
        val navigationCoordinator = remember { getNavigationCoordinator() }
        val topAppBarState = rememberTopAppBarState()
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)
        val drawerCoordinator = remember { getDrawerCoordinator() }
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
        val settingsRepository = remember { getSettingsRepository() }
        val settings by settingsRepository.currentSettings.collectAsState()
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
        val detailOpener = remember { getDetailOpener() }
        val connection = navigationCoordinator.getBottomBarScrollConnection()
        val scope = rememberCoroutineScope()
        val notificationCenter = remember { getNotificationCenter() }
        val isOnOtherInstance = remember { otherInstance.isNotEmpty() }
        val otherInstanceName = remember { otherInstance }
        val snackbarHostState = remember { SnackbarHostState() }
        val errorMessage = LocalStrings.current.messageGenericError
        val notificationEventKey =
            buildString {
                append("explore")
                if (isOnOtherInstance) {
                    append("-")
                    append(otherInstanceName)
                }
            }
        val searchFocusRequester = remember { FocusRequester() }
        var listingTypeBottomSheetOpened by remember { mutableStateOf(false) }
        var resultTypeBottomSheetOpened by remember { mutableStateOf(false) }
        var sortBottomSheetOpened by remember { mutableStateOf(false) }

        LaunchedEffect(navigationCoordinator) {
            navigationCoordinator.onDoubleTabSelection
                .onEach { section ->
                    runCatching {
                        if (section == TabNavigationSection.Explore) {
                            lazyListState.scrollToItem(0)
                            topAppBarState.heightOffset = 0f
                            topAppBarState.contentOffset = 0f
                        }
                    }
                }.launchIn(this)
        }
        LaunchedEffect(model) {
            model.effects
                .onEach {
                    when (it) {
                        ExploreMviModel.Effect.BackToTop -> {
                            runCatching {
                                lazyListState.scrollToItem(0)
                                topAppBarState.heightOffset = 0f
                                topAppBarState.contentOffset = 0f
                            }
                        }

                        ExploreMviModel.Effect.OperationFailure -> {
                            snackbarHostState.showSnackbar(errorMessage)
                        }

                        ExploreMviModel.Effect.OpenSearch -> {
                            searchFocusRequester.requestFocus()
                        }
                    }
                }.launchIn(this)
        }

        Scaffold(
            topBar = {
                ExploreTopBar(
                    topAppBarState = topAppBarState,
                    scrollBehavior = scrollBehavior,
                    listingType = uiState.listingType,
                    sortType = uiState.sortType,
                    resultType = uiState.resultType,
                    otherInstance = otherInstanceName,
                    edgeToEdge = settings.edgeToEdge,
                    onSelectListingType = {
                        focusManager.clearFocus()
                        listingTypeBottomSheetOpened = true
                    },
                    onSelectSortType = {
                        focusManager.clearFocus()
                        sortBottomSheetOpened = true
                    },
                    onSelectResultTypeType = {
                        resultTypeBottomSheetOpened = true
                    },
                    onHamburgerTapped = {
                        scope.launch {
                            drawerCoordinator.toggleDrawer()
                        }
                    },
                    onBack = {
                        navigationCoordinator.popScreen()
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
        ) { padding ->
            Column(
                modifier =
                    Modifier
                        .padding(
                            top = padding.calculateTopPadding(),
                        ),
                verticalArrangement = Arrangement.spacedBy(Spacing.xs),
            ) {
                SearchField(
                    modifier =
                        Modifier
                            .focusRequester(searchFocusRequester)
                            .padding(
                                horizontal = Spacing.s,
                                vertical = Spacing.s,
                            ).fillMaxWidth(),
                    hint = LocalStrings.current.exploreSearchPlaceholder,
                    value = uiState.searchText,
                    onValueChange = { value ->
                        model.reduce(ExploreMviModel.Intent.SetSearch(value))
                    },
                    onClear = {
                        model.reduce(ExploreMviModel.Intent.SetSearch(""))
                    },
                )

                PullToRefreshBox(
                    modifier =
                        Modifier
                            .padding(top = Spacing.xs)
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
                            ).nestedScroll(keyboardScrollConnection),
                    isRefreshing = uiState.refreshing,
                    onRefresh = { model.reduce(ExploreMviModel.Intent.Refresh) },
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = lazyListState,
                    ) {
                        if (uiState.results.isEmpty() && uiState.initial) {
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
                        items(uiState.results, key = { it.uniqueIdentifier }) { result ->
                            when (result) {
                                is SearchResult.Community -> {
                                    CommunityItem(
                                        modifier =
                                            Modifier.fillMaxWidth().onClick(
                                                onClick = {
                                                    detailOpener.openCommunityDetail(
                                                        community = result.model,
                                                        otherInstance = otherInstanceName,
                                                    )
                                                },
                                            ),
                                        community = result.model,
                                        autoLoadImages = uiState.autoLoadImages,
                                        preferNicknames = uiState.preferNicknames,
                                        showSubscribeButton = !isOnOtherInstance,
                                        onSubscribe = {
                                            model.reduce(
                                                ExploreMviModel.Intent.ToggleSubscription(
                                                    result.model.id,
                                                ),
                                            )
                                        },
                                        highlightText = uiState.searchText,
                                    )
                                }

                                is SearchResult.Post -> {
                                    @Composable
                                    fun List<ActionOnSwipe>.toSwipeActions(): List<SwipeAction> =
                                        mapNotNull {
                                            when (it) {
                                                ActionOnSwipe.UpVote ->
                                                    SwipeAction(
                                                        swipeContent = {
                                                            Icon(
                                                                modifier =
                                                                    Modifier.then(
                                                                        VoteAction.UpVote.toModifier(),
                                                                    ),
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
                                                                ExploreMviModel.Intent.UpVotePost(
                                                                    result.model.id,
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
                                                                ExploreMviModel.Intent.DownVotePost(
                                                                    result.model.id,
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
                                                            detailOpener.openReply(
                                                                originalPost = result.model,
                                                            )
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
                                                        backgroundColor =
                                                            saveColor
                                                                ?: defaultSaveColor,
                                                        onTriggered = {
                                                            model.reduce(
                                                                ExploreMviModel.Intent.SavePost(
                                                                    id = result.model.id,
                                                                ),
                                                            )
                                                        },
                                                    )

                                                else -> null
                                            }
                                        }

                                    SwipeActionCard(
                                        modifier = Modifier.fillMaxWidth(),
                                        enabled = uiState.swipeActionsEnabled && !isOnOtherInstance,
                                        onGestureBegin = {
                                            model.reduce(ExploreMviModel.Intent.HapticIndication)
                                        },
                                        swipeToStartActions = uiState.actionsOnSwipeToStartPosts.toSwipeActions(),
                                        swipeToEndActions =
                                            if (uiState.isLogged) {
                                                uiState.actionsOnSwipeToEndPosts.toSwipeActions()
                                            } else {
                                                emptyList()
                                            },
                                        content = {
                                            PostCard(
                                                post = result.model,
                                                postLayout = uiState.postLayout,
                                                limitBodyHeight = true,
                                                fullHeightImage = uiState.fullHeightImages,
                                                fullWidthImage = uiState.fullWidthImages,
                                                voteFormat = uiState.voteFormat,
                                                autoLoadImages = uiState.autoLoadImages,
                                                preferNicknames = uiState.preferNicknames,
                                                showScores = uiState.showScores,
                                                blurNsfw = uiState.blurNsfw,
                                                actionButtonsActive = uiState.isLogged,
                                                downVoteEnabled = uiState.downVoteEnabled,
                                                highlightText = uiState.searchText,
                                                onClick = {
                                                    detailOpener.openPostDetail(
                                                        post = result.model,
                                                        otherInstance = otherInstanceName,
                                                    )
                                                },
                                                onDoubleClick =
                                                    {
                                                        model.reduce(
                                                            ExploreMviModel.Intent.UpVotePost(
                                                                id = result.model.id,
                                                                feedback = true,
                                                            ),
                                                        )
                                                    }.takeIf { uiState.downVoteEnabled && uiState.isLogged && !isOnOtherInstance },
                                                onOpenCommunity = { community, instance ->
                                                    detailOpener.openCommunityDetail(
                                                        community = community,
                                                        otherInstance =
                                                            instance.takeIf {
                                                                it.isNotEmpty()
                                                            } ?: otherInstanceName,
                                                    )
                                                },
                                                onOpenCreator = { user, instance ->
                                                    detailOpener.openUserDetail(
                                                        user = user,
                                                        otherInstance =
                                                            instance.takeIf {
                                                                it.isNotEmpty()
                                                            } ?: otherInstanceName,
                                                    )
                                                },
                                                onUpVote =
                                                    {
                                                        model.reduce(
                                                            ExploreMviModel.Intent.UpVotePost(result.model.id),
                                                        )
                                                    }.takeIf { uiState.isLogged },
                                                onDownVote =
                                                    {
                                                        model.reduce(
                                                            ExploreMviModel.Intent.DownVotePost(
                                                                result.model.id,
                                                            ),
                                                        )
                                                    }.takeIf { uiState.isLogged },
                                                onSave =
                                                    {
                                                        model.reduce(
                                                            ExploreMviModel.Intent.SavePost(result.model.id),
                                                        )
                                                    }.takeIf { uiState.isLogged },
                                                onOpenImage = { url ->
                                                    navigationCoordinator.pushScreen(
                                                        ZoomableImageScreen(
                                                            url = url,
                                                            source =
                                                                result.model.community
                                                                    ?.readableHandle
                                                                    .orEmpty(),
                                                        ),
                                                    )
                                                },
                                                onOpenVideo = { url ->
                                                    navigationCoordinator.pushScreen(
                                                        ZoomableImageScreen(
                                                            url = url,
                                                            isVideo = true,
                                                            source =
                                                                result.model.community
                                                                    ?.readableHandle
                                                                    .orEmpty(),
                                                        ),
                                                    )
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

                                is SearchResult.Comment -> {
                                    @Composable
                                    fun List<ActionOnSwipe>.toSwipeActions(): List<SwipeAction> =
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
                                                                ExploreMviModel.Intent.UpVoteComment(
                                                                    result.model.id,
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
                                                                ExploreMviModel.Intent.DownVoteComment(
                                                                    result.model.id,
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
                                                            detailOpener.openPostDetail(
                                                                post = PostModel(id = result.model.postId),
                                                                highlightCommentId = result.model.id,
                                                                otherInstance = otherInstanceName,
                                                            )
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
                                                        backgroundColor =
                                                            saveColor
                                                                ?: defaultSaveColor,
                                                        onTriggered = {
                                                            model.reduce(
                                                                ExploreMviModel.Intent.SaveComment(
                                                                    result.model.id,
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
                                            model.reduce(ExploreMviModel.Intent.HapticIndication)
                                        },
                                        swipeToStartActions = uiState.actionsOnSwipeToStartComments.toSwipeActions(),
                                        swipeToEndActions =
                                            if (uiState.isLogged) {
                                                uiState.actionsOnSwipeToEndComments.toSwipeActions()
                                            } else {
                                                emptyList()
                                            },
                                        content = {
                                            CommentCard(
                                                modifier = Modifier.background(MaterialTheme.colorScheme.background),
                                                comment = result.model,
                                                voteFormat = uiState.voteFormat,
                                                autoLoadImages = uiState.autoLoadImages,
                                                preferNicknames = uiState.preferNicknames,
                                                showScores = uiState.showScores,
                                                showBot = true,
                                                showExpandedIndicator = false,
                                                indentAmount = 0,
                                                actionButtonsActive = uiState.isLogged,
                                                downVoteEnabled = uiState.downVoteEnabled,
                                                highlightText = uiState.searchText,
                                                onClick = {
                                                    detailOpener.openPostDetail(
                                                        post = PostModel(id = result.model.postId),
                                                        highlightCommentId = result.model.id,
                                                        otherInstance = otherInstanceName,
                                                    )
                                                },
                                                onDoubleClick =
                                                    {
                                                        if (uiState.isLogged) {
                                                            model.reduce(
                                                                ExploreMviModel.Intent.UpVoteComment(
                                                                    id = result.model.id,
                                                                    feedback = true,
                                                                ),
                                                            )
                                                        }
                                                    }.takeIf { uiState.doubleTapActionEnabled },
                                                onUpVote =
                                                    {
                                                        model.reduce(
                                                            ExploreMviModel.Intent.UpVoteComment(
                                                                id = result.model.id,
                                                            ),
                                                        )
                                                    }.takeIf { uiState.isLogged },
                                                onDownVote =
                                                    {
                                                        model.reduce(
                                                            ExploreMviModel.Intent.DownVoteComment(
                                                                id = result.model.id,
                                                            ),
                                                        )
                                                    }.takeIf { uiState.isLogged },
                                                onSave =
                                                    {
                                                        model.reduce(
                                                            ExploreMviModel.Intent.SaveComment(
                                                                id = result.model.id,
                                                            ),
                                                        )
                                                    }.takeIf { uiState.isLogged },
                                                onOpenCommunity = { community, instance ->
                                                    detailOpener.openCommunityDetail(
                                                        community = community,
                                                        otherInstance =
                                                            instance.takeIf {
                                                                it.isNotEmpty()
                                                            } ?: otherInstanceName,
                                                    )
                                                },
                                                onOpenCreator = { user, instance ->
                                                    detailOpener.openUserDetail(
                                                        user = user,
                                                        otherInstance =
                                                            instance.takeIf {
                                                                it.isNotEmpty()
                                                            } ?: otherInstanceName,
                                                    )
                                                },
                                            )
                                        },
                                    )
                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical = Spacing.interItem),
                                        thickness = 0.25.dp,
                                    )
                                }

                                is SearchResult.User -> {
                                    UserItem(
                                        modifier =
                                            Modifier.fillMaxWidth().onClick(
                                                onClick = {
                                                    detailOpener.openUserDetail(
                                                        user = result.model,
                                                        otherInstance = otherInstanceName,
                                                    )
                                                },
                                            ),
                                        user = result.model,
                                        preferNicknames = uiState.preferNicknames,
                                        highlightText = uiState.searchText,
                                    )
                                }
                            }
                        }
                        item {
                            if (!uiState.initial && !uiState.loading && !uiState.refreshing && uiState.canFetchMore) {
                                model.reduce(ExploreMviModel.Intent.LoadNextPage)
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

                        if (uiState.results.isEmpty() && !uiState.loading) {
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
            }
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
                                screenKey = notificationEventKey,
                            ),
                        )
                    }
                },
            )
        }

        if (resultTypeBottomSheetOpened) {
            val values =
                listOf(
                    SearchResultType.Posts,
                    SearchResultType.Communities,
                    SearchResultType.Comments,
                    SearchResultType.Users,
                    SearchResultType.Urls,
                )
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
                    resultTypeBottomSheetOpened = false
                    if (index != null) {
                        notificationCenter.send(
                            NotificationCenterEvent.ChangeSearchResultType(
                                value = values[index],
                                screenKey = notificationEventKey,
                            ),
                        )
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
                                screenKey = notificationEventKey,
                            ),
                        )
                    }
                },
            )
        }
    }
}
