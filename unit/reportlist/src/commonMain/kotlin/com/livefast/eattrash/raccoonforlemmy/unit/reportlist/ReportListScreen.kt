package com.livefast.eattrash.raccoonforlemmy.unit.reportlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.ReportOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.PostLayout
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.CornerSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.toWindowInsets
import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.getViewModel
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.ProgressHud
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.SectionSelector
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.SwipeAction
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.SwipeActionCard
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.Option
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.OptionId
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.CustomModalBottomSheet
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.CustomModalBottomSheetItem
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getMainRouter
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.persistence.di.getSettingsRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommentReportModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostReportModel
import com.livefast.eattrash.raccoonforlemmy.unit.rawcontent.RawContentDialog
import com.livefast.eattrash.raccoonforlemmy.unit.reportlist.components.CommentReportCard
import com.livefast.eattrash.raccoonforlemmy.unit.reportlist.components.PostReportCard
import com.livefast.eattrash.raccoonforlemmy.unit.reportlist.components.ReportCardPlaceHolder
import com.livefast.eattrash.raccoonforlemmy.unit.reportlist.di.ReportListMviModelParams
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportListScreen(modifier: Modifier = Modifier, communityId: Long? = null) {
    val model: ReportListMviModel =
        getViewModel<ReportListViewModel>(ReportListMviModelParams(communityId ?: 0L))
    val uiState by model.uiState.collectAsState()
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)
    val navigationCoordinator = remember { getNavigationCoordinator() }
    var rawContent by remember { mutableStateOf<Any?>(null) }
    val settingsRepository = remember { getSettingsRepository() }
    val settings by settingsRepository.currentSettings.collectAsState()
    val lazyListState = rememberLazyListState()
    val mainRouter = remember { getMainRouter() }
    val defaultResolveColor = MaterialTheme.colorScheme.secondary
    var reportTypeBottomSheetOpened by remember { mutableStateOf(false) }

    LaunchedEffect(model) {
        model.effects
            .onEach { effect ->
                when (effect) {
                    ReportListMviModel.Effect.BackToTop ->
                        kotlin.runCatching {
                            lazyListState.scrollToItem(0)
                            topAppBarState.heightOffset = 0f
                            topAppBarState.contentOffset = 0f
                        }
                }
            }.launchIn(this)
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        modifier = modifier,
        topBar = {
            TopAppBar(
                windowInsets = topAppBarState.toWindowInsets(),
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navigationCoordinator.pop()
                        },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = LocalStrings.current.actionGoBack,
                        )
                    }
                },
                title = {
                    Column(modifier = Modifier) {
                        Text(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = Spacing.s),
                            text = LocalStrings.current.reportListTitle,
                            style = MaterialTheme.typography.titleMedium,
                        )
                        val text =
                            when (uiState.unresolvedOnly) {
                                true -> LocalStrings.current.reportListTypeUnresolved
                                else -> LocalStrings.current.reportListTypeAll
                            }
                        Text(
                            modifier =
                            Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(CornerSize.xl))
                                .clickable {
                                    reportTypeBottomSheetOpened = true
                                }.padding(horizontal = Spacing.s),
                            text = text,
                            style = MaterialTheme.typography.titleSmall,
                        )
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier =
            Modifier
                .padding(
                    top = padding.calculateTopPadding(),
                ).then(
                    if (settings.hideNavigationBarWhileScrolling) {
                        Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
                    } else {
                        Modifier
                    },
                ),
            verticalArrangement = Arrangement.spacedBy(Spacing.s),
        ) {
            SectionSelector(
                titles =
                listOf(
                    LocalStrings.current.profileSectionPosts,
                    LocalStrings.current.profileSectionComments,
                ),
                currentSection =
                when (uiState.section) {
                    ReportListSection.Comments -> 1
                    else -> 0
                },
                onSectionSelected = {
                    val section =
                        when (it) {
                            1 -> ReportListSection.Comments
                            else -> ReportListSection.Posts
                        }
                    model.reduce(ReportListMviModel.Intent.ChangeSection(section))
                },
            )

            PullToRefreshBox(
                modifier =
                Modifier
                    .then(
                        if (settings.hideNavigationBarWhileScrolling) {
                            Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
                        } else {
                            Modifier
                        },
                    ),
                isRefreshing = uiState.refreshing,
                onRefresh = {
                    model.reduce(ReportListMviModel.Intent.Refresh)
                },
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = lazyListState,
                ) {
                    if (uiState.section == ReportListSection.Posts) {
                        if (uiState.postReports.isEmpty() && uiState.loading && uiState.initial) {
                            items(5) {
                                ReportCardPlaceHolder(uiState.postLayout)
                                if (uiState.postLayout != PostLayout.Card) {
                                    HorizontalDivider(modifier = Modifier.padding(vertical = Spacing.interItem))
                                } else {
                                    Spacer(modifier = Modifier.height(Spacing.interItem))
                                }
                            }
                        }
                        if (uiState.postReports.isEmpty() && !uiState.initial && !uiState.loading) {
                            item {
                                Text(
                                    modifier = Modifier.fillMaxWidth().padding(top = Spacing.xs),
                                    textAlign = TextAlign.Center,
                                    text = LocalStrings.current.messageEmptyList,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onBackground,
                                )
                            }
                        }
                        items(
                            items = uiState.postReports,
                            key = {
                                it.id.toString() + (
                                    it.updateDate ?: it.publishDate
                                    ) + it.resolved + uiState.unresolvedOnly
                            },
                        ) { report ->
                            SwipeActionCard(
                                modifier = Modifier.fillMaxWidth(),
                                enabled = uiState.swipeActionsEnabled,
                                onGestureBegin = {
                                    model.reduce(ReportListMviModel.Intent.HapticIndication)
                                },
                                swipeToStartActions =
                                buildList {
                                    this +=
                                        SwipeAction(
                                            swipeContent = {
                                                val icon =
                                                    when {
                                                        report.resolved -> Icons.Default.Report
                                                        else -> Icons.Default.ReportOff
                                                    }
                                                Icon(
                                                    imageVector = icon,
                                                    contentDescription = LocalStrings.current.actionMarkAsResolved,
                                                    tint = Color.White,
                                                )
                                            },
                                            backgroundColor = defaultResolveColor,
                                            onTriggered = {
                                                model.reduce(
                                                    ReportListMviModel.Intent.ResolvePost(report.id),
                                                )
                                            },
                                        )
                                },
                                content = {
                                    PostReportCard(
                                        report = report,
                                        postLayout = uiState.postLayout,
                                        autoLoadImages = uiState.autoLoadImages,
                                        preferNicknames = uiState.preferNicknames,
                                        onOpen = {
                                            mainRouter.openPostDetail(
                                                post = PostModel(id = report.postId),
                                                isMod = true,
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
                                                    OptionId.ResolveReport,
                                                    if (report.resolved) {
                                                        LocalStrings.current.reportActionUnresolve
                                                    } else {
                                                        LocalStrings.current.reportActionResolve
                                                    },
                                                )
                                        },
                                        onSelectOption = { optionId ->
                                            when (optionId) {
                                                OptionId.SeeRaw -> {
                                                    rawContent = report
                                                }

                                                OptionId.ResolveReport -> {
                                                    model.reduce(
                                                        ReportListMviModel.Intent.ResolvePost(
                                                            report.id,
                                                        ),
                                                    )
                                                }

                                                else -> Unit
                                            }
                                        },
                                    )
                                },
                            )
                            if (uiState.postLayout != PostLayout.Card) {
                                HorizontalDivider(modifier = Modifier.padding(vertical = Spacing.interItem))
                            } else {
                                Spacer(modifier = Modifier.height(Spacing.interItem))
                            }
                        }
                    } else {
                        if (uiState.commentReports.isEmpty() && uiState.loading && uiState.initial) {
                            items(5) {
                                ReportCardPlaceHolder(uiState.postLayout)
                                if (uiState.postLayout != PostLayout.Card) {
                                    HorizontalDivider(modifier = Modifier.padding(vertical = Spacing.interItem))
                                } else {
                                    Spacer(modifier = Modifier.height(Spacing.interItem))
                                }
                            }
                        }
                        if (uiState.commentReports.isEmpty() && !uiState.initial && !uiState.loading) {
                            item {
                                Text(
                                    modifier = Modifier.fillMaxWidth().padding(top = Spacing.xs),
                                    textAlign = TextAlign.Center,
                                    text = LocalStrings.current.messageEmptyList,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onBackground,
                                )
                            }
                        }
                        items(
                            items = uiState.commentReports,
                            key = {
                                it.id.toString() + (
                                    it.updateDate ?: it.publishDate
                                    ) + it.resolved
                            },
                        ) { report ->
                            SwipeActionCard(
                                modifier = Modifier.fillMaxWidth(),
                                enabled = uiState.swipeActionsEnabled,
                                onGestureBegin = {
                                    model.reduce(ReportListMviModel.Intent.HapticIndication)
                                },
                                swipeToStartActions =
                                buildList {
                                    this +=
                                        SwipeAction(
                                            swipeContent = {
                                                val icon =
                                                    when {
                                                        report.resolved -> Icons.Default.Report
                                                        else -> Icons.Default.ReportOff
                                                    }
                                                Icon(
                                                    imageVector = icon,
                                                    contentDescription = LocalStrings.current.actionMarkAsResolved,
                                                    tint = Color.White,
                                                )
                                            },
                                            backgroundColor = defaultResolveColor,
                                            onTriggered = {
                                                model.reduce(
                                                    ReportListMviModel.Intent.ResolveComment(report.id),
                                                )
                                            },
                                        )
                                },
                                content = {
                                    CommentReportCard(
                                        report = report,
                                        postLayout = uiState.postLayout,
                                        autoLoadImages = uiState.autoLoadImages,
                                        preferNicknames = uiState.preferNicknames,
                                        onOpen = {
                                            mainRouter.openPostDetail(
                                                post = PostModel(id = report.postId),
                                                highlightCommentId = report.commentId,
                                                isMod = true,
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
                                                    OptionId.ResolveReport,
                                                    if (report.resolved) {
                                                        LocalStrings.current.reportActionUnresolve
                                                    } else {
                                                        LocalStrings.current.reportActionResolve
                                                    },
                                                )
                                        },
                                        onSelectOption = { optionId ->
                                            when (optionId) {
                                                OptionId.SeeRaw -> {
                                                    rawContent = report
                                                }

                                                OptionId.ResolveReport -> {
                                                    model.reduce(
                                                        ReportListMviModel.Intent.ResolveComment(
                                                            report.id,
                                                        ),
                                                    )
                                                }

                                                else -> Unit
                                            }
                                        },
                                    )
                                },
                            )
                            if (uiState.postLayout != PostLayout.Card) {
                                HorizontalDivider(modifier = Modifier.padding(vertical = Spacing.interItem))
                            } else {
                                Spacer(modifier = Modifier.height(Spacing.interItem))
                            }
                        }
                    }

                    item {
                        if (!uiState.initial && !uiState.loading && !uiState.refreshing && uiState.canFetchMore) {
                            model.reduce(ReportListMviModel.Intent.LoadNextPage)
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

                if (uiState.asyncInProgress) {
                    ProgressHud()
                }
            }
        }
    }

    if (rawContent != null) {
        when (val content = rawContent) {
            is PostReportModel -> {
                RawContentDialog(
                    title = content.originalTitle,
                    publishDate = content.publishDate,
                    updateDate = content.updateDate,
                    url = content.originalUrl,
                    text = content.originalText,
                    onDismiss = {
                        rawContent = null
                    },
                )
            }

            is CommentReportModel -> {
                RawContentDialog(
                    publishDate = content.publishDate,
                    updateDate = content.updateDate,
                    text = content.originalText,
                    onDismiss = {
                        rawContent = null
                    },
                )
            }
        }
    }

    if (reportTypeBottomSheetOpened) {
        val values =
            listOf(
                LocalStrings.current.reportListTypeUnresolved,
                LocalStrings.current.reportListTypeAll,
            )
        CustomModalBottomSheet(
            title = LocalStrings.current.reportListTypeTitle,
            items =
            values.map { value ->
                CustomModalBottomSheetItem(label = value)
            },
            onSelect = { index ->
                reportTypeBottomSheetOpened = false
                if (index != null) {
                    model.reduce(ReportListMviModel.Intent.ChangeType(unresolvedOnly = index == 0))
                }
            },
        )
    }
}
