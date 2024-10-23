package com.livefast.eattrash.raccoonforlemmy.unit.myaccount

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.PostLayout
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.CornerSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.ProgressHud
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.SectionSelector
import com.livefast.eattrash.raccoonforlemmy.core.commonui.detailopener.api.getDetailOpener
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.CommentCard
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.CommentCardPlaceholder
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.Option
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.OptionId
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.PostCard
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.PostCardPlaceholder
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.ProfileLoggedSection
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.UserHeader
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.ShareBottomSheet
import com.livefast.eattrash.raccoonforlemmy.core.l10n.messages.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.TabNavigationSection
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.notifications.di.getNotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.persistence.di.getSettingsRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommentModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.readableHandle
import com.livefast.eattrash.raccoonforlemmy.unit.rawcontent.RawContentDialog
import com.livefast.eattrash.raccoonforlemmy.unit.zoomableimage.ZoomableImageScreen
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

private object AuthIssueAnnotations {
    const val ACTION_REFRESH = "refresh"
    const val ACTION_LOGIN = "login"
}

object ProfileLoggedScreen : Tab {
    override val options: TabOptions
        @Composable get() {
            return TabOptions(0u, "")
        }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val model = getScreenModel<ProfileLoggedMviModel>()
        val uiState by model.uiState.collectAsState()
        val notificationCenter = remember { getNotificationCenter() }
        val navigationCoordinator = remember { getNavigationCoordinator() }
        val lazyListState = rememberLazyListState()
        var rawContent by remember { mutableStateOf<Any?>(null) }
        val detailOpener = remember { getDetailOpener() }
        val settingsRepository = remember { getSettingsRepository() }
        val settings by settingsRepository.currentSettings.collectAsState()
        var postIdToDelete by remember { mutableStateOf<Long?>(null) }
        var commentIdToDelete by remember { mutableStateOf<Long?>(null) }

        LaunchedEffect(navigationCoordinator) {
            navigationCoordinator.onDoubleTabSelection
                .onEach { section ->
                    runCatching {
                        if (section == TabNavigationSection.Profile) {
                            lazyListState.scrollToItem(0)
                        }
                    }
                }.launchIn(this)
        }
        LaunchedEffect(notificationCenter) {
            notificationCenter
                .subscribe(NotificationCenterEvent.PostCreated::class)
                .onEach {
                    model.reduce(ProfileLoggedMviModel.Intent.Refresh)
                }.launchIn(this)

            notificationCenter
                .subscribe(NotificationCenterEvent.CommentCreated::class)
                .onEach {
                    model.reduce(ProfileLoggedMviModel.Intent.Refresh)
                }.launchIn(this)
        }

