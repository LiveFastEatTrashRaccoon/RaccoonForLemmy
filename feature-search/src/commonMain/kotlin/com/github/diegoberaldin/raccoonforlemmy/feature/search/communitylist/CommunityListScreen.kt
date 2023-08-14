package com.github.diegoberaldin.raccoonforlemmy.feature.search.communitylist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.diegoberaldin.racconforlemmy.core.utils.onClick
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.bindToLifecycle
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.communitydetail.CommunityDetailScreen
import com.github.diegoberaldin.raccoonforlemmy.feature.search.di.getSearchScreenModel
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.CommunityItem
import com.github.diegoberaldin.raccoonforlemmy.resources.MR
import dev.icerock.moko.resources.compose.stringResource

class CommunityListScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val model = rememberScreenModel { getSearchScreenModel() }
        model.bindToLifecycle(key)
        val uiState by model.uiState.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        Column(
            modifier = Modifier.padding(Spacing.xxs),
            verticalArrangement = Arrangement.spacedBy(Spacing.xxs),
        ) {
            TextField(
                modifier = Modifier.padding(
                    horizontal = Spacing.m,
                    vertical = Spacing.s,
                ).fillMaxWidth(),
                label = {
                    Text(text = stringResource(MR.strings.explore_search_placeholder))
                },
                singleLine = true,
                value = uiState.searchText,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                ),
                onValueChange = { value ->
                    model.reduce(CommunityListMviModel.Intent.SetSearch(value))
                },
            )
            Row(
                modifier = Modifier.padding(horizontal = Spacing.xxs),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (uiState.isLogged) {
                    Checkbox(
                        checked = uiState.subscribedOnly,
                        onCheckedChange = {
                            model.reduce(CommunityListMviModel.Intent.SetSubscribedOnly(it))
                        },
                        colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary),
                    )
                    Text(
                        text = stringResource(MR.strings.explore_subscribed_only),
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = {
                    model.reduce(CommunityListMviModel.Intent.SearchFired)
                }) {
                    Text(
                        text = stringResource(MR.strings.button_search),
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
            }

            val pullRefreshState = rememberPullRefreshState(uiState.refreshing, {
                model.reduce(CommunityListMviModel.Intent.Refresh)
            })
            Box(
                modifier = Modifier.padding(Spacing.xxs).pullRefresh(pullRefreshState),
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(Spacing.xs),
                ) {
                    items(uiState.communities) { community ->
                        CommunityItem(
                            modifier = Modifier.fillMaxWidth().onClick {
                                navigator.push(
                                    CommunityDetailScreen(
                                        community = community,
                                        onBack = {
                                            navigator.pop()
                                        },
                                    ),
                                )
                            },
                            community = community,
                        )
                    }
                    item {
                        if (!uiState.loading && !uiState.refreshing && uiState.canFetchMore) {
                            model.reduce(CommunityListMviModel.Intent.LoadNextPage)
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
}
