package com.github.diegoberaldin.raccoonforlemmy.core.commonui.communitydetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.github.diegoberaldin.racconforlemmy.core.utils.onClick
import com.github.diegoberaldin.racconforlemmy.core.utils.toLocalPixel
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.bindToLifecycle
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.di.getCommunityDetailScreenViewModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.postdetail.PostDetailScreen
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommunityModel
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

class CommunityDetailScreen(
    private val community: CommunityModel,
) : Screen {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val model = rememberScreenModel { getCommunityDetailScreenViewModel(community) }
        model.bindToLifecycle(key)
        val uiState by model.uiState.collectAsState()
        val bottomSheetNavigator = LocalBottomSheetNavigator.current

        Scaffold(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface).padding(Spacing.xs),
            topBar = {
                val communityName = community.name
                val communityHost = community.host
                TopAppBar(
                    title = {
                        Text(
                            modifier = Modifier.padding(horizontal = Spacing.s),
                            text = buildString {
                                append(communityName)
                                if (communityHost.isNotEmpty()) {
                                    append("@$communityHost")
                                }
                            },
                        )
                    },
                    navigationIcon = {
                        Image(
                            modifier = Modifier.onClick {
                                bottomSheetNavigator.hide()
                            },
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                        )
                    },
                )
            },
        ) { padding ->
            val community = uiState.community
            val pullRefreshState = rememberPullRefreshState(uiState.refreshing, {
                model.reduce(CommunityDetailScreenMviModel.Intent.Refresh)
            })
            Box(
                modifier = Modifier.pullRefresh(pullRefreshState),
            ) {
                LazyColumn(
                    modifier = Modifier.padding(padding),
                    verticalArrangement = Arrangement.spacedBy(Spacing.xs),
                ) {
                    item {
                        val communityIcon = community.icon.orEmpty()
                        val communityTitle = community.title

                        val iconSize = 50.dp
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                val banner = community.banner.orEmpty()
                                if (banner.isNotEmpty()) {
                                    val painterResource = asyncPainterResource(banner)
                                    KamelImage(
                                        modifier = Modifier.fillMaxWidth().aspectRatio(2.25f),
                                        resource = painterResource,
                                        contentScale = ContentScale.FillBounds,
                                        contentDescription = null,
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier.fillMaxWidth().aspectRatio(2.5f),
                                    )
                                }
                                Column(
                                    modifier = Modifier.graphicsLayer(translationY = -(iconSize / 2).toLocalPixel()),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(Spacing.xxs),
                                ) {
                                    if (communityIcon.isNotEmpty()) {
                                        val painterResource =
                                            asyncPainterResource(data = communityIcon)
                                        KamelImage(
                                            modifier = Modifier.padding(Spacing.xxxs).size(iconSize)
                                                .clip(RoundedCornerShape(iconSize / 2)),
                                            resource = painterResource,
                                            contentDescription = null,
                                            contentScale = ContentScale.FillBounds,
                                        )
                                    } else {
                                        Box(
                                            modifier = Modifier.padding(Spacing.xxxs)
                                                .size(iconSize)
                                                .background(
                                                    color = MaterialTheme.colorScheme.primary,
                                                    shape = RoundedCornerShape(iconSize / 2),
                                                ),
                                            contentAlignment = Alignment.Center,
                                        ) {
                                            Text(
                                                text = community.name.firstOrNull()?.toString()
                                                    .orEmpty()
                                                    .uppercase(),
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = MaterialTheme.colorScheme.onPrimary,
                                            )
                                        }
                                    }
                                    Text(
                                        text = buildString {
                                            append(communityTitle)
                                        },
                                        style = MaterialTheme.typography.headlineSmall,
                                    )
                                }
                            }
                        }
                    }
                    items(uiState.posts) { post ->
                        PostCard(
                            modifier = Modifier.onClick {
                                bottomSheetNavigator.show(PostDetailScreen(post))
                            },
                            post = post,
                            onUpVote = {
                                model.reduce(
                                    CommunityDetailScreenMviModel.Intent.UpVotePost(
                                        it,
                                        post,
                                    ),
                                )
                            },
                            onDownVote = {
                                model.reduce(
                                    CommunityDetailScreenMviModel.Intent.DownVotePost(
                                        it,
                                        post,
                                    ),
                                )
                            },
                            onSave = {
                                model.reduce(
                                    CommunityDetailScreenMviModel.Intent.SavePost(
                                        it,
                                        post,
                                    ),
                                )
                            },
                        )
                    }
                    item {
                        if (!uiState.loading && !uiState.refreshing && uiState.canFetchMore) {
                            model.reduce(CommunityDetailScreenMviModel.Intent.LoadNextPage)
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
