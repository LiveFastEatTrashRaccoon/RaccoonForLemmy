package com.github.diegoberaldin.raccoonforlemmy.core.commonui.userdetail.comments

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.github.diegoberaldin.racconforlemmy.core.utils.toLocalPixel
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.bindToLifecycle
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.SectionSelector
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.SwipeableCard
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.UserCounters
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.UserHeader
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.createcomment.CreateCommentScreen
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.di.getUserCommentsViewModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.postdetail.CommentCard
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.userdetail.UserDetailSection
import com.github.diegoberaldin.raccoonforlemmy.core.notifications.di.getNotificationCenter
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.PostModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.SortType
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.UserModel
import com.github.diegoberaldin.raccoonforlemmy.resources.MR
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class UserDetailCommentsScreen(
    private val user: UserModel,
    private val parentKey: String,
) : Tab {

    override val options: TabOptions
        @Composable get() {
            return TabOptions(1u, "")
        }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val model = rememberScreenModel(
            user.id.toString(),
        ) { getUserCommentsViewModel(user) }
        model.bindToLifecycle(key)
        val uiState by model.uiState.collectAsState()
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val notificationCenter = remember { getNotificationCenter() }
        DisposableEffect(key) {
            notificationCenter.addObserver({
                (it as? SortType)?.also { sortType ->
                    model.reduce(UserCommentsMviModel.Intent.ChangeSort(sortType))
                }
            }, key, parentKey)
            onDispose {
                notificationCenter.removeObserver(key)
            }
        }

        val pullRefreshState = rememberPullRefreshState(uiState.refreshing, {
            model.reduce(UserCommentsMviModel.Intent.Refresh)
        })
        Box(
            modifier = Modifier.pullRefresh(pullRefreshState),
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(Spacing.xs),
            ) {
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(Spacing.xs),
                    ) {
                        UserHeader(user = uiState.user)
                        UserCounters(
                            modifier = Modifier.graphicsLayer(translationY = -Spacing.m.toLocalPixel()),
                            user = uiState.user,
                        )
                        SectionSelector(
                            titles = listOf(
                                stringResource(MR.strings.profile_section_posts),
                                stringResource(MR.strings.profile_section_comments),
                            ),
                            currentSection = 1,
                            onSectionSelected = {
                                val section = when (it) {
                                    0 -> UserDetailSection.POSTS
                                    else -> UserDetailSection.COMMENTS
                                }
                                notificationCenter.getObserver(key)?.also { obsever ->
                                    obsever.invoke(section)
                                }
                            },
                        )
                    }
                }
                itemsIndexed(uiState.comments) { idx, comment ->
                    SwipeableCard(
                        modifier = Modifier.fillMaxWidth(),
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
                            val (iconModifier, iconTint) = when {
                                direction == DismissDirection.StartToEnd && comment.myVote < 0 -> {
                                    Modifier.background(
                                        color = Color.Transparent,
                                        shape = CircleShape,
                                    ) to MaterialTheme.colorScheme.onSecondary
                                }

                                direction == DismissDirection.StartToEnd -> {
                                    Modifier.background(
                                        color = MaterialTheme.colorScheme.onTertiary,
                                        shape = CircleShape,
                                    ) to MaterialTheme.colorScheme.tertiary
                                }

                                direction == DismissDirection.EndToStart && comment.myVote > 0 -> {
                                    Modifier.background(
                                        color = Color.Transparent,
                                        shape = CircleShape,
                                    ) to MaterialTheme.colorScheme.onTertiary
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
                        onGestureBegin = {
                            model.reduce(UserCommentsMviModel.Intent.HapticIndication)
                        },
                        onDismissToStart = {
                            model.reduce(
                                UserCommentsMviModel.Intent.UpVoteComment(idx),
                            )
                        },
                        onDismissToEnd = {
                            model.reduce(
                                UserCommentsMviModel.Intent.DownVoteComment(idx),
                            )
                        },
                        content = {
                            CommentCard(
                                comment = comment,
                                onSave = {
                                    model.reduce(
                                        UserCommentsMviModel.Intent.SaveComment(
                                            index = idx,
                                            feedback = true,
                                        ),
                                    )
                                },
                                onUpVote = {
                                    model.reduce(
                                        UserCommentsMviModel.Intent.UpVoteComment(
                                            index = idx,
                                            feedback = true,
                                        ),
                                    )
                                },
                                onDownVote = {
                                    model.reduce(
                                        UserCommentsMviModel.Intent.DownVoteComment(
                                            index = idx,
                                            feedback = true,
                                        ),
                                    )
                                },
                                onReply = {
                                    val screen = CreateCommentScreen(
                                        originalPost = Json.encodeToString(PostModel(id = comment.postId)),
                                        originalComment = Json.encodeToString(comment),
                                    )
                                    notificationCenter.addObserver({
                                        model.reduce(UserCommentsMviModel.Intent.Refresh)
                                    }, key, screen.key)
                                    bottomSheetNavigator.show(screen)
                                }
                            )
                        },
                    )
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
                item {
                    Spacer(modifier = Modifier.height(Spacing.xxxl))
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
