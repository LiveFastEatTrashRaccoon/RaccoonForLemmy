package com.livefast.eattrash.raccoonforlemmy.unit.drafts

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.PostLayout
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.toWindowInsets
import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.getViewModel
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.SectionSelector
import com.livefast.eattrash.raccoonforlemmy.core.commonui.detailopener.api.getDetailOpener
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.Option
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.OptionId
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.DraftModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.di.getSettingsRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommentModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import com.livefast.eattrash.raccoonforlemmy.unit.drafts.components.DraftCard
import com.livefast.eattrash.raccoonforlemmy.unit.drafts.components.DraftCardPlaceHolder

class DraftsScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val model: DraftsMviModel = getViewModel<DraftsViewModel>()
        val uiState by model.uiState.collectAsState()
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
        val topAppBarState = rememberTopAppBarState()
        val navigationCoordinator = remember { getNavigationCoordinator() }
        val settingsRepository = remember { getSettingsRepository() }
        val settings by settingsRepository.currentSettings.collectAsState()
        val lazyListState = rememberLazyListState()
        val detailOpener = remember { getDetailOpener() }
        var itemToDelete by remember { mutableStateOf<DraftModel?>(null) }

        Scaffold(
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            topBar = {
                TopAppBar(
                    windowInsets = topAppBarState.toWindowInsets(),
                    scrollBehavior = scrollBehavior,
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navigationCoordinator.popScreen()
                            },
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = LocalStrings.current.actionGoBack,
                            )
                        }
                    },
                    title = {
                        Text(
                            text = LocalStrings.current.navigationDrawerTitleDrafts,
                            style = MaterialTheme.typography.titleMedium,
                        )
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
                    modifier = Modifier.padding(vertical = Spacing.xs),
                    titles =
                    listOf(
                        LocalStrings.current.profileSectionPosts,
                        LocalStrings.current.profileSectionComments,
                    ),
                    currentSection =
                    when (uiState.section) {
                        DraftsSection.Comments -> 1
                        else -> 0
                    },
                    onSectionSelected = {
                        val section =
                            when (it) {
                                1 -> DraftsSection.Comments
                                else -> DraftsSection.Posts
                            }
                        model.reduce(DraftsMviModel.Intent.ChangeSection(section))
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
                        model.reduce(DraftsMviModel.Intent.Refresh)
                    },
                ) {
                    LazyColumn(
                        modifier =
                        Modifier.fillMaxSize(),
                        state = lazyListState,
                    ) {
                        if (uiState.section == DraftsSection.Posts) {
                            if (uiState.postDrafts.isEmpty() && uiState.loading && uiState.initial) {
                                items(5) {
                                    DraftCardPlaceHolder(uiState.postLayout)
                                    if (uiState.postLayout != PostLayout.Card) {
                                        HorizontalDivider(modifier = Modifier.padding(vertical = Spacing.interItem))
                                    } else {
                                        Spacer(modifier = Modifier.height(Spacing.interItem))
                                    }
                                }
                            }
                            if (uiState.postDrafts.isEmpty() && !uiState.initial) {
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
                                items = uiState.postDrafts,
                                key = { it.toKey() },
                            ) { draft ->
                                DraftCard(
                                    draft = draft,
                                    postLayout = uiState.postLayout,
                                    onOpen = {
                                        detailOpener.openCreatePost(
                                            draftId = draft.id,
                                            communityId = draft.communityId,
                                            initialText = draft.body,
                                            initialTitle = draft.title,
                                            initialUrl = draft.url,
                                            initialNsfw = draft.nsfw,
                                            forceCommunitySelection = true,
                                        )
                                    },
                                    options =
                                    buildList {
                                        this +=
                                            Option(
                                                OptionId.Delete,
                                                LocalStrings.current.commentActionDelete,
                                            )
                                    },
                                    onSelectOption = { optionId ->
                                        when (optionId) {
                                            OptionId.Delete -> {
                                                draft.id?.also {
                                                    itemToDelete = draft
                                                }
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
                        } else {
                            if (uiState.commentDrafts.isEmpty() && uiState.loading && uiState.initial) {
                                items(5) {
                                    DraftCardPlaceHolder(uiState.postLayout)
                                    if (uiState.postLayout != PostLayout.Card) {
                                        HorizontalDivider(modifier = Modifier.padding(vertical = Spacing.interItem))
                                    } else {
                                        Spacer(modifier = Modifier.height(Spacing.interItem))
                                    }
                                }
                            }
                            if (uiState.commentDrafts.isEmpty() && !uiState.initial) {
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
                                uiState.commentDrafts,
                                key = { it.toKey() },
                            ) { draft ->
                                DraftCard(
                                    draft = draft,
                                    postLayout = uiState.postLayout,
                                    onOpen = {
                                        detailOpener.openReply(
                                            draftId = draft.id,
                                            originalPost =
                                            PostModel(
                                                id = draft.postId ?: 0,
                                            ),
                                            originalComment =
                                            draft.parentId?.let {
                                                CommentModel(id = it, text = "")
                                            },
                                            initialText = draft.body,
                                        )
                                    },
                                    options =
                                    buildList {
                                        this +=
                                            Option(
                                                OptionId.Delete,
                                                LocalStrings.current.commentActionDelete,
                                            )
                                    },
                                    onSelectOption = { optionId ->
                                        when (optionId) {
                                            OptionId.Delete -> {
                                                draft.id?.also {
                                                    itemToDelete = draft
                                                }
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
                        }

                        item {
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
                }
            }
        }

        itemToDelete?.also { item ->
            AlertDialog(
                onDismissRequest = {
                    itemToDelete = null
                },
                dismissButton = {
                    Button(
                        onClick = {
                            itemToDelete = null
                        },
                    ) {
                        Text(text = LocalStrings.current.buttonCancel)
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            model.reduce(DraftsMviModel.Intent.Delete(item))
                            itemToDelete = null
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

private fun DraftModel.toKey(): String = buildString {
    append(date)
    if (communityId != null) {
        append("&community=")
        append(communityId)
    } else {
        if (postId != null) {
            append("&post=")
            append(postId)
        } else if (parentId != null) {
            append("&comment=")
            append(parentId)
        }
    }
    if (!title.isNullOrEmpty()) {
        append("&title=")
        append(title)
    }
    append("&body=")
    append(body)
}
