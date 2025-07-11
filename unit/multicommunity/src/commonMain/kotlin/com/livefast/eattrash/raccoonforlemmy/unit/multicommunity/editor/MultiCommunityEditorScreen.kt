package com.livefast.eattrash.raccoonforlemmy.unit.multicommunity.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.toWindowInsets
import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.getViewModel
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.CustomImage
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.SearchField
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.CommunityItem
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.onClick
import com.livefast.eattrash.raccoonforlemmy.core.utils.toReadableMessage
import com.livefast.eattrash.raccoonforlemmy.unit.multicommunity.di.MultiCommunityEditorMviModelParams
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiCommunityEditorScreen(modifier: Modifier = Modifier, communityId: Long? = null) {
    val model: MultiCommunityEditorMviModel =
        getViewModel<MultiCommunityEditorViewModel>(MultiCommunityEditorMviModelParams(communityId ?: 0))
    val uiState by model.uiState.collectAsState()
    val topAppBarState = rememberTopAppBarState()
    val navigationCoordinator = remember { getNavigationCoordinator() }

    LaunchedEffect(model) {
        model.effects
            .onEach {
                when (it) {
                    MultiCommunityEditorMviModel.Effect.Close -> {
                        navigationCoordinator.pop()
                    }
                }
            }.launchIn(this)
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                windowInsets = topAppBarState.toWindowInsets(),
                title = {
                    Text(
                        text = LocalStrings.current.multiCommunityEditorTitle,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleMedium,
                    )
                },
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
                actions = {
                    IconButton(
                        modifier = Modifier.padding(horizontal = Spacing.xs),
                        onClick = {
                            model.reduce(MultiCommunityEditorMviModel.Intent.Submit)
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = LocalStrings.current.buttonConfirm,
                        )
                    }
                },
            )
        },
    ) { padding ->
        val focusManager = LocalFocusManager.current
        val keyboardScrollConnection =
            remember {
                object : NestedScrollConnection {
                    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                        focusManager.clearFocus()
                        return Offset.Zero
                    }
                }
            }
        Column(
            modifier =
            Modifier
                .padding(
                    top = padding.calculateTopPadding(),
                ).nestedScroll(keyboardScrollConnection),
            verticalArrangement = Arrangement.spacedBy(Spacing.s),
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                colors =
                TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                ),
                maxLines = 1,
                label = {
                    Text(text = LocalStrings.current.multiCommunityEditorName)
                },
                textStyle = MaterialTheme.typography.bodyMedium,
                value = uiState.name,
                keyboardOptions =
                KeyboardOptions(
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                ),
                onValueChange = { value ->
                    model.reduce(MultiCommunityEditorMviModel.Intent.SetName(value))
                },
                isError = uiState.nameError != null,
                supportingText = {
                    val error = uiState.nameError
                    if (error != null) {
                        Text(
                            text = error.toReadableMessage(),
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                },
            )

            Spacer(modifier = Modifier.height(Spacing.s))
            Column(
                modifier = Modifier.padding(horizontal = Spacing.m),
            ) {
                Text(text = LocalStrings.current.multiCommunityEditorIcon)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.s),
                ) {
                    val iconSize = 40.dp
                    if (uiState.autoLoadImages) {
                        itemsIndexed(uiState.availableIcons) { idx, url ->
                            val selected = url == uiState.icon
                            CustomImage(
                                modifier =
                                Modifier
                                    .size(iconSize)
                                    .clip(RoundedCornerShape(iconSize / 2))
                                    .let {
                                        if (selected) {
                                            it
                                                .border(
                                                    width = 1.dp,
                                                    color = MaterialTheme.colorScheme.primary,
                                                    shape = CircleShape,
                                                ).padding(1.dp)
                                                .border(
                                                    width = 1.dp,
                                                    color = MaterialTheme.colorScheme.onPrimary,
                                                    shape = CircleShape,
                                                ).padding(1.dp)
                                                .border(
                                                    width = 1.dp,
                                                    color = MaterialTheme.colorScheme.primary,
                                                    shape = CircleShape,
                                                )
                                        } else {
                                            it
                                        }
                                    }.onClick(
                                        onClick = {
                                            model.reduce(
                                                MultiCommunityEditorMviModel.Intent.SelectImage(
                                                    idx,
                                                ),
                                            )
                                        },
                                    ),
                                url = url,
                                autoload = uiState.autoLoadImages,
                                contentScale = ContentScale.FillBounds,
                            )
                        }
                    }
                    item {
                        val selected = uiState.icon == null
                        Box(
                            modifier =
                            Modifier
                                .padding(Spacing.xxxs)
                                .size(iconSize)
                                .background(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(iconSize / 2),
                                ).let {
                                    if (selected) {
                                        it
                                            .border(
                                                width = 1.dp,
                                                color = MaterialTheme.colorScheme.primary,
                                                shape = CircleShape,
                                            ).padding(1.dp)
                                            .border(
                                                width = 1.dp,
                                                color = MaterialTheme.colorScheme.onPrimary,
                                                shape = CircleShape,
                                            ).padding(1.dp)
                                            .border(
                                                width = 1.dp,
                                                color = MaterialTheme.colorScheme.primary,
                                                shape = CircleShape,
                                            )
                                    } else {
                                        it
                                    }
                                }.onClick(
                                    onClick = {
                                        model.reduce(
                                            MultiCommunityEditorMviModel.Intent.SelectImage(
                                                null,
                                            ),
                                        )
                                    },
                                ),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text =
                                uiState.name
                                    .firstOrNull()
                                    ?.toString()
                                    .orEmpty()
                                    .uppercase(),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimary,
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(Spacing.s))
            Column(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.s),
            ) {
                Text(
                    text = LocalStrings.current.multiCommunityEditorCommunities,
                )

                // search field
                SearchField(
                    modifier = Modifier.fillMaxWidth(),
                    hint = LocalStrings.current.exploreSearchPlaceholder,
                    value = uiState.searchText,
                    onValueChange = { value ->
                        model.reduce(MultiCommunityEditorMviModel.Intent.SetSearch(value))
                    },
                    onClear = {
                        model.reduce(MultiCommunityEditorMviModel.Intent.SetSearch(""))
                    },
                )
            }
            LazyColumn(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.s)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(Spacing.xxs),
            ) {
                items(uiState.communities) { community ->
                    val selected = uiState.selectedCommunityIds.contains(community.id)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        CommunityItem(
                            modifier =
                            Modifier
                                .weight(1f)
                                .background(MaterialTheme.colorScheme.background),
                            noPadding = true,
                            community = community,
                            preferNicknames = uiState.preferNicknames,
                        )
                        Checkbox(
                            checked = selected,
                            onCheckedChange = {
                                model.reduce(
                                    MultiCommunityEditorMviModel.Intent.ToggleCommunity(
                                        community.id,
                                    ),
                                )
                            },
                        )
                    }
                }

                item {
                    if (!uiState.loading && uiState.canFetchMore) {
                        model.reduce(MultiCommunityEditorMviModel.Intent.LoadNextPage)
                    }
                    if (uiState.loading) {
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
        }
    }
}
