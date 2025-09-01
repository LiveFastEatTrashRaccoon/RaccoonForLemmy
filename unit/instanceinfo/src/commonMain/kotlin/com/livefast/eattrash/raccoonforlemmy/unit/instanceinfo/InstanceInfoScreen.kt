package com.livefast.eattrash.raccoonforlemmy.unit.instanceinfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.ContentFontClass
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.getViewModel
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.CustomizedContent
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.CommunityItem
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.CommunityItemPlaceholder
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.SortBottomSheet
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getMainRouter
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.persistence.di.getSettingsRepository
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.onClick
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.getAdditionalLabel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toIcon
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toReadableName
import com.livefast.eattrash.raccoonforlemmy.unit.instanceinfo.di.InstanceInfoMviModelParams
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstanceInfoScreen(url: String, modifier: Modifier = Modifier) {
    val model: InstanceInfoMviModel = getViewModel<InstanceInfoViewModel>(InstanceInfoMviModelParams(url))
    val uiState by model.uiState.collectAsState()
    val navigationCoordinator = remember { getNavigationCoordinator() }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val settingsRepository = remember { getSettingsRepository() }
    val settings by settingsRepository.currentSettings.collectAsState()
    val listState = rememberLazyListState()
    val mainRouter = remember { getMainRouter() }
    var sortBottomSheetOpened by remember { mutableStateOf(false) }
    val instanceName = url.replace("https://", "")

    LaunchedEffect(model) {
        model.effects
            .onEach { effect ->
                when (effect) {
                    InstanceInfoMviModel.Effect.BackToTop -> {
                        runCatching {
                            listState.scrollToItem(0)
                        }
                    }
                }
            }.launchIn(this)
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        modifier = modifier,
        topBar = {
            TopAppBar(
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
                    Text(
                        text =
                        buildString {
                            append(LocalStrings.current.instanceDetailTitle)
                            append(" ")
                            append(instanceName)
                        },
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleMedium,
                    )
                },
                actions = {
                    val additionalLabel = uiState.sortType.getAdditionalLabel()
                    if (additionalLabel.isNotEmpty()) {
                        Text(
                            text =
                            buildString {
                                append("(")
                                append(additionalLabel)
                                append(")")
                            },
                        )
                        Spacer(modifier = Modifier.width(Spacing.xs))
                    }
                    IconButton(
                        onClick = {
                            sortBottomSheetOpened = true
                        },
                    ) {
                        Icon(
                            imageVector = uiState.sortType.toIcon(),
                            contentDescription = uiState.sortType.toReadableName(),
                        )
                    }
                },
            )
        },
    ) { padding ->
        PullToRefreshBox(
            modifier =
            Modifier
                .then(
                    if (settings.hideNavigationBarWhileScrolling) {
                        Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
                    } else {
                        Modifier
                    },
                ).padding(
                    top = padding.calculateTopPadding(),
                ),
            isRefreshing = uiState.refreshing,
            onRefresh = {
                model.reduce(InstanceInfoMviModel.Intent.Refresh)
            },
        ) {
            LazyColumn(
                modifier = Modifier.padding(top = Spacing.xs, start = Spacing.s, end = Spacing.s),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(Spacing.s),
            ) {
                item {
                    CustomizedContent(ContentFontClass.Title) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(Spacing.s),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = uiState.title,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                            if (uiState.description.isNotEmpty()) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = uiState.description,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onBackground,
                                )
                            }
                        }
                    }
                }

                item {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = LocalStrings.current.instanceDetailCommunities,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }

                if (uiState.communities.isEmpty() && uiState.initial) {
                    items(5) {
                        CommunityItemPlaceholder()
                    }
                }

                items(uiState.communities) { community ->
                    CommunityItem(
                        modifier =
                        Modifier.onClick(
                            onClick = {
                                mainRouter.openCommunityDetail(community, instanceName)
                            },
                        ),
                        community = community,
                        autoLoadImages = uiState.autoLoadImages,
                        preferNicknames = uiState.preferNicknames,
                        showSubscribers = true,
                        noPadding = true,
                    )
                }

                item {
                    if (!uiState.initial && !uiState.loading && !uiState.refreshing && uiState.canFetchMore) {
                        model.reduce(InstanceInfoMviModel.Intent.LoadNextPage)
                    }
                }
            }
        }
    }

    if (sortBottomSheetOpened) {
        SortBottomSheet(
            values = uiState.availableSortTypes,
            expandTop = true,
            onSelect = { value ->
                sortBottomSheetOpened = false
                if (value != null) {
                    model.reduce(InstanceInfoMviModel.Intent.ChangeSortType(value))
                }
            },
        )
    }
}
