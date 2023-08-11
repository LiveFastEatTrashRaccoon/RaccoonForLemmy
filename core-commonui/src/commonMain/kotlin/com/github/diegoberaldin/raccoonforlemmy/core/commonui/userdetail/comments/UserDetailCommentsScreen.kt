package com.github.diegoberaldin.raccoonforlemmy.core.commonui.userdetail.comments

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.bindToLifecycle
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.UserCounters
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.UserHeader
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.di.getUserCommentsViewModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.postdetail.CommentCard
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.userdetail.SectionSelector
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.userdetail.UserDetailSection
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.UserModel

internal class UserDetailCommentsScreen(
    private val modifier: Modifier = Modifier,
    private val user: UserModel,
    private val onSectionSelected: (UserDetailSection) -> Unit,
) : Screen {
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val model = rememberScreenModel(
            user.id.toString(),
        ) { getUserCommentsViewModel(user) }
        model.bindToLifecycle(key)
        val uiState by model.uiState.collectAsState()

        val pullRefreshState = rememberPullRefreshState(uiState.refreshing, {
            model.reduce(UserCommentsMviModel.Intent.Refresh)
        })
        Box(
            modifier = modifier.pullRefresh(pullRefreshState),
        ) {
            var width by remember { mutableStateOf(0f) }
            LazyColumn(
                modifier = Modifier.fillMaxSize().onGloballyPositioned {
                    width = it.size.toSize().width
                },
                verticalArrangement = Arrangement.spacedBy(Spacing.xs),
            ) {
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(Spacing.xs),
                    ) {
                        UserHeader(user = uiState.user)
                        UserCounters(user = uiState.user)
                        Spacer(modifier = Modifier.height(Spacing.xxs))
                        SectionSelector(
                            currentSection = UserDetailSection.COMMENTS,
                            onSectionSelected = {
                                onSectionSelected(it)
                            },
                        )
                    }
                }
                items(uiState.comments, key = { it.id.toString() + it.myVote }) { comment ->
                    val dismissState = rememberDismissState(
                        confirmStateChange = {
                            when (it) {
                                DismissValue.DismissedToEnd -> {
                                    model.reduce(
                                        UserCommentsMviModel.Intent.DownVoteComment(
                                            comment = comment,
                                        ),
                                    )
                                }

                                DismissValue.DismissedToStart -> {
                                    model.reduce(
                                        UserCommentsMviModel.Intent.UpVoteComment(
                                            comment = comment,
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
                            model.reduce(UserCommentsMviModel.Intent.HapticIndication)
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
                                DismissDirection.StartToEnd -> Icons.Default.ArrowCircleDown
                                DismissDirection.EndToStart -> Icons.Default.ArrowCircleUp
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
                        CommentCard(
                            comment = comment,
                            onSave = {
                                model.reduce(
                                    UserCommentsMviModel.Intent.SaveComment(
                                        comment = comment,
                                        feedback = true,
                                    ),
                                )
                            },
                            onUpVote = {
                                model.reduce(
                                    UserCommentsMviModel.Intent.UpVoteComment(
                                        comment = comment,
                                        feedback = true,
                                    ),
                                )
                            },
                            onDownVote = {
                                model.reduce(
                                    UserCommentsMviModel.Intent.DownVoteComment(
                                        comment = comment,
                                        feedback = true,
                                    ),
                                )
                            },
                        )
                    }
                }
                item {
                    if (!uiState.loading && !uiState.refreshing && uiState.canFetchMore) {
                        model.reduce(UserCommentsMviModel.Intent.LoadNextPage)
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