        if (uiState.initial) {
            ProgressHud()
            return
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(Spacing.s),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PullToRefreshBox(
                isRefreshing = uiState.refreshing,
                onRefresh = {
                    model.reduce(ProfileLoggedMviModel.Intent.Refresh)
                },
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = lazyListState,
                ) {
                    if (uiState.user == null) {
                        item {
                            Text(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = Spacing.s),
                                textAlign = TextAlign.Center,
                                text = LocalStrings.current.messageAuthIssue,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onBackground,
                            )

                            val annotatedString =
                                buildAnnotatedString {
                                    val linkStyle =
                                        SpanStyle(
                                            color = MaterialTheme.colorScheme.primary,
                                            textDecoration = TextDecoration.Underline,
                                        )
                                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                                        append(LocalStrings.current.messageAuthIssueSegue0)
                                        append("\n• ")
                                        pushLink(
                                            LinkAnnotation.Clickable(
                                                tag = AuthIssueAnnotations.ACTION_REFRESH,
                                                linkInteractionListener = {
                                                    model.reduce(ProfileLoggedMviModel.Intent.Refresh)
                                                },
                                            ),
                                        )
                                        withStyle(linkStyle) {
                                            append(LocalStrings.current.messageAuthIssueSegue1)
                                        }
                                        pop()
                                        append("\n• ")
                                        pushLink(
                                            LinkAnnotation.Clickable(
                                                tag = AuthIssueAnnotations.ACTION_LOGIN,
                                                linkInteractionListener = {
                                                    notificationCenter.send(
                                                        NotificationCenterEvent.ProfileSideMenuAction.Logout,
                                                    )
                                                },
                                            ),
                                        )
                                        withStyle(linkStyle) {
                                            append(LocalStrings.current.messageAuthIssueSegue2)
                                        }
                                        pop()
                                        append("\n• ")
                                        append(LocalStrings.current.messageAuthIssueSegue3)
                                    }
                                }
                            Box(
                                modifier =
                                    Modifier
                                        .padding(
                                            vertical = Spacing.m,
                                            horizontal = Spacing.s,
                                        ).border(
                                            width = 1.dp,
                                            color = MaterialTheme.colorScheme.onBackground,
                                            shape = RoundedCornerShape(CornerSize.l),
                                        ).padding(
                                            vertical = Spacing.s,
                                            horizontal = Spacing.m,
                                        ),
                            ) {
                                Text(
                                    modifier =
                                        Modifier.fillMaxWidth(),
                                    text = annotatedString,
                                    style = MaterialTheme.typography.bodyLarge,
                                )
                            }
                        }
                    } else {
                        item {
                            uiState.user?.also { user ->
                                UserHeader(
                                    user = user,
                                    autoLoadImages = uiState.autoLoadImages,
                                    onOpenImage = { url ->
                                        navigationCoordinator.pushScreen(
                                            ZoomableImageScreen(
                                                url = url,
                                                source = user.readableHandle,
                                            ),
                                        )
                                    },
                                )
                            }
                        }
                        item {
                            SectionSelector(
                                modifier = Modifier.padding(bottom = Spacing.s),
                                titles =
                                    listOf(
                                        LocalStrings.current.profileSectionPosts,
                                        LocalStrings.current.profileSectionComments,
                                    ),
                                currentSection =
                                    when (uiState.section) {
                                        ProfileLoggedSection.Comments -> 1
                                        else -> 0
                                    },
                                onSectionSelected = { idx ->
                                    val section =
                                        when (idx) {
                                            1 -> ProfileLoggedSection.Comments
                                            else -> ProfileLoggedSection.Posts
                                        }
                                    model.reduce(
                                        ProfileLoggedMviModel.Intent.ChangeSection(section),
                                    )
                                },
                            )
                        }
                        if (uiState.section == ProfileLoggedSection.Posts) {
                            if (uiState.posts.isEmpty() && uiState.loading && !uiState.initial) {
                                items(5) {
                                    PostCardPlaceholder(
                                        modifier = Modifier.padding(horizontal = Spacing.xs),
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
                                key = { it.id.toString() + (it.updateDate ?: it.publishDate) },
                            ) { post ->
                                PostCard(
                                    post = post,
                                    postLayout = uiState.postLayout,
                                    limitBodyHeight = true,
                                    fullHeightImage = uiState.fullHeightImages,
                                    fullWidthImage = uiState.fullWidthImages,
                                    voteFormat = uiState.voteFormat,
                                    autoLoadImages = uiState.autoLoadImages,
                                    preferNicknames = uiState.preferNicknames,
                                    showScores = uiState.showScores,
                                    showUnreadComments = uiState.showUnreadComments,
                                    hideAuthor = true,
                                    blurNsfw = false,
                                    downVoteEnabled = uiState.downVoteEnabled,
                                    onClick = {
                                        model.reduce(ProfileLoggedMviModel.Intent.WillOpenDetail)
                                        detailOpener.openPostDetail(post)
                                    },
                                    onReply = {
                                        model.reduce(ProfileLoggedMviModel.Intent.WillOpenDetail)
                                        detailOpener.openPostDetail(post)
                                    },
                                    onOpenCommunity = { community, instance ->
                                        detailOpener.openCommunityDetail(community, instance)
                                    },
                                    onOpenCreator = { user, instance ->
                                        detailOpener.openUserDetail(user, instance)
                                    },
                                    onOpenImage = { url ->
                                        navigationCoordinator.pushScreen(
                                            ZoomableImageScreen(
                                                url = url,
                                                source = post.community?.readableHandle.orEmpty(),
                                            ),
                                        )
                                    },
                                    onUpVote = {
                                        model.reduce(
                                            ProfileLoggedMviModel.Intent.UpVotePost(
                                                id = post.id,
                                            ),
                                        )
                                    },
                                    onDownVote = {
                                        model.reduce(
                                            ProfileLoggedMviModel.Intent.DownVotePost(
                                                id = post.id,
                                            ),
                                        )
                                    },
                                    onSave = {
                                        model.reduce(
                                            ProfileLoggedMviModel.Intent.SavePost(
                                                id = post.id,
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
                                                    OptionId.CrossPost,
                                                    LocalStrings.current.postActionCrossPost,
                                                )
                                            this +=
                                                Option(
                                                    OptionId.SeeRaw,
                                                    LocalStrings.current.postActionSeeRaw,
                                                )
                                            this +=
                                                Option(
                                                    OptionId.Edit,
                                                    LocalStrings.current.postActionEdit,
                                                )
                                            if (post.deleted) {
                                                this +=
                                                    Option(
                                                        OptionId.Restore,
                                                        LocalStrings.current.actionRestore,
                                                    )
                                            } else {
                                                this +=
                                                    Option(
                                                        OptionId.Delete,
                                                        LocalStrings.current.commentActionDelete,
                                                    )
                                            }
                                        },
                                    onOptionSelected = { optionId ->
                                        when (optionId) {
                                            OptionId.Delete -> {
                                                postIdToDelete = post.id
                                            }

                                            OptionId.Edit -> {
                                                detailOpener.openCreatePost(
                                                    editedPost = post,
                                                )
                                            }

                                            OptionId.SeeRaw -> {
                                                rawContent = post
                                            }

                                            OptionId.CrossPost -> {
                                                detailOpener.openCreatePost(
                                                    crossPost = post,
                                                    forceCommunitySelection = true,
                                                )
                                            }

                                            OptionId.Share -> {
                                                val urls =
                                                    listOfNotNull(
                                                        post.originalUrl,
                                                        "https://${uiState.instance}/post/${post.id}",
                                                    ).distinct()
                                                if (urls.size == 1) {
                                                    model.reduce(
                                                        ProfileLoggedMviModel.Intent.Share(
                                                            urls.first(),
                                                        ),
                                                    )
                                                } else {
                                                    val screen = ShareBottomSheet(urls = urls)
                                                    navigationCoordinator.showBottomSheet(screen)
                                                }
                                            }

                                            OptionId.Restore -> {
                                                model.reduce(
                                                    ProfileLoggedMviModel.Intent.RestorePost(
                                                        post.id,
                                                    ),
                                                )
                                            }

                                            else -> Unit
                                        }
                                    },
                                )
                                if (uiState.postLayout != PostLayout.Card) {
                                    HorizontalDivider(modifier = Modifier.padding(vertical = Spacing.interItem))
                                } else {
                                    Spacer(modifier = Modifier.height(Spacing.interItem))
                                }
                            }

                            if (uiState.posts.isEmpty() && !uiState.loading && !uiState.initial) {
                                item {
                                    Text(
                                        modifier =
                                            Modifier
                                                .fillMaxWidth()
                                                .padding(top = Spacing.xs),
                                        textAlign = TextAlign.Center,
                                        text = LocalStrings.current.messageEmptyList,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onBackground,
                                    )
                                }
                            }
                        } else {
                            if (uiState.comments.isEmpty() && uiState.loading && uiState.initial) {
                                items(5) {
                                    CommentCardPlaceholder(
                                        modifier = Modifier.padding(horizontal = Spacing.xs),
                                        hideAuthor = true,
                                    )
                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical = Spacing.xxxs),
                                        thickness = 0.25.dp,
                                    )
                                }
                            }
                            items(
                                items = uiState.comments,
                                key = { it.id.toString() + (it.updateDate ?: it.publishDate) },
                            ) { comment ->
                                CommentCard(
                                    modifier = Modifier.background(MaterialTheme.colorScheme.background),
                                    comment = comment,
                                    voteFormat = uiState.voteFormat,
                                    autoLoadImages = uiState.autoLoadImages,
                                    showScores = uiState.showScores,
                                    hideCommunity = false,
                                    hideAuthor = true,
                                    indentAmount = 0,
                                    downVoteEnabled = uiState.downVoteEnabled,
                                    onImageClick = { url ->
                                        navigationCoordinator.pushScreen(
                                            ZoomableImageScreen(
                                                url = url,
                                                source = comment.community?.readableHandle.orEmpty(),
                                            ),
                                        )
                                    },
                                    onOpenCommunity = { community, instance ->
                                        detailOpener.openCommunityDetail(community, instance)
                                    },
                                    onClick = {
                                        detailOpener.openPostDetail(
                                            post = PostModel(id = comment.postId),
                                            highlightCommentId = comment.id,
                                        )
                                    },
                                    onReply = {
                                        detailOpener.openReply(
                                            originalPost = PostModel(id = comment.postId),
                                            originalComment = comment,
                                        )
                                    },
                                    onUpVote = {
                                        model.reduce(
                                            ProfileLoggedMviModel.Intent.UpVoteComment(
                                                id = comment.id,
                                            ),
                                        )
                                    },
                                    onDownVote = {
                                        model.reduce(
                                            ProfileLoggedMviModel.Intent.DownVoteComment(
                                                id = comment.id,
                                            ),
                                        )
                                    },
                                    onSave = {
                                        model.reduce(
                                            ProfileLoggedMviModel.Intent.SaveComment(
                                                id = comment.id,
                                            ),
                                        )
                                    },
                                    options =
                                        buildList {
                                            this +=
                                                Option(
                                                    OptionId.SeeRaw,
                                                    LocalStrings.current.postActionSeeRaw,
                                                )
                                            this +=
                                                Option(
                                                    OptionId.SeeRaw,
                                                    LocalStrings.current.postActionSeeRaw,
                                                )
                                            this +=
                                                Option(
                                                    OptionId.Edit,
                                                    LocalStrings.current.postActionEdit,
                                                )
                                            if (comment.deleted) {
                                                this +=
                                                    Option(
                                                        OptionId.Restore,
                                                        LocalStrings.current.actionRestore,
                                                    )
                                            } else {
                                                this +=
                                                    Option(
                                                        OptionId.Delete,
                                                        LocalStrings.current.commentActionDelete,
                                                    )
                                            }
                                        },
                                    onOptionSelected = { optionId ->
                                        when (optionId) {
                                            OptionId.Delete -> {
                                                commentIdToDelete = comment.id
                                            }

                                            OptionId.Edit -> {
                                                detailOpener.openReply(
                                                    originalPost = PostModel(id = comment.postId),
                                                    editedComment = comment,
                                                )
                                            }

                                            OptionId.SeeRaw -> {
                                                rawContent = comment
                                            }

                                            OptionId.Share -> {
                                                val urls =
                                                    listOfNotNull(
                                                        comment.originalUrl,
                                                        "https://${uiState.instance}/comment/${comment.id}",
                                                    ).distinct()
                                                if (urls.size == 1) {
                                                    model.reduce(
                                                        ProfileLoggedMviModel.Intent.Share(
                                                            urls.first(),
                                                        ),
                                                    )
                                                } else {
                                                    val screen =
                                                        ShareBottomSheet(urls = urls)
                                                    navigationCoordinator.showBottomSheet(
                                                        screen,
                                                    )
                                                }
                                            }

                                            OptionId.Restore -> {
                                                model.reduce(
                                                    ProfileLoggedMviModel.Intent.RestoreComment(
                                                        comment.id,
                                                    ),
                                                )
                                            }

                                            else -> Unit
                                        }
                                    },
                                )
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = Spacing.xxxs),
                                    thickness = 0.25.dp,
                                )
                            }

                            if (uiState.comments.isEmpty() && !uiState.loading && !uiState.initial) {
                                item {
                                    Text(
                                        modifier =
                                            Modifier
                                                .fillMaxWidth()
                                                .padding(top = Spacing.xs),
                                        textAlign = TextAlign.Center,
                                        text = LocalStrings.current.messageEmptyList,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onBackground,
                                    )
                                }
                            }
                        }
                        item {
                            if (!uiState.initial && !uiState.loading && !uiState.refreshing && uiState.canFetchMore) {
                                if (settings.infiniteScrollEnabled) {
                                    model.reduce(ProfileLoggedMviModel.Intent.LoadNextPage)
                                } else {
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(top = Spacing.s),
                                        horizontalArrangement = Arrangement.Center,
                                    ) {
                                        Button(
                                            onClick = {
                                                model.reduce(ProfileLoggedMviModel.Intent.LoadNextPage)
                                            },
                                        ) {
                                            Text(
                                                text =
                                                    if (uiState.section == ProfileLoggedSection.Posts) {
                                                        LocalStrings.current.postListLoadMorePosts
                                                    } else {
                                                        LocalStrings.current.postDetailLoadMoreComments
                                                    },
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
                    }
                    item {
                        Spacer(modifier = Modifier.height(Spacing.xxxl))
                    }
                }
            }
        }

        if (rawContent != null) {
            when (val content = rawContent) {
                is PostModel -> {
                    RawContentDialog(
                        title = content.title,
                        publishDate = content.publishDate,
                        updateDate = content.updateDate,
                        url = content.url,
                        text = content.text,
                        upVotes = content.upvotes,
                        downVotes = content.downvotes,
                        onDismiss = {
                            rawContent = null
                        },
                        onQuote = { quotation ->
                            rawContent = null
                            if (quotation != null) {
                                detailOpener.openReply(
                                    originalPost = content,
                                    initialText =
                                        buildString {
                                            append("> ")
                                            append(quotation)
                                            append("\n\n")
                                        },
                                )
                            }
                        },
                    )
                }

                is CommentModel -> {
                    RawContentDialog(
                        text = content.text,
                        upVotes = content.upvotes,
                        downVotes = content.downvotes,
                        publishDate = content.publishDate,
                        updateDate = content.updateDate,
                        onDismiss = {
                            rawContent = null
                        },
                        onQuote = { quotation ->
                            rawContent = null
                            if (quotation != null) {
                                detailOpener.openReply(
                                    originalPost = PostModel(id = content.postId),
                                    originalComment = content,
                                    initialText =
                                        buildString {
                                            append("> ")
                                            append(quotation)
                                            append("\n\n")
                                        },
                                )
                            }
                        },
                    )
                }
            }
        }

        postIdToDelete?.also { itemId ->
            AlertDialog(
                onDismissRequest = {
                    postIdToDelete = null
                },
                dismissButton = {
                    Button(
                        onClick = {
                            postIdToDelete = null
                        },
                    ) {
                        Text(text = LocalStrings.current.buttonCancel)
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            model.reduce(ProfileLoggedMviModel.Intent.DeletePost(itemId))
                            postIdToDelete = null
                        },
                    ) {
                        Text(text = LocalStrings.current.buttonConfirm)
                    }
                },
                text = {
                    Text(text = LocalStrings.current.messageAreYouSure)
                },
            )
        }
        commentIdToDelete?.also { itemId ->
            AlertDialog(
                onDismissRequest = {
                    commentIdToDelete = null
                },
                dismissButton = {
                    Button(
                        onClick = {
                            commentIdToDelete = null
                        },
                    ) {
                        Text(text = LocalStrings.current.buttonCancel)
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            model.reduce(ProfileLoggedMviModel.Intent.DeleteComment(itemId))
                            commentIdToDelete = null
                        },
                    ) {
                        Text(text = LocalStrings.current.buttonConfirm)
                    }
                },
                text = {
                    Text(text = LocalStrings.current.messageAreYouSure)
                },
            )
        }
    }
}
