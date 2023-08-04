package com.github.diegoberaldin.raccoonforlemmy.core.commonui.postdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.diegoberaldin.racconforlemmy.core.utils.onClick
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.bindToLifecycle
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.communitydetail.CommunityDetailScreen
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.PostCardBody
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.PostCardFooter
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.PostCardImage
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.PostCardSubtitle
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.PostCardTitle
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.di.getPostDetailScreenViewModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.PostModel

class PostDetailScreen(
    private val post: PostModel,
    private val onBack: () -> Unit,
) : Screen {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val model = rememberScreenModel { getPostDetailScreenViewModel(post) }
        model.bindToLifecycle(key)
        val uiState by model.uiState.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        Scaffold(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface).padding(Spacing.xs),
            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        Image(
                            modifier = Modifier.onClick {
                                onBack()
                            },
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                        )
                    },
                )
            },
        ) { padding ->
            val post = uiState.post
            val pullRefreshState = rememberPullRefreshState(uiState.refreshing, {
                model.reduce(PostDetailScreenMviModel.Intent.Refresh)
            })
            Box(
                modifier = Modifier.pullRefresh(pullRefreshState),
            ) {
                LazyColumn(
                    modifier = Modifier.padding(padding),
                    verticalArrangement = Arrangement.spacedBy(Spacing.xs),
                ) {
                    item {
                        PostCardTitle(post)
                        PostCardSubtitle(
                            community = post.community,
                            creator = post.creator,
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
                        )
                        PostCardImage(post)
                        PostCardBody(
                            text = post.text,
                        )
                        PostCardFooter(
                            comments = post.comments,
                            score = post.score,
                            upVoted = post.myVote > 0,
                            downVoted = post.myVote < 0,
                            saved = post.saved,
                            onUpVote = {
                                model.reduce(PostDetailScreenMviModel.Intent.UpVotePost(it, post))
                            },
                            onDownVote = {
                                model.reduce(PostDetailScreenMviModel.Intent.DownVotePost(it, post))
                            },
                            onSave = {
                                model.reduce(PostDetailScreenMviModel.Intent.SavePost(it, post))
                            },
                        )
                    }
                    items(uiState.comments) { comment ->
                        CommentCard(
                            comment = comment,
                            onUpVote = {
                                model.reduce(
                                    PostDetailScreenMviModel.Intent.UpVoteComment(
                                        it,
                                        comment,
                                    ),
                                )
                            },
                            onDownVote = {
                                model.reduce(
                                    PostDetailScreenMviModel.Intent.DownVoteComment(
                                        it,
                                        comment,
                                    ),
                                )
                            },
                            onSave = {
                                model.reduce(
                                    PostDetailScreenMviModel.Intent.SaveComment(
                                        it,
                                        comment,
                                    ),
                                )
                            },
                        )
                    }
                    item {
                        if (!uiState.loading && !uiState.refreshing && uiState.canFetchMore) {
                            model.reduce(PostDetailScreenMviModel.Intent.LoadNextPage)
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
