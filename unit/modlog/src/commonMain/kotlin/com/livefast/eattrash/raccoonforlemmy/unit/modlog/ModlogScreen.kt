package com.livefast.eattrash.raccoonforlemmy.unit.modlog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import cafe.adriel.voyager.core.screen.Screen
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.PostLayout
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.toWindowInsets
import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.getViewModel
import com.livefast.eattrash.raccoonforlemmy.core.commonui.detailopener.api.getDetailOpener
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.persistence.di.getSettingsRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.ModlogItem
import com.livefast.eattrash.raccoonforlemmy.unit.modlog.components.AdminPurgeCommentItem
import com.livefast.eattrash.raccoonforlemmy.unit.modlog.components.AdminPurgeCommunityItem
import com.livefast.eattrash.raccoonforlemmy.unit.modlog.components.AdminPurgePersonItem
import com.livefast.eattrash.raccoonforlemmy.unit.modlog.components.AdminPurgePostItem
import com.livefast.eattrash.raccoonforlemmy.unit.modlog.components.HideCommunityItem
import com.livefast.eattrash.raccoonforlemmy.unit.modlog.components.ModAddCommunityItem
import com.livefast.eattrash.raccoonforlemmy.unit.modlog.components.ModAddItem
import com.livefast.eattrash.raccoonforlemmy.unit.modlog.components.ModBanFromCommunityItem
import com.livefast.eattrash.raccoonforlemmy.unit.modlog.components.ModBanItem
import com.livefast.eattrash.raccoonforlemmy.unit.modlog.components.ModFeaturePostItem
import com.livefast.eattrash.raccoonforlemmy.unit.modlog.components.ModLockPostItem
import com.livefast.eattrash.raccoonforlemmy.unit.modlog.components.ModRemoveCommentItem
import com.livefast.eattrash.raccoonforlemmy.unit.modlog.components.ModRemovePostItem
import com.livefast.eattrash.raccoonforlemmy.unit.modlog.components.ModTransferCommunityItem
import com.livefast.eattrash.raccoonforlemmy.unit.modlog.components.ModlogItemPlaceholder
import com.livefast.eattrash.raccoonforlemmy.unit.modlog.components.RemoveCommunityItem
import com.livefast.eattrash.raccoonforlemmy.unit.modlog.di.ModlogMviModelParams
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ModlogScreen(private val communityId: Long? = null) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val model: ModlogMviModel = getViewModel<ModlogViewModel>(ModlogMviModelParams(communityId ?: 0L))
        val uiState by model.uiState.collectAsState()
        val topAppBarState = rememberTopAppBarState()
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)
        val navigationCoordinator = remember { getNavigationCoordinator() }
        val settingsRepository = remember { getSettingsRepository() }
        val settings by settingsRepository.currentSettings.collectAsState()
        val lazyListState = rememberLazyListState()
        val detailOpener = remember { getDetailOpener() }

        LaunchedEffect(model) {
            model.effects
                .onEach { effect ->
                    when (effect) {
                        ModlogMviModel.Effect.BackToTop -> {
                            runCatching {
                                lazyListState.scrollToItem(0)
                                topAppBarState.heightOffset = 0f
                                topAppBarState.contentOffset = 0f
                            }
                        }
                    }
                }.launchIn(this)
        }

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
                            modifier = Modifier.padding(horizontal = Spacing.s),
                            text = LocalStrings.current.modlogTitle,
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
                        model.reduce(ModlogMviModel.Intent.Refresh)
                    },
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = lazyListState,
                        verticalArrangement = Arrangement.spacedBy(Spacing.s),
                    ) {
                        if (uiState.items.isEmpty() && uiState.loading && uiState.initial) {
                            items(5) {
                                ModlogItemPlaceholder(uiState.postLayout)
                                if (uiState.postLayout != PostLayout.Card) {
                                    HorizontalDivider(modifier = Modifier.padding(vertical = Spacing.interItem))
                                } else {
                                    Spacer(modifier = Modifier.height(Spacing.interItem))
                                }
                            }
                        }
                        if (uiState.items.isEmpty() && !uiState.initial) {
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
                            items = uiState.items,
                            key = { "${it.type}${it.id}" },
                        ) { item ->
                            when (item) {
                                is ModlogItem.ModAdd ->
                                    ModAddItem(
                                        item = item,
                                        autoLoadImages = uiState.autoLoadImages,
                                        postLayout = uiState.postLayout,
                                        onOpenUser = { user ->
                                            detailOpener.openUserDetail(user)
                                        },
                                    )

                                is ModlogItem.ModBan ->
                                    ModBanItem(
                                        item = item,
                                        autoLoadImages = uiState.autoLoadImages,
                                        postLayout = uiState.postLayout,
                                        onOpenUser = { user ->
                                            detailOpener.openUserDetail(user)
                                        },
                                    )

                                is ModlogItem.ModAddCommunity -> {
                                    ModAddCommunityItem(
                                        item = item,
                                        autoLoadImages = uiState.autoLoadImages,
                                        postLayout = uiState.postLayout,
                                        onOpenUser = { user ->
                                            detailOpener.openUserDetail(user)
                                        },
                                    )
                                }

                                is ModlogItem.ModBanFromCommunity -> {
                                    ModBanFromCommunityItem(
                                        item = item,
                                        autoLoadImages = uiState.autoLoadImages,
                                        postLayout = uiState.postLayout,
                                        onOpenUser = { user ->
                                            detailOpener.openUserDetail(user)
                                        },
                                    )
                                }

                                is ModlogItem.ModFeaturePost -> {
                                    ModFeaturePostItem(
                                        item = item,
                                        autoLoadImages = uiState.autoLoadImages,
                                        postLayout = uiState.postLayout,
                                        onOpenUser = { user ->
                                            detailOpener.openUserDetail(user)
                                        },
                                    )
                                }

                                is ModlogItem.ModLockPost -> {
                                    ModLockPostItem(
                                        item = item,
                                        autoLoadImages = uiState.autoLoadImages,
                                        postLayout = uiState.postLayout,
                                        onOpenUser = { user ->
                                            detailOpener.openUserDetail(user)
                                        },
                                    )
                                }

                                is ModlogItem.ModRemoveComment -> {
                                    ModRemoveCommentItem(
                                        item = item,
                                        autoLoadImages = uiState.autoLoadImages,
                                        postLayout = uiState.postLayout,
                                        onOpenUser = { user ->
                                            detailOpener.openUserDetail(user)
                                        },
                                    )
                                }

                                is ModlogItem.ModRemovePost -> {
                                    ModRemovePostItem(
                                        item = item,
                                        autoLoadImages = uiState.autoLoadImages,
                                        postLayout = uiState.postLayout,
                                        onOpenUser = { user ->
                                            detailOpener.openUserDetail(user)
                                        },
                                    )
                                }

                                is ModlogItem.ModTransferCommunity -> {
                                    ModTransferCommunityItem(
                                        item = item,
                                        autoLoadImages = uiState.autoLoadImages,
                                        postLayout = uiState.postLayout,
                                        onOpenUser = { user ->
                                            detailOpener.openUserDetail(user)
                                        },
                                    )
                                }

                                is ModlogItem.AdminPurgeComment -> {
                                    AdminPurgeCommentItem(
                                        item = item,
                                        autoLoadImages = uiState.autoLoadImages,
                                        postLayout = uiState.postLayout,
                                        onOpenUser = { user ->
                                            detailOpener.openUserDetail(user)
                                        },
                                    )
                                }

                                is ModlogItem.AdminPurgeCommunity -> {
                                    AdminPurgeCommunityItem(
                                        item = item,
                                        autoLoadImages = uiState.autoLoadImages,
                                        postLayout = uiState.postLayout,
                                        onOpenUser = { user ->
                                            detailOpener.openUserDetail(user)
                                        },
                                    )
                                }

                                is ModlogItem.AdminPurgePerson -> {
                                    AdminPurgePersonItem(
                                        item = item,
                                        autoLoadImages = uiState.autoLoadImages,
                                        postLayout = uiState.postLayout,
                                        onOpenUser = { user ->
                                            detailOpener.openUserDetail(user)
                                        },
                                    )
                                }

                                is ModlogItem.AdminPurgePost -> {
                                    AdminPurgePostItem(
                                        item = item,
                                        autoLoadImages = uiState.autoLoadImages,
                                        postLayout = uiState.postLayout,
                                        onOpenUser = { user ->
                                            detailOpener.openUserDetail(user)
                                        },
                                    )
                                }

                                is ModlogItem.HideCommunity -> {
                                    HideCommunityItem(
                                        item = item,
                                        autoLoadImages = uiState.autoLoadImages,
                                        postLayout = uiState.postLayout,
                                        onOpenUser = { user ->
                                            detailOpener.openUserDetail(user)
                                        },
                                    )
                                }

                                is ModlogItem.RemoveCommunity -> {
                                    RemoveCommunityItem(
                                        item = item,
                                        autoLoadImages = uiState.autoLoadImages,
                                        postLayout = uiState.postLayout,
                                        onOpenUser = { user ->
                                            detailOpener.openUserDetail(user)
                                        },
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
