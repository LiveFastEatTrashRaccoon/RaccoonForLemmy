package com.github.diegoberaldin.raccoonforlemmy.feature.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
import com.github.diegoberaldin.raccoonforlemmy.feature.home.di.getHomeScreenModel
import com.github.diegoberaldin.raccoonforlemmy.feature.home.viewmodel.HomeScreenMviModel

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
                                    model.reduce(HomeScreenMviModel.Intent.ChangeListing(it))
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
                                    model.reduce(HomeScreenMviModel.Intent.ChangeSort(it))
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
                model.reduce(HomeScreenMviModel.Intent.Refresh)
            })
            Box(
                modifier = Modifier.padding(padding).pullRefresh(pullRefreshState),
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(Spacing.xs),
                ) {
                    items(uiState.posts) { post ->
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
                            onUpVote = {
                                model.reduce(HomeScreenMviModel.Intent.UpVotePost(it, post))
                            },
                            onDownVote = {
                                model.reduce(HomeScreenMviModel.Intent.DownVotePost(it, post))
                            },
                            onSave = {
                                model.reduce(HomeScreenMviModel.Intent.SavePost(it, post))
                            },
                        )
                    }
                    item {
                        if (!uiState.loading && !uiState.refreshing && uiState.canFetchMore) {
                            model.reduce(HomeScreenMviModel.Intent.LoadNextPage)
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
