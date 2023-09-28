package com.github.diegoberaldin.raccoonforlemmy.core.commonui.communitydetail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Pending
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.data.PostLayout
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.bindToLifecycle
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.communityInfo.CommunityInfoScreen
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.CommunityHeader
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.PostCard
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.SwipeableCard
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.createcomment.CreateCommentScreen
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.createpost.CreatePostScreen
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.di.getCommunityDetailViewModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.di.getNavigationCoordinator
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.image.ZoomableImageScreen
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.instanceinfo.InstanceInfoScreen
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.modals.SortBottomSheet
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.postdetail.PostDetailScreen
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.userdetail.UserDetailScreen
import com.github.diegoberaldin.raccoonforlemmy.core.notifications.NotificationCenterContractKeys
import com.github.diegoberaldin.raccoonforlemmy.core.notifications.di.getNotificationCenter
import com.github.diegoberaldin.raccoonforlemmy.core.utils.onClick
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.SortType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.toIcon
import com.github.diegoberaldin.raccoonforlemmy.resources.MR
import dev.icerock.moko.resources.compose.stringResource

class CommunityDetailScreen(
    private val community: CommunityModel,
    private val otherInstance: String = "",
) : Screen {

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val model = rememberScreenModel(community.id.toString() + otherInstance) {
            getCommunityDetailViewModel(
                community = community,
                otherInstance = otherInstance,
            )
        }
        model.bindToLifecycle(key)
        val uiState by model.uiState.collectAsState()
        val navigator = remember { getNavigationCoordinator().getRootNavigator() }
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val isOnOtherInstance = otherInstance.isNotEmpty()
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
        val isFabVisible = remember { mutableStateOf(true) }
        val fabNestedScrollConnection = remember {
            object : NestedScrollConnection {
                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                    if (available.y < -1) {
                        isFabVisible.value = false
                    }
                    if (available.y > 1) {
                        isFabVisible.value = true
                    }
                    return Offset.Zero
                }
            }
        }
        val notificationCenter = remember { getNotificationCenter() }
        DisposableEffect(key) {
            onDispose {
                notificationCenter.removeObserver(key)
            }
        }

        val stateCommunity = uiState.community
        Scaffold(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
                .padding(Spacing.xs),
            topBar = {
                TopAppBar(
                    scrollBehavior = scrollBehavior,
                    title = {
                        Text(
                            modifier = Modifier.padding(horizontal = Spacing.s),
                            text = stateCommunity.name,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    },
                    actions = {
                        // subscribe button
                        if (!isOnOtherInstance && uiState.isLogged) {
                            Image(
                                modifier = Modifier.onClick {
                                    when (stateCommunity.subscribed) {
                                        true -> model.reduce(CommunityDetailMviModel.Intent.Unsubscribe)
                                        false -> model.reduce(CommunityDetailMviModel.Intent.Subscribe)
                                        else -> Unit
                                    }
                                },
                                imageVector = when (stateCommunity.subscribed) {
                                    true -> Icons.Outlined.CheckCircle
                                    false -> Icons.Outlined.AddCircleOutline
                                    else -> Icons.Outlined.Pending
                                },
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground),
                            )
                            Spacer(Modifier.width(Spacing.s))
                        }

                        // sort button
                        Image(
                            modifier = Modifier.onClick {
                                val sheet = SortBottomSheet(
                                    expandTop = true,
                                )
                                notificationCenter.addObserver({
                                    (it as? SortType)?.also { sortType ->
                                        model.reduce(
                                            CommunityDetailMviModel.Intent.ChangeSort(
                                                sortType
                                            )
                                        )
                                    }
                                }, key, NotificationCenterContractKeys.ChangeSortType)
                                bottomSheetNavigator.show(sheet)
                            },
                            imageVector = uiState.sortType.toIcon(),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                        )
                    },
                    navigationIcon = {
                        Image(
                            modifier = Modifier.onClick {
                                navigator?.pop()
                            },
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                        )
                    },
                )
            },
            floatingActionButton = {
                AnimatedVisibility(
                    visible = isFabVisible.value,
                    enter = slideInVertically(
                        initialOffsetY = { it * 2 },
                    ),
                    exit = slideOutVertically(
                        targetOffsetY = { it * 2 },
                    ),
                ) {
                    FloatingActionButton(
                        backgroundColor = MaterialTheme.colorScheme.secondary,
                        shape = CircleShape,
                        onClick = {
                            val screen = CreatePostScreen(
                                communityId = stateCommunity.id,
                            )
                            notificationCenter.addObserver({
                                model.reduce(CommunityDetailMviModel.Intent.Refresh)
                            }, key, NotificationCenterContractKeys.PostCreated)
                            bottomSheetNavigator.show(screen)
                        },
                        content = {
                            Icon(
                                imageVector = Icons.Default.Create,
                                contentDescription = null,
                            )
                        },
                    )
                }
            }) { padding ->
            if (uiState.currentUserId != null) {
                val pullRefreshState = rememberPullRefreshState(uiState.refreshing, {
                    model.reduce(CommunityDetailMviModel.Intent.Refresh)
                })
                Box(
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
                        .nestedScroll(fabNestedScrollConnection).padding(padding)
                        .pullRefresh(pullRefreshState),
                ) {
                    LazyColumn {
                        item {
                            CommunityHeader(
                                community = stateCommunity,
                                onOpenCommunityInfo = {
                                    bottomSheetNavigator.show(
                                        CommunityInfoScreen(stateCommunity),
                                    )
                                },
                                onOpenInstanceInfo = {
                                    navigator?.push(
                                        InstanceInfoScreen(
                                            url = stateCommunity.instanceUrl,
                                        ),
                                    )
                                },
                            )
                        }
                        itemsIndexed(uiState.posts) { idx, post ->
                            SwipeableCard(
                                modifier = Modifier.fillMaxWidth(),
                                enabled = uiState.swipeActionsEnabled,
                                directions = if (isOnOtherInstance) {
                                    emptySet()
                                } else {
                                    setOf(
                                        DismissDirection.StartToEnd,
                                        DismissDirection.EndToStart,
                                    )
                                },
                                backgroundColor = {
                                    when (it) {
                                        DismissValue.DismissedToStart -> MaterialTheme.colorScheme.secondary
                                        DismissValue.DismissedToEnd -> MaterialTheme.colorScheme.tertiary
                                        else -> Color.Transparent
                                    }
                                },
                                swipeContent = { direction ->
                                    val icon = when (direction) {
                                        DismissDirection.StartToEnd -> Icons.Default.ArrowCircleDown
                                        DismissDirection.EndToStart -> Icons.Default.ArrowCircleUp
                                    }
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = null,
                                        tint = Color.White,
                                    )
                                },
                                onGestureBegin = {
                                    model.reduce(CommunityDetailMviModel.Intent.HapticIndication)
                                },
                                onDismissToStart = {
                                    model.reduce(
                                        CommunityDetailMviModel.Intent.UpVotePost(idx),
                                    )
                                },
                                onDismissToEnd = {
                                    model.reduce(
                                        CommunityDetailMviModel.Intent.DownVotePost(idx),
                                    )
                                },
                                content = {
                                    PostCard(
                                        modifier = Modifier.onClick {
                                            navigator?.push(
                                                PostDetailScreen(post),
                                            )
                                        },
                                        onOpenCreator = { user ->
                                            navigator?.push(
                                                UserDetailScreen(user),
                                            )
                                        },
                                        post = post,
                                        postLayout = uiState.postLayout,
                                        options = buildList {
                                            add(stringResource(MR.strings.post_action_share))
                                            if (post.creator?.id == uiState.currentUserId) {
                                                add(stringResource(MR.strings.post_action_edit))
                                                add(stringResource(MR.strings.comment_action_delete))
                                            }
                                        },
                                        blurNsfw = when {
                                            stateCommunity.nsfw -> false
                                            else -> uiState.blurNsfw
                                        },
                                        onUpVote = if (isOnOtherInstance) {
                                            null
                                        } else {
                                            {
                                                model.reduce(
                                                    CommunityDetailMviModel.Intent.UpVotePost(
                                                        index = idx,
                                                        feedback = true,
                                                    ),
                                                )
                                            }
                                        },
                                        onDownVote = if (isOnOtherInstance) {
                                            null
                                        } else {
                                            {
                                                model.reduce(
                                                    CommunityDetailMviModel.Intent.DownVotePost(
                                                        index = idx,
                                                        feedback = true,
                                                    ),
                                                )
                                            }
                                        },
                                        onSave = if (isOnOtherInstance) {
                                            null
                                        } else {
                                            {
                                                model.reduce(
                                                    CommunityDetailMviModel.Intent.SavePost(
                                                        index = idx,
                                                        feedback = true,
                                                    ),
                                                )
                                            }
                                        },
                                        onReply = {
                                            val screen = CreateCommentScreen(
                                                originalPost = post,
                                            )
                                            notificationCenter.addObserver({
                                                model.reduce(CommunityDetailMviModel.Intent.Refresh)
                                            }, key, NotificationCenterContractKeys.CommentCreated)
                                            bottomSheetNavigator.show(screen)
                                        },
                                        onImageClick = { url ->
                                            navigator?.push(
                                                ZoomableImageScreen(url),
                                            )
                                        },
                                        onOptionSelected = { optionIdx ->
                                            when (optionIdx) {
                                                2 -> model.reduce(
                                                    CommunityDetailMviModel.Intent.DeletePost(
                                                        post.id
                                                    )
                                                )

                                                1 -> {
                                                    notificationCenter.addObserver(
                                                        {
                                                            model.reduce(CommunityDetailMviModel.Intent.Refresh)
                                                        },
                                                        key,
                                                        NotificationCenterContractKeys.PostCreated
                                                    )
                                                    bottomSheetNavigator.show(
                                                        CreatePostScreen(
                                                            editedPost = post,
                                                        )
                                                    )
                                                }

                                                else -> model.reduce(
                                                    CommunityDetailMviModel.Intent.SharePost(idx)
                                                )
                                            }
                                        }
                                    )
                                },
                            )
                            if (uiState.postLayout != PostLayout.Card) {
                                Divider(modifier = Modifier.padding(vertical = Spacing.s))
                            } else {
                                Spacer(modifier = Modifier.height(Spacing.xs))
                            }
                        }
                        item {
                            if (!uiState.loading && !uiState.refreshing && uiState.canFetchMore) {
                                model.reduce(CommunityDetailMviModel.Intent.LoadNextPage)
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
                            Spacer(modifier = Modifier.height(Spacing.s))
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
    }
}
