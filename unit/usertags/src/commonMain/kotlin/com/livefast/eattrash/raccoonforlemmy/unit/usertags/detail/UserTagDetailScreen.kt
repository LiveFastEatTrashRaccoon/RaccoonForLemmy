package com.livefast.eattrash.raccoonforlemmy.unit.usertags.detail

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
import androidx.compose.ui.text.style.TextAlign
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.toWindowInsets
import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.getViewModel
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.CommunityItemPlaceholder
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.Option
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.OptionId
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.UserTagMemberItem
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.unit.usertags.di.UserTagDetailMviModelParams

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserTagDetailScreen(id: Long, modifier: Modifier = Modifier) {
    val model: UserTagDetailMviModel = getViewModel<UserTagDetailViewModel>(UserTagDetailMviModelParams(id))
    val uiState by model.uiState.collectAsState()
    val navigatorCoordinator = remember { getNavigationCoordinator() }
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                windowInsets = topAppBarState.toWindowInsets(),
                title = {
                    Text(
                        modifier = Modifier.padding(horizontal = Spacing.s),
                        text = uiState.tag?.name.orEmpty(),
                        style = MaterialTheme.typography.titleMedium,
                    )
                },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navigatorCoordinator.pop()
                        },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = LocalStrings.current.actionGoBack,
                        )
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
                ).fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            isRefreshing = uiState.refreshing,
            onRefresh = {
                model.reduce(UserTagDetailMviModel.Intent.Refresh)
            },
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(Spacing.xxs),
            ) {
                if (uiState.initial) {
                    items(5) {
                        CommunityItemPlaceholder()
                    }
                }
                items(uiState.users) { user ->
                    UserTagMemberItem(
                        member = user,
                        options =
                        buildList {
                            this +=
                                Option(
                                    id = OptionId.Delete,
                                    text = LocalStrings.current.commentActionDelete,
                                )
                        },
                        onSelectOption = { optionId ->
                            when (optionId) {
                                OptionId.Delete ->
                                    model.reduce(
                                        UserTagDetailMviModel.Intent.Remove(user.username),
                                    )

                                else -> Unit
                            }
                        },
                    )
                }
                if (uiState.users.isEmpty()) {
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
