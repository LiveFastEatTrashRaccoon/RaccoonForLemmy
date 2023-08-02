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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SpaceDashboard
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.github.diegoberaldin.racconforlemmy.core.utils.onClick
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.bindToLifecycle
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.modals.ListingTypeBottomSheet
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.modals.SortBottomSheet
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.postdetail.PostDetailScreen
import com.github.diegoberaldin.raccoonforlemmy.feature.home.di.getHomeScreenModel
import com.github.diegoberaldin.raccoonforlemmy.feature.home.viewmodel.HomeScreenMviModel
import com.github.diegoberaldin.raccoonforlemmy.resources.MR
import com.github.diegoberaldin.raccoonforlemmy.resources.di.getLanguageRepository
import com.github.diegoberaldin.raccoonforlemmy.resources.di.staticString
import dev.icerock.moko.resources.desc.desc

object HomeTab : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.SpaceDashboard)
            val languageRepository = remember { getLanguageRepository() }
            val lang by languageRepository.currentLanguage.collectAsState()
            return remember(lang) {
                val title = staticString(MR.strings.navigation_home.desc())
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon,
                )
            }
        }

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val model = rememberScreenModel { getHomeScreenModel() }
        model.bindToLifecycle(key)
        val uiState by model.uiState.collectAsState()
        val bottomSheetNavigator = LocalBottomSheetNavigator.current

        Scaffold(
            modifier = Modifier.padding(Spacing.xxs),
            topBar = {
                PostsTopBar(
                    currentInstance = uiState.instance,
                    listingType = uiState.listingType,
                    sortType = uiState.sortType,
                    onSelectListingType = {
                        bottomSheetNavigator.show(
                            ListingTypeBottomSheet(isLogged = uiState.isLogged) {
                                model.reduce(HomeScreenMviModel.Intent.ChangeListing(it))
                            },
                        )
                    },
                    onSelectSortType = {
                        bottomSheetNavigator.show(
                            SortBottomSheet {
                                model.reduce(HomeScreenMviModel.Intent.ChangeSort(it))
                            },
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
                                bottomSheetNavigator.show(PostDetailScreen(post))
                            },
                            post = post,
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
