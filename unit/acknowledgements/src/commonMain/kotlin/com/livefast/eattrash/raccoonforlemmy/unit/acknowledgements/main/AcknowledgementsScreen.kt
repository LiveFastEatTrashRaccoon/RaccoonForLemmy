package com.livefast.eattrash.raccoonforlemmy.unit.acknowledgements.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.onClick
import com.livefast.eattrash.raccoonforlemmy.unit.acknowledgements.components.AcknoledgementItem
import com.livefast.eattrash.raccoonforlemmy.unit.acknowledgements.components.AcknoledgementItemPlaceholder

class AcknowledgementsScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val model = getScreenModel<AcknowledgementsMviModel>()
        val uiState by model.uiState.collectAsState()
        val navigationCoordinator = remember { getNavigationCoordinator() }
        val topAppBarState = rememberTopAppBarState()
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)
        val uriHandler = LocalUriHandler.current

        Scaffold(
            modifier =
                Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(Spacing.xs),
            topBar = {
                TopAppBar(
                    scrollBehavior = scrollBehavior,
                    title = {
                        Text(
                            modifier = Modifier.padding(horizontal = Spacing.s),
                            text = LocalStrings.current.settingsAboutAcknowledgements,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    },
                    navigationIcon = {
                        if (navigationCoordinator.canPop.value) {
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
                        }
                    },
                )
            },
        ) { padding ->
            PullToRefreshBox(
                modifier =
                    Modifier
                        .padding(
                            top = padding.calculateTopPadding(),
                        ).nestedScroll(scrollBehavior.nestedScrollConnection)
                        .fillMaxSize(),
                isRefreshing = uiState.refreshing,
                onRefresh = {
                    model.reduce(AcknowledgementsMviModel.Intent.Refresh)
                },
            ) {
                LazyColumn(
                    modifier =
                        Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(Spacing.xs),
                ) {
                    if (uiState.initial) {
                        items(5) {
                            AcknoledgementItemPlaceholder()
                        }
                    }
                    items(uiState.items) { item ->
                        AcknoledgementItem(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .onClick(
                                        onClick = {
                                            if (!item.url.isNullOrEmpty()) {
                                                uriHandler.openUri(item.url)
                                            }
                                        },
                                    ),
                            item = item,
                        )
                    }

                    if (!uiState.initial && uiState.items.isEmpty()) {
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

                    item {
                        Spacer(modifier = Modifier.height(Spacing.xxxl))
                    }
                }
            }
        }
    }
}
