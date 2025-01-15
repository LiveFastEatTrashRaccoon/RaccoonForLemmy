package com.livefast.eattrash.raccoonforlemmy.unit.multicommunity.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.kodein.rememberScreenModel
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.PostLayout
import com.livefast.eattrash.raccoonforlemmy.core.appearance.di.getThemeRepository
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.toWindowInsets
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
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.CustomModalBottomSheet
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.CustomModalBottomSheetItem
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.SortBottomSheet
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.notifications.di.getNotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.ActionOnSwipe
import com.livefast.eattrash.raccoonforlemmy.core.persistence.di.getSettingsRepository
import com.livefast.eattrash.raccoonforlemmy.core.utils.VoteAction
import com.livefast.eattrash.raccoonforlemmy.core.utils.toIcon
import com.livefast.eattrash.raccoonforlemmy.core.utils.toModifier
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.getAdditionalLabel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.readableHandle
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toIcon
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toReadableName
import com.livefast.eattrash.raccoonforlemmy.unit.moderatewithreason.ModerateWithReasonAction
import com.livefast.eattrash.raccoonforlemmy.unit.moderatewithreason.ModerateWithReasonScreen
import com.livefast.eattrash.raccoonforlemmy.unit.moderatewithreason.toInt
import com.livefast.eattrash.raccoonforlemmy.unit.zoomableimage.ZoomableImageScreen
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MultiCommunityScreen(
    private val communityId: Long,
) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val model: MultiCommunityMviModel = rememberScreenModel(arg = communityId)
        val uiState by model.uiState.collectAsState()
        val topAppBarState = rememberTopAppBarState()
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)
        val navigationCoordinator = remember { getNavigationCoordinator() }
        val themeRepository = remember { getThemeRepository() }
        val upVoteColor by themeRepository.upVoteColor.collectAsState()
        val downVoteColor by themeRepository.downVoteColor.collectAsState()
        val replyColor by themeRepository.replyColor.collectAsState()
        val saveColor by themeRepository.saveColor.collectAsState()
        val defaultUpvoteColor = MaterialTheme.colorScheme.primary
        val defaultReplyColor = MaterialTheme.colorScheme.secondary
        val defaultDownVoteColor = MaterialTheme.colorScheme.tertiary
        val defaultSaveColor = MaterialTheme.colorScheme.secondaryContainer
        val lazyListState = rememberLazyListState()
        val scope = rememberCoroutineScope()
        val fabNestedScrollConnection = remember { getFabNestedScrollConnection() }
        val isFabVisible by fabNestedScrollConnection.isFabVisible.collectAsState()
        val settingsRepository = remember { getSettingsRepository() }
        val settings by settingsRepository.currentSettings.collectAsState()
        val detailOpener = remember { getDetailOpener() }
        val notificationCenter = remember { getNotificationCenter() }
        val clipboardManager = LocalClipboardManager.current
        var shareBottomSheetUrls by remember { mutableStateOf<List<String>?>(null) }
        var sortBottomSheetOpened by remember { mutableStateOf(false) }
        var copyPostBottomSheet by remember { mutableStateOf<PostModel?>(null) }

        LaunchedEffect(model) {
            model.effects
                .onEach { effect ->
                    when (effect) {
                        MultiCommunityMviModel.Effect.BackToTop -> {
                            runCatching {
                                lazyListState.scrollToItem(0)
                                topAppBarState.heightOffset = 0f
                                topAppBarState.contentOffset = 0f
                            }
                        }

                        is MultiCommunityMviModel.Effect.OpenDetail ->
                            detailOpener.openPostDetail(effect.post)
                    }
                }.launchIn(this)
        }

        Scaffold(
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            topBar = {
                val sortType = uiState.sortType
                TopAppBar(
                    windowInsets = topAppBarState.toWindowInsets(),
                    title = {
                        Text(
                            text = uiState.community.name,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    },
                    scrollBehavior = scrollBehavior,
                    navigationIcon = {
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
                    },
                    actions = {
                        val additionalLabel = sortType.getAdditionalLabel()
                        if (additionalLabel.isNotEmpty()) {
                            Text(
                                text =
                                    buildString {
                                        append("(")
                                        append(additionalLabel)
                                        append(")")
                                    },
                            )
                            Spacer(modifier = Modifier.width(Spacing.xs))
                        }
                        if (sortType != null) {
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
                                this +=
                                    FloatingActionButtonMenuItem(
                                        icon = Icons.Default.ClearAll,
                                        text = LocalStrings.current.actionClearRead,
                                        onSelected = {
                                            model.reduce(MultiCommunityMviModel.Intent.ClearRead)
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
                    model.reduce(MultiCommunityMviModel.Intent.Refresh)
                },
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = lazyListState,
                ) {
                    if (uiState.posts.isEmpty() && uiState.loading) {
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
                        key = {
                            it.id.toString() + (it.updateDate ?: it.publishDate)
                        },
                    ) { post ->
                        LaunchedEffect(post.id) {
                            if (settings.markAsReadWhileScrolling && !post.read) {
                                model.reduce(MultiCommunityMviModel.Intent.MarkAsRead(post.id))
                            }
                        }

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
                                                    contentDescription = LocalStrings.current.actionUpvote,
                                                    tint = Color.White,
                                                )
                                            },
                                            backgroundColor = upVoteColor ?: defaultUpvoteColor,
                                            onTriggered = {
                                                model.reduce(
                                                    MultiCommunityMviModel.Intent.UpVotePost(post.id),
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
                                            backgroundColor = downVoteColor ?: defaultDownVoteColor,
                                            onTriggered = {
                                                model.reduce(
                                                    MultiCommunityMviModel.Intent.DownVotePost(
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
                                                    MultiCommunityMviModel.Intent.SavePost(
                                                        id = post.id,
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
                                model.reduce(MultiCommunityMviModel.Intent.HapticIndication)
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
                                    showScores = uiState.showScores,
                                    blurNsfw = uiState.blurNsfw,
                                    fadeRead = uiState.fadeReadPosts,
                                    showUnreadComments = uiState.showUnreadComments,
                                    downVoteEnabled = uiState.downVoteEnabled,
                                    botTagColor = uiState.botTagColor,
                                    meTagColor = uiState.meTagColor,
                                    onClick = {
                                        model.reduce(
                                            MultiCommunityMviModel.Intent.WillOpenDetail(post.id),
                                        )
                                    },
                                    onDoubleClick =
                                        {
                                            model.reduce(
                                                MultiCommunityMviModel.Intent.UpVotePost(
                                                    id = post.id,
                                                    feedback = true,
                                                ),
                                            )
                                        }.takeIf { uiState.swipeActionsEnabled },
                                    onOpenCommunity = { community, instance ->
                                        detailOpener.openCommunityDetail(community, instance)
                                    },
                                    onOpenCreator = { user, instance ->
                                        detailOpener.openUserDetail(user, instance)
                                    },
                                    onUpVote = {
                                        model.reduce(
                                            MultiCommunityMviModel.Intent.UpVotePost(id = post.id),
                                        )
                                    },
                                    onDownVote = {
                                        model.reduce(
                                            MultiCommunityMviModel.Intent.DownVotePost(id = post.id),
                                        )
                                    },
                                    onSave = {
                                        model.reduce(
                                            MultiCommunityMviModel.Intent.SavePost(id = post.id),
                                        )
                                    },
                                    onReply = {
                                        model.reduce(
                                            MultiCommunityMviModel.Intent.WillOpenDetail(post.id),
                                        )
                                    },
                                    onOpenImage = { url ->
                                        model.reduce(MultiCommunityMviModel.Intent.MarkAsRead(post.id))
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
                                            if (uiState.currentUserId != null) {
                                                this +=
                                                    Option(
                                                        OptionId.Hide,
                                                        LocalStrings.current.postActionHide,
                                                    )
                                                this +=
                                                    Option(
                                                        OptionId.Report,
                                                        LocalStrings.current.postActionReport,
                                                    )
                                            }
                                        },
                                    onOptionSelected = { optionId ->
                                        when (optionId) {
                                            OptionId.Report -> {
                                                val screen =
                                                    ModerateWithReasonScreen(
                                                        actionId = ModerateWithReasonAction.ReportPost.toInt(),
                                                        contentId = post.id,
                                                    )
                                                navigationCoordinator.pushScreen(screen)
                                            }

                                            OptionId.Hide ->
                                                model.reduce(
                                                    MultiCommunityMviModel.Intent.Hide(
                                                        post.id,
                                                    ),
                                                )

                                            OptionId.Share -> {
                                                val urls =
                                                    listOfNotNull(
                                                        post.originalUrl,
                                                        "https://${uiState.instance}/post/${post.id}",
                                                    ).distinct()
                                                if (urls.size == 1) {
                                                    model.reduce(
                                                        MultiCommunityMviModel.Intent.Share(
                                                            urls.first(),
                                                        ),
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
                                                    clipboardManager.setText(AnnotatedString(texts.first()))
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
                    item {
                        if (!uiState.initial && !uiState.loading && !uiState.refreshing && uiState.canFetchMore) {
                            if (settings.infiniteScrollEnabled) {
                                model.reduce(MultiCommunityMviModel.Intent.LoadNextPage)
                            } else {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(top = Spacing.s),
                                    horizontalArrangement = Arrangement.Center,
                                ) {
                                    Button(
                                        onClick = {
                                            model.reduce(MultiCommunityMviModel.Intent.LoadNextPage)
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

                    if (uiState.posts.isEmpty() && !uiState.initial) {
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
                                screenKey = "multiCommunity",
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
                title = LocalStrings.current.communityDetailBlock,
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
