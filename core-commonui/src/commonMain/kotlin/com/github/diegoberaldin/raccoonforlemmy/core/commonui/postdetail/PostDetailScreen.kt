package com.github.diegoberaldin.raccoonforlemmy.core.commonui.postdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.diegoberaldin.racconforlemmy.core.utils.onClick
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.di.getThemeRepository
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.CornerSize
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.bindToLifecycle
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.communitydetail.CommunityDetailScreen
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.PostCardBody
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.PostCardFooter
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.PostCardImage
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.PostCardSubtitle
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.PostCardTitle
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.SwipeableCard
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.createcomment.CreateCommentScreen
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.di.getPostDetailViewModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.modals.SortBottomSheet
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.userdetail.UserDetailScreen
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.PostModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.SortType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.toIcon
import com.github.diegoberaldin.raccoonforlemmy.resources.MR
import dev.icerock.moko.resources.compose.stringResource

class PostDetailScreen(
    private val post: PostModel,
    private val onBack: () -> Unit,
) : Screen {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val model = rememberScreenModel { getPostDetailViewModel(post) }
        model.bindToLifecycle(key)
        val uiState by model.uiState.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        val bottomSheetNavigator = LocalBottomSheetNavigator.current

        Scaffold(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface).padding(Spacing.xs),
            topBar = {
                TopAppBar(
                    title = {},
                    actions = {
                        Image(
                            modifier = Modifier.onClick {
                                bottomSheetNavigator.show(
                                    SortBottomSheet(
                                        values = listOf(
                                            SortType.Hot,
                                            SortType.Top.Generic,
                                            SortType.New,
                                            SortType.Old,
                                        ),
                                        onSelected = {
                                            model.reduce(PostDetailMviModel.Intent.ChangeSort(it))
                                        },
                                        onHide = {
                                            bottomSheetNavigator.hide()
                                        },
                                    ),
                                )
                            },
                            imageVector = uiState.sortType.toIcon(),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                        )
                    },
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
            floatingActionButton = {
                FloatingActionButton(
                    shape = CircleShape,
                    backgroundColor = MaterialTheme.colorScheme.secondary,
                    onClick = {
                        bottomSheetNavigator.show(
                            CreateCommentScreen(
                                originalPost = post,
                                onCommentCreated = {
                                    bottomSheetNavigator.hide()
                                    model.reduce(PostDetailMviModel.Intent.Refresh)
                                }
                            )
                        )
                    },
                    content = {
                        Icon(
                            imageVector = Icons.Default.Reply,
                            contentDescription = null,
                        )
                    },
                )
            }
        ) { padding ->
            val post = uiState.post
            val pullRefreshState = rememberPullRefreshState(uiState.refreshing, {
                model.reduce(PostDetailMviModel.Intent.Refresh)
            })
            Box(
                modifier = Modifier.pullRefresh(pullRefreshState),
            ) {
                LazyColumn(
                    modifier = Modifier.padding(padding),
                    verticalArrangement = Arrangement.spacedBy(Spacing.xs),
                ) {
                    item {
                        val themeRepository = remember { getThemeRepository() }
                        val fontScale by themeRepository.contentFontScale.collectAsState()
                        CompositionLocalProvider(
                            LocalDensity provides Density(
                                density = LocalDensity.current.density,
                                fontScale = fontScale,
                            ),
                        ) {
                            Card(
                                modifier = Modifier.background(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(CornerSize.m),
                                ).padding(
                                    vertical = Spacing.lHalf,
                                    horizontal = Spacing.s,
                                ),
                            ) {
                                PostCardTitle(post)
                                PostCardSubtitle(
                                    community = post.community,
                                    creator = post.creator?.copy(avatar = null),
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
                                )
                                PostCardImage(
                                    post = post,
                                    blurNsfw = false,
                                )
                                PostCardBody(
                                    text = post.text,
                                )
                                PostCardFooter(
                                    comments = post.comments,
                                    score = post.score,
                                    upVoted = post.myVote > 0,
                                    downVoted = post.myVote < 0,
                                    saved = post.saved,
                                    date = post.publishDate,
                                    onUpVote = {
                                        model.reduce(
                                            PostDetailMviModel.Intent.UpVotePost(
                                                post = post,
                                                feedback = true,
                                            ),
                                        )
                                    },
                                    onDownVote = {
                                        model.reduce(
                                            PostDetailMviModel.Intent.DownVotePost(
                                                post = post,
                                                feedback = true,
                                            ),
                                        )
                                    },
                                    onSave = {
                                        model.reduce(
                                            PostDetailMviModel.Intent.SavePost(
                                                post = post,
                                                feedback = true,
                                            ),
                                        )
                                    },
                                )
                            }
                        }
                    }
                    itemsIndexed(uiState.comments) { idx, comment ->
                        Column {
                            SwipeableCard(
                                modifier = Modifier.fillMaxWidth(),
                                backgroundColor = {
                                    when (it) {
                                        DismissValue.DismissedToStart -> MaterialTheme.colorScheme.secondary
                                        DismissValue.DismissedToEnd -> MaterialTheme.colorScheme.tertiary
                                        DismissValue.Default -> Color.Transparent
                                    }
                                },
                                onGestureBegin = {
                                    model.reduce(PostDetailMviModel.Intent.HapticIndication)
                                },
                                onDismissToStart = {
                                    model.reduce(
                                        PostDetailMviModel.Intent.UpVoteComment(
                                            comment = comment,
                                        ),
                                    )
                                },
                                onDismissToEnd = {
                                    model.reduce(
                                        PostDetailMviModel.Intent.DownVoteComment(
                                            comment = comment,
                                        ),
                                    )
                                },
                                swipeContent = { direction ->
                                    val icon = when (direction) {
                                        DismissDirection.StartToEnd -> Icons.Default.ArrowCircleDown
                                        DismissDirection.EndToStart -> Icons.Default.ArrowCircleUp
                                    }
                                    val (iconModifier, iconTint) = when {
                                        direction == DismissDirection.StartToEnd && post.myVote < 0 -> {
                                            Modifier.background(
                                                color = Color.Transparent,
                                                shape = CircleShape,
                                            ) to MaterialTheme.colorScheme.onTertiary
                                        }

                                        direction == DismissDirection.StartToEnd -> {
                                            Modifier.background(
                                                color = MaterialTheme.colorScheme.onTertiary,
                                                shape = CircleShape,
                                            ) to MaterialTheme.colorScheme.tertiary
                                        }

                                        direction == DismissDirection.EndToStart && post.myVote > 0 -> {
                                            Modifier.background(
                                                color = Color.Transparent,
                                                shape = CircleShape,
                                            ) to MaterialTheme.colorScheme.onSecondary
                                        }

                                        else -> {
                                            Modifier.background(
                                                color = MaterialTheme.colorScheme.onSecondary,
                                                shape = CircleShape,
                                            ) to MaterialTheme.colorScheme.secondary
                                        }
                                    }
                                    Icon(
                                        modifier = iconModifier,
                                        imageVector = icon,
                                        contentDescription = null,
                                        tint = iconTint,
                                    )
                                },
                                content = {
                                    CommentCard(
                                        comment = comment,
                                        onUpVote = {
                                            model.reduce(
                                                PostDetailMviModel.Intent.UpVoteComment(
                                                    comment = comment,
                                                    feedback = true,
                                                ),
                                            )
                                        },
                                        onDownVote = {
                                            model.reduce(
                                                PostDetailMviModel.Intent.DownVoteComment(
                                                    comment = comment,
                                                    feedback = true,
                                                ),
                                            )
                                        },
                                        onSave = {
                                            model.reduce(
                                                PostDetailMviModel.Intent.SaveComment(
                                                    comment = comment,
                                                    feedback = true,
                                                ),
                                            )
                                        },
                                        onReply = {
                                            bottomSheetNavigator.show(
                                                CreateCommentScreen(
                                                    originalPost = post,
                                                    originalComment = comment,
                                                    onCommentCreated = {
                                                        bottomSheetNavigator.hide()
                                                        model.reduce(PostDetailMviModel.Intent.Refresh)
                                                    }
                                                )
                                            )
                                        }
                                    )
                                },
                            )
                            if ((comment.comments ?: 0) > 0
                                && comment.depth == PostDetailViewModel.COMMENT_DEPTH
                                && (idx < uiState.comments.lastIndex && uiState.comments[idx + 1].depth < comment.depth)
                            ) {
                                Row {
                                    Spacer(modifier = Modifier.weight(1f))
                                    Button(
                                        onClick = {
                                            model.reduce(
                                                PostDetailMviModel.Intent.FetchMoreComments(
                                                    parentId = comment.id
                                                )
                                            )
                                        }) {
                                        Text(
                                            text = stringResource(MR.strings.post_detail_load_more_comments),
                                            style = MaterialTheme.typography.labelMedium,
                                        )
                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                    item {
                        if (!uiState.loading && !uiState.refreshing && uiState.canFetchMore) {
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
                    item {
                        Spacer(modifier = Modifier.height(Spacing.s))
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
