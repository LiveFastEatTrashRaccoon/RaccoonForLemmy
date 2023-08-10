package com.github.diegoberaldin.raccoonforlemmy.feature.home.postlist

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.diegoberaldin.racconforlemmy.core.utils.onClick
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.bindToLifecycle
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.communitydetail.CommunityDetailScreen
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.communitydetail.PostCard
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.modals.ListingTypeBottomSheet
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.modals.SortBottomSheet
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.postdetail.PostDetailScreen
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.userdetail.UserDetailScreen
import com.github.diegoberaldin.raccoonforlemmy.feature.home.di.getHomeScreenModel

class PostListScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val model = rememberScreenModel { getHomeScreenModel() }
        model.bindToLifecycle(key)
        val uiState by model.uiState.collectAsState()
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val navigator = LocalNavigator.currentOrThrow

        Scaffold(
            modifier = Modifier.padding(Spacing.xxs),
            topBar = {
                PostsTopBar(
                    currentInstance = uiState.instance,
                    listingType = uiState.listingType,
                    sortType = uiState.sortType,
                    onSelectListingType = {
                        bottomSheetNavigator.show(
                            ListingTypeBottomSheet(
                                isLogged = uiState.isLogged,
                                onSelected = {
                                    model.reduce(PostListMviModel.Intent.ChangeListing(it))
                                },
                                onHide = {
                                    bottomSheetNavigator.hide()
                                },
                            ),
                        )
                    },
                    onSelectSortType = {
                        bottomSheetNavigator.show(
                            SortBottomSheet(
                                onSelected = {
                                    model.reduce(PostListMviModel.Intent.ChangeSort(it))
                                },
                                onHide = {
                                    bottomSheetNavigator.hide()
                                },
                            ),
                        )
                    },
                )
            },
        ) { padding ->
            val pullRefreshState = rememberPullRefreshState(uiState.refreshing, {
                model.reduce(PostListMviModel.Intent.Refresh)
            })
            Box(
                modifier = Modifier.padding(padding).pullRefresh(pullRefreshState),
            ) {
                var width by remember { mutableStateOf(0f) }
                LazyColumn(
                    modifier = Modifier.fillMaxSize().onGloballyPositioned {
                        width = it.size.toSize().width
                    },
                    verticalArrangement = Arrangement.spacedBy(Spacing.xs),
                ) {
                    items(uiState.posts, key = { it.id.toString() + it.myVote }) { post ->
                        val dismissState = rememberDismissState(
                            confirmStateChange = {
                                when (it) {
                                    DismissValue.DismissedToEnd -> {
                                        model.reduce(
                                            PostListMviModel.Intent.DownVotePost(
                                                post = post,
                                            ),
                                        )
                                    }

                                    DismissValue.DismissedToStart -> {
                                        model.reduce(
                                            PostListMviModel.Intent.UpVotePost(
                                                post = post,
                                            ),
                                        )
                                    }

                                    else -> Unit
                                }
                                false
                            },
                        )
                        var willDismissDirection: DismissDirection? by remember {
                            mutableStateOf(null)
                        }
                        val threshold = 0.15f
                        LaunchedEffect(Unit) {
                            snapshotFlow { dismissState.offset.value }.collect {
                                willDismissDirection = when {
                                    it > width * threshold -> DismissDirection.StartToEnd
                                    it < -width * threshold -> DismissDirection.EndToStart
                                    else -> null
                                }
                            }
                        }
                        LaunchedEffect(willDismissDirection) {
                            if (willDismissDirection != null) {
                                model.reduce(PostListMviModel.Intent.HapticIndication)
                            }
                        }
                        SwipeToDismiss(
                            state = dismissState,
                            directions = setOf(
                                DismissDirection.StartToEnd,
                                DismissDirection.EndToStart,
                            ),
                            dismissThresholds = {
                                FractionalThreshold(threshold)
                            },
                            background = {
                                val direction =
                                    dismissState.dismissDirection ?: return@SwipeToDismiss
                                val color by animateColorAsState(
                                    when (dismissState.targetValue) {
                                        DismissValue.Default -> Color.Transparent
                                        DismissValue.DismissedToEnd -> MaterialTheme.colorScheme.secondary
                                        DismissValue.DismissedToStart,
                                        -> MaterialTheme.colorScheme.secondary
                                    },
                                )
                                val alignment = when (direction) {
                                    DismissDirection.StartToEnd -> Alignment.CenterStart
                                    DismissDirection.EndToStart -> Alignment.CenterEnd
                                }
                                val icon = when (direction) {
                                    DismissDirection.StartToEnd -> Icons.Default.ThumbDown
                                    DismissDirection.EndToStart -> Icons.Default.ThumbUp
                                }

                                Box(
                                    Modifier.fillMaxSize().background(color)
                                        .padding(horizontal = 20.dp),
                                    contentAlignment = alignment,
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = null,
                                    )
                                }
                            },
                        ) {
                            PostCard(
                                modifier = Modifier.onClick {
                                    navigator.push(
                                        PostDetailScreen(
                                            post = post,
                                            onBack = {
                                                navigator.pop()
                                            },
                                        ),
                                    )
                                },
                                post = post,
                                onOpenCommunity = { community ->
                                    navigator.push(
                                        CommunityDetailScreen(
                                            community = community,
                                            onBack = {
                                                navigator.pop()
                                            },
                                        ),
                                    )
                                },
                                onOpenCreator = { user ->
                                    navigator.push(
                                        UserDetailScreen(
                                            user = user,
                                            onBack = {
                                                navigator.pop()
                                            },
                                        ),
                                    )
                                },
                                onUpVote = {
                                    model.reduce(
                                        PostListMviModel.Intent.UpVotePost(
                                            post = post,
                                            feedback = true,
                                        ),
                                    )
                                },
                                onDownVote = {
                                    model.reduce(
                                        PostListMviModel.Intent.DownVotePost(
                                            post = post,
                                            feedback = true,
                                        ),
                                    )
                                },
                                onSave = {
                                    model.reduce(
                                        PostListMviModel.Intent.SavePost(
                                            post = post,
                                            feedback = true,
                                        ),
                                    )
                                },
                            )
                        }
                    }
                    item {
                        if (!uiState.loading && !uiState.refreshing && uiState.canFetchMore) {
                            model.reduce(PostListMviModel.Intent.LoadNextPage)
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
                }

                PullRefreshIndicator(
                    refreshing = uiState.refreshing,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter),
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}
