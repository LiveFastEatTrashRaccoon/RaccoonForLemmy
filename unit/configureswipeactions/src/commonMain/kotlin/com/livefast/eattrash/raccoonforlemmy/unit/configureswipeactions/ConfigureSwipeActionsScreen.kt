package com.livefast.eattrash.raccoonforlemmy.unit.configureswipeactions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.IconSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.Option
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.OptionId
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.SettingsHeader
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.CustomModalBottomSheet
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.CustomModalBottomSheetItem
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.notifications.di.getNotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.ActionOnSwipeDirection
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.ActionOnSwipeTarget
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.toIcon
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.toModifier
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.toReadableName
import com.livefast.eattrash.raccoonforlemmy.core.persistence.di.getSettingsRepository
import com.livefast.eattrash.raccoonforlemmy.unit.configureswipeactions.ui.components.ConfigureActionItem
import com.livefast.eattrash.raccoonforlemmy.unit.configureswipeactions.ui.components.ConfigureAddAction

private data class ActionConfig(
    val target: ActionOnSwipeTarget,
    val direction: ActionOnSwipeDirection,
)

class ConfigureSwipeActionsScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val model = getScreenModel<ConfigureSwipeActionsMviModel>()
        val uiState by model.uiState.collectAsState()
        val navigationCoordinator = remember { getNavigationCoordinator() }
        val topAppBarState = rememberTopAppBarState()
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)
        val settingsRepository = remember { getSettingsRepository() }
        val settings by settingsRepository.currentSettings.collectAsState()
        val notificationCenter = remember { getNotificationCenter() }
        var selectActionBottomSheet by remember { mutableStateOf<ActionConfig?>(null) }

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
                            text = LocalStrings.current.settingsConfigureSwipeActions,
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
            Box(
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
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(Spacing.xs),
                ) {
                    // posts
                    item {
                        SettingsHeader(
                            title = LocalStrings.current.exploreResultTypePosts,
                            icon = Icons.AutoMirrored.Default.Article,
                            rightButton = @Composable {
                                TextButton(
                                    contentPadding =
                                        PaddingValues(
                                            horizontal = Spacing.xs,
                                            vertical = Spacing.xxs,
                                        ),
                                    onClick = {
                                        model.reduce(ConfigureSwipeActionsMviModel.Intent.ResetActionsPosts)
                                    },
                                ) {
                                    Text(
                                        text = LocalStrings.current.buttonReset,
                                        style = MaterialTheme.typography.labelSmall,
                                    )
                                }
                            },
                        )
                    }
                    item {
                        Row(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        vertical = Spacing.xxs,
                                        horizontal = Spacing.s,
                                    ),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = LocalStrings.current.configureActionsSideStart,
                            )
                        }
                    }
                    itemsIndexed(uiState.actionsOnSwipeToStartPosts) { idx, action ->
                        ConfigureActionItem(
                            icon =
                                when (idx) {
                                    1 -> Icons.Default.KeyboardDoubleArrowLeft
                                    else -> Icons.AutoMirrored.Default.KeyboardArrowLeft
                                },
                            action = action,
                            options =
                                buildList {
                                    this +=
                                        Option(
                                            OptionId.Remove,
                                            LocalStrings.current.commentActionDelete,
                                        )
                                },
                            onOptionSelected = { optionId ->
                                when (optionId) {
                                    OptionId.Remove -> {
                                        model.reduce(
                                            ConfigureSwipeActionsMviModel.Intent.DeleteActionPosts(
                                                value = action,
                                                direction = ActionOnSwipeDirection.ToStart,
                                            ),
                                        )
                                    }

                                    else -> Unit
                                }
                            },
                        )
                    }
                    if (uiState.availableOptionsPosts.isNotEmpty() && uiState.actionsOnSwipeToStartPosts.size < 2) {
                        item {
                            ConfigureAddAction {
                                selectActionBottomSheet =
                                    ActionConfig(
                                        direction = ActionOnSwipeDirection.ToStart,
                                        target = ActionOnSwipeTarget.Posts,
                                    )
                            }
                        }
                    }
                    item {
                        Row(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        vertical = Spacing.xxs,
                                        horizontal = Spacing.s,
                                    ),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = LocalStrings.current.configureActionsSideEnd,
                            )
                        }
                    }
                    itemsIndexed(uiState.actionsOnSwipeToEndPosts) { idx, action ->
                        ConfigureActionItem(
                            icon =
                                when (idx) {
                                    1 -> Icons.Default.KeyboardDoubleArrowRight
                                    else -> Icons.AutoMirrored.Default.KeyboardArrowRight
                                },
                            action = action,
                            options =
                                buildList {
                                    this +=
                                        Option(
                                            OptionId.Remove,
                                            LocalStrings.current.commentActionDelete,
                                        )
                                },
                            onOptionSelected = { optionId ->
                                when (optionId) {
                                    OptionId.Remove -> {
                                        model.reduce(
                                            ConfigureSwipeActionsMviModel.Intent.DeleteActionPosts(
                                                value = action,
                                                direction = ActionOnSwipeDirection.ToEnd,
                                            ),
                                        )
                                    }

                                    else -> Unit
                                }
                            },
                        )
                    }
                    if (uiState.availableOptionsPosts.isNotEmpty() && uiState.actionsOnSwipeToEndPosts.size < 2) {
                        item {
                            ConfigureAddAction {
                                selectActionBottomSheet =
                                    ActionConfig(
                                        direction = ActionOnSwipeDirection.ToEnd,
                                        target = ActionOnSwipeTarget.Posts,
                                    )
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(Spacing.interItem))
                    }

                    // comments
                    item {
                        SettingsHeader(
                            title = LocalStrings.current.exploreResultTypeComments,
                            icon = Icons.AutoMirrored.Default.Message,
                            rightButton = @Composable {
                                TextButton(
                                    contentPadding =
                                        PaddingValues(
                                            horizontal = Spacing.xs,
                                            vertical = Spacing.xxs,
                                        ),
                                    onClick = {
                                        model.reduce(ConfigureSwipeActionsMviModel.Intent.ResetActionsComments)
                                    },
                                ) {
                                    Text(
                                        text = LocalStrings.current.buttonReset,
                                        style = MaterialTheme.typography.labelSmall,
                                    )
                                }
                            },
                        )
                    }
                    item {
                        Row(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        vertical = Spacing.xxs,
                                        horizontal = Spacing.s,
                                    ),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = LocalStrings.current.configureActionsSideStart,
                            )
                        }
                    }
                    itemsIndexed(uiState.actionsOnSwipeToStartComments) { idx, action ->
                        ConfigureActionItem(
                            icon =
                                when (idx) {
                                    1 -> Icons.Default.KeyboardDoubleArrowLeft
                                    else -> Icons.AutoMirrored.Default.KeyboardArrowLeft
                                },
                            action = action,
                            options =
                                buildList {
                                    this +=
                                        Option(
                                            OptionId.Remove,
                                            LocalStrings.current.commentActionDelete,
                                        )
                                },
                            onOptionSelected = { optionId ->
                                when (optionId) {
                                    OptionId.Remove -> {
                                        model.reduce(
                                            ConfigureSwipeActionsMviModel.Intent.DeleteActionComments(
                                                value = action,
                                                direction = ActionOnSwipeDirection.ToStart,
                                            ),
                                        )
                                    }

                                    else -> Unit
                                }
                            },
                        )
                    }
                    if (uiState.availableOptionsComments.isNotEmpty() && uiState.actionsOnSwipeToStartComments.size < 2) {
                        item {
                            ConfigureAddAction {
                                selectActionBottomSheet =
                                    ActionConfig(
                                        direction = ActionOnSwipeDirection.ToStart,
                                        target = ActionOnSwipeTarget.Comments,
                                    )
                            }
                        }
                    }
                    item {
                        Row(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        vertical = Spacing.xxs,
                                        horizontal = Spacing.s,
                                    ),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = LocalStrings.current.configureActionsSideEnd,
                            )
                        }
                    }
                    itemsIndexed(uiState.actionsOnSwipeToEndComments) { idx, action ->
                        ConfigureActionItem(
                            icon =
                                when (idx) {
                                    1 -> Icons.Default.KeyboardDoubleArrowRight
                                    else -> Icons.AutoMirrored.Default.KeyboardArrowRight
                                },
                            action = action,
                            options =
                                buildList {
                                    this +=
                                        Option(
                                            OptionId.Remove,
                                            LocalStrings.current.commentActionDelete,
                                        )
                                },
                            onOptionSelected = { optionId ->
                                when (optionId) {
                                    OptionId.Remove -> {
                                        model.reduce(
                                            ConfigureSwipeActionsMviModel.Intent.DeleteActionComments(
                                                value = action,
                                                direction = ActionOnSwipeDirection.ToEnd,
                                            ),
                                        )
                                    }

                                    else -> Unit
                                }
                            },
                        )
                    }
                    if (uiState.availableOptionsComments.isNotEmpty() && uiState.actionsOnSwipeToEndComments.size < 2) {
                        item {
                            ConfigureAddAction {
                                selectActionBottomSheet =
                                    ActionConfig(
                                        direction = ActionOnSwipeDirection.ToEnd,
                                        target = ActionOnSwipeTarget.Comments,
                                    )
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(Spacing.interItem))
                    }

                    // inbox
                    item {
                        SettingsHeader(
                            title = LocalStrings.current.navigationInbox,
                            icon = Icons.Default.Mail,
                            rightButton = @Composable {
                                TextButton(
                                    contentPadding =
                                        PaddingValues(
                                            horizontal = Spacing.xs,
                                            vertical = Spacing.xxs,
                                        ),
                                    onClick = {
                                        model.reduce(ConfigureSwipeActionsMviModel.Intent.ResetActionsInbox)
                                    },
                                ) {
                                    Text(
                                        text = LocalStrings.current.buttonReset,
                                        style = MaterialTheme.typography.labelSmall,
                                    )
                                }
                            },
                        )
                    }
                    item {
                        Row(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        vertical = Spacing.xxs,
                                        horizontal = Spacing.s,
                                    ),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = LocalStrings.current.configureActionsSideStart,
                            )
                        }
                    }
                    itemsIndexed(uiState.actionsOnSwipeToStartInbox) { idx, action ->
                        ConfigureActionItem(
                            icon =
                                when (idx) {
                                    1 -> Icons.Default.KeyboardDoubleArrowLeft
                                    else -> Icons.AutoMirrored.Default.KeyboardArrowLeft
                                },
                            action = action,
                            options =
                                buildList {
                                    this +=
                                        Option(
                                            OptionId.Remove,
                                            LocalStrings.current.commentActionDelete,
                                        )
                                },
                            onOptionSelected = { optionId ->
                                when (optionId) {
                                    OptionId.Remove -> {
                                        model.reduce(
                                            ConfigureSwipeActionsMviModel.Intent.DeleteActionInbox(
                                                value = action,
                                                direction = ActionOnSwipeDirection.ToStart,
                                            ),
                                        )
                                    }

                                    else -> Unit
                                }
                            },
                        )
                    }
                    if (uiState.availableOptionsInbox.isNotEmpty() && uiState.actionsOnSwipeToStartInbox.size < 2) {
                        item {
                            ConfigureAddAction {
                                selectActionBottomSheet =
                                    ActionConfig(
                                        direction = ActionOnSwipeDirection.ToStart,
                                        target = ActionOnSwipeTarget.Inbox,
                                    )
                            }
                        }
                    }
                    item {
                        Row(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        vertical = Spacing.xxs,
                                        horizontal = Spacing.s,
                                    ),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = LocalStrings.current.configureActionsSideEnd,
                            )
                        }
                    }
                    itemsIndexed(uiState.actionsOnSwipeToEndInbox) { idx, action ->
                        ConfigureActionItem(
                            icon =
                                when (idx) {
                                    1 -> Icons.Default.KeyboardDoubleArrowRight
                                    else -> Icons.AutoMirrored.Default.KeyboardArrowRight
                                },
                            action = action,
                            options =
                                buildList {
                                    this +=
                                        Option(
                                            OptionId.Remove,
                                            LocalStrings.current.commentActionDelete,
                                        )
                                },
                            onOptionSelected = { optionId ->
                                when (optionId) {
                                    OptionId.Remove -> {
                                        model.reduce(
                                            ConfigureSwipeActionsMviModel.Intent.DeleteActionInbox(
                                                value = action,
                                                direction = ActionOnSwipeDirection.ToEnd,
                                            ),
                                        )
                                    }

                                    else -> Unit
                                }
                            },
                        )
                    }
                    if (uiState.availableOptionsInbox.isNotEmpty() && uiState.actionsOnSwipeToEndInbox.size < 2) {
                        item {
                            ConfigureAddAction {
                                selectActionBottomSheet =
                                    ActionConfig(
                                        direction = ActionOnSwipeDirection.ToEnd,
                                        target = ActionOnSwipeTarget.Inbox,
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

        selectActionBottomSheet?.also { config ->
            val values =
                when (config.target) {
                    ActionOnSwipeTarget.Comments -> uiState.availableOptionsComments
                    ActionOnSwipeTarget.Inbox -> uiState.availableOptionsInbox
                    ActionOnSwipeTarget.Posts -> uiState.availableOptionsPosts
                }
            CustomModalBottomSheet(
                title = LocalStrings.current.selectActionTitle,
                items =
                    values.map { value ->
                        CustomModalBottomSheetItem(
                            label = value.toReadableName(),
                            trailingContent = {
                                val icon = value.toIcon()
                                if (icon != null) {
                                    Icon(
                                        modifier = Modifier.size(IconSize.m).then(value.toModifier()),
                                        imageVector = icon,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onBackground,
                                    )
                                }
                            },
                        )
                    },
                onSelected = { index ->
                    selectActionBottomSheet = null
                    if (index != null) {
                        val value = values[index]
                        notificationCenter.send(
                            NotificationCenterEvent.ActionsOnSwipeSelected(
                                value = value,
                                direction = config.direction,
                                target = config.target,
                            ),
                        )
                    }
                },
            )
        }
    }
}
