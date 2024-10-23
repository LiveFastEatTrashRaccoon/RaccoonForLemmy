package com.livefast.eattrash.raccoonforlemmy.unit.instanceinfo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import cafe.adriel.voyager.core.screen.Screen
import com.livefast.eattrash.raccoonforlemmy.core.appearance.repository.ContentFontClass
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.CustomizedContent
import com.livefast.eattrash.raccoonforlemmy.core.commonui.detailopener.api.getDetailOpener
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.CommunityItem
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.CommunityItemPlaceholder
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.SortBottomSheet
import com.livefast.eattrash.raccoonforlemmy.core.l10n.messages.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.navigation.getScreenModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.di.getSettingsRepository
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.onClick
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.getAdditionalLabel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toIcon
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toInt
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.parameter.parametersOf

class InstanceInfoScreen(
    private val url: String,
) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val instanceName = url.replace("https://", "")
        val model =
            getScreenModel<InstanceInfoMviModel>(
                tag = instanceName,
                parameters = { parametersOf(url) },
            )
        val uiState by model.uiState.collectAsState()
        val navigationCoordinator = remember { getNavigationCoordinator() }
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
        val settingsRepository = remember { getSettingsRepository() }
        val settings by settingsRepository.currentSettings.collectAsState()
        val listState = rememberLazyListState()
        val detailOpener = remember { getDetailOpener() }

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
            modifier =
                Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(Spacing.xs),
            topBar = {
                TopAppBar(
                    scrollBehavior = scrollBehavior,
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navigationCoordinator.popScreen()
                            },
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = null,
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
                                val sheet =
                                    SortBottomSheet(
                                        values = uiState.availableSortTypes.map { it.toInt() },
                                        expandTop = true,
                                        screenKey = "instanceInfo",
                                    )
                                navigationCoordinator.showBottomSheet(sheet)
                            },
                        ) {
                            Icon(
                                imageVector = uiState.sortType.toIcon(),
                                contentDescription = null,
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
                                        detailOpener.openCommunityDetail(community, instanceName)
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
    }
}
