package com.livefast.eattrash.raccoonforlemmy.unit.usertags.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExpandLess
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.kodein.rememberScreenModel
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.toWindowInsets
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.FloatingActionButtonMenu
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.FloatingActionButtonMenuItem
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.CommunityItemPlaceholder
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.Option
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.OptionId
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.SettingsHeader
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.UserTagItem
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.di.getFabNestedScrollConnection
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.EditUserTagDialog
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.UserTagModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.UserTagType
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.isSpecial
import com.livefast.eattrash.raccoonforlemmy.core.utils.ValidationError
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.onClick
import com.livefast.eattrash.raccoonforlemmy.unit.usertags.detail.UserTagDetailScreen
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class UserTagsScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val model = rememberScreenModel<UserTagsMviModel>()
        val uiState by model.uiState.collectAsState()
        val navigatorCoordinator = remember { getNavigationCoordinator() }
        val topAppBarState = rememberTopAppBarState()
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)
        val fabNestedScrollConnection = remember { getFabNestedScrollConnection() }
        val isFabVisible by fabNestedScrollConnection.isFabVisible.collectAsState()
        val lazyListState = rememberLazyListState()
        val scope = rememberCoroutineScope()
        var addTagDialogOpen by remember { mutableStateOf(false) }
        var addTagTitleError by remember { mutableStateOf<ValidationError?>(null) }
        var tagToEdit by remember { mutableStateOf<UserTagModel?>(null) }
        var editTagTitleError by remember { mutableStateOf<ValidationError?>(null) }

        LaunchedEffect(model) {
            model.effects
                .onEach { event ->
                    when (event) {
                        UserTagsMviModel.Effect.BackToTop ->
                            runCatching {
                                lazyListState.scrollToItem(0)
                                topAppBarState.heightOffset = 0f
                                topAppBarState.contentOffset = 0f
                            }
                    }
                }.launchIn(this)
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    windowInsets = topAppBarState.toWindowInsets(),
                    title = {
                        Text(
                            modifier = Modifier.padding(horizontal = Spacing.s),
                            text = LocalStrings.current.userTagsTitle,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    },
                    scrollBehavior = scrollBehavior,
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navigatorCoordinator.popScreen()
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
            floatingActionButton = {
                AnimatedVisibility(
                    visible = isFabVisible,
                    enter =
                        slideInVertically(
                            initialOffsetY = { it * 2 },
                        ),
                    exit =
                        slideOutVertically(
                            targetOffsetY = { it * 2 },
                        ),
                ) {
                    FloatingActionButtonMenu(
                        items =
                            buildList {
                                this +=
                                    FloatingActionButtonMenuItem(
                                        icon = Icons.Default.ExpandLess,
                                        text = LocalStrings.current.actionBackToTop,
                                        onSelected = {
                                            scope.launch {
                                                runCatching {
                                                    lazyListState.scrollToItem(0)
                                                    topAppBarState.heightOffset = 0f
                                                    topAppBarState.contentOffset = 0f
                                                }
                                            }
                                        },
                                    )
                                this +=
                                    FloatingActionButtonMenuItem(
                                        icon = Icons.Default.Add,
                                        text = LocalStrings.current.buttonAdd,
                                        onSelected = {
                                            addTagDialogOpen = true
                                        },
                                    )
                            },
                    )
                }
            },
        ) { padding ->
            PullToRefreshBox(
                modifier =
                    Modifier
                        .padding(
                            top = padding.calculateTopPadding(),
                        ).fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection)
                        .nestedScroll(fabNestedScrollConnection),
                isRefreshing = uiState.refreshing,
                onRefresh = {
                    model.reduce(UserTagsMviModel.Intent.Refresh)
                },
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = lazyListState,
                    verticalArrangement = Arrangement.spacedBy(Spacing.xxs),
                ) {
                    if (uiState.initial) {
                        items(5) {
                            CommunityItemPlaceholder()
                        }
                    }

                    if (uiState.specialTags.isNotEmpty()) {
                        item {
                            SettingsHeader(
                                title = LocalStrings.current.userTagsSpecialSectionTitle,
                            )
                        }
                    }
                    items(uiState.specialTags) { tag ->
                        UserTagItem(
                            modifier = Modifier.fillMaxWidth(),
                            tag = tag,
                            options =
                                buildList {
                                    this +=
                                        Option(
                                            id = OptionId.Edit,
                                            text = LocalStrings.current.postActionEdit,
                                        )
                                },
                            onOptionSelected = { optionId ->
                                when (optionId) {
                                    OptionId.Edit -> tagToEdit = tag
                                    else -> Unit
                                }
                            },
                        )
                    }

                    if (uiState.regularTags.isNotEmpty()) {
                        item {
                            SettingsHeader(
                                title = LocalStrings.current.userTagsRegularSectionTitle,
                            )
                        }
                    }
                    items(uiState.regularTags) { tag ->
                        UserTagItem(
                            modifier =
                                Modifier.fillMaxWidth().onClick(
                                    onClick = {
                                        tag.id?.also {
                                            val screen = UserTagDetailScreen(it)
                                            navigatorCoordinator.pushScreen(screen)
                                        }
                                    },
                                ),
                            tag = tag,
                            options =
                                buildList {
                                    this +=
                                        Option(
                                            id = OptionId.Edit,
                                            text = LocalStrings.current.postActionEdit,
                                        )
                                    this +=
                                        Option(
                                            id = OptionId.Delete,
                                            text = LocalStrings.current.commentActionDelete,
                                        )
                                },
                            onOptionSelected = { optionId ->
                                when (optionId) {
                                    OptionId.Edit -> tagToEdit = tag

                                    OptionId.Delete ->
                                        tag.id?.also {
                                            model.reduce(UserTagsMviModel.Intent.Delete(it))
                                        }

                                    else -> Unit
                                }
                            },
                        )
                    }
                    if ((uiState.specialTags + uiState.regularTags).isEmpty() && !uiState.initial) {
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
                    item {
                        Spacer(modifier = Modifier.height(Spacing.xxxl))
                    }
                }
            }
        }

        if (addTagDialogOpen) {
            val forbiddenTagNames =
                (uiState.specialTags + uiState.regularTags).map {
                    it.name.lowercase()
                }
            EditUserTagDialog(
                title = LocalStrings.current.buttonAdd,
                titleError = addTagTitleError,
                value = "",
                onClose = { name, color ->
                    addTagTitleError =
                        if (name?.lowercase() in forbiddenTagNames) {
                            ValidationError.InvalidField
                        } else {
                            null
                        }
                    if (addTagTitleError == null) {
                        addTagDialogOpen = false
                        if (name != null) {
                            model.reduce(
                                UserTagsMviModel.Intent.Add(
                                    name = name,
                                    color = color?.toArgb(),
                                ),
                            )
                        }
                    }
                },
            )
        }
        if (tagToEdit != null) {
            val forbiddenTagNames =
                (uiState.specialTags + uiState.regularTags).mapNotNull {
                    if (it.id != tagToEdit?.id) {
                        it.name.lowercase()
                    } else {
                        null
                    }
                }
            EditUserTagDialog(
                title = LocalStrings.current.postActionEdit,
                titleError = editTagTitleError,
                value = tagToEdit?.name.orEmpty(),
                canEditName = tagToEdit?.isSpecial != true,
                color = tagToEdit?.color?.let { Color(it) } ?: MaterialTheme.colorScheme.primary,
                onClose = { name, color ->
                    editTagTitleError =
                        if (name?.lowercase() in forbiddenTagNames) {
                            ValidationError.InvalidField
                        } else {
                            null
                        }
                    if (editTagTitleError == null) {
                        val tagId = tagToEdit?.id
                        val type = tagToEdit?.type ?: UserTagType.Regular
                        tagToEdit = null
                        if (tagId != null && name != null) {
                            model.reduce(
                                UserTagsMviModel.Intent.Edit(
                                    id = tagId,
                                    name = name,
                                    type = type,
                                    color = color?.toArgb(),
                                ),
                            )
                        }
                    }
                },
            )
        }
    }
}
