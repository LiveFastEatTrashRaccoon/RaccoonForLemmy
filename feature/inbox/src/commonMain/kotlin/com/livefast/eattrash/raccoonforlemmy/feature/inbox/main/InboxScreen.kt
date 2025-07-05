package com.livefast.eattrash.raccoonforlemmy.feature.inbox.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.CornerSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.toWindowInsets
import com.livefast.eattrash.raccoonforlemmy.core.architecture.di.getViewModel
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.SectionSelector
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.CustomModalBottomSheet
import com.livefast.eattrash.raccoonforlemmy.core.commonui.modals.CustomModalBottomSheetItem
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getDrawerCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.notifications.di.getNotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.persistence.di.getSettingsRepository
import com.livefast.eattrash.raccoonforlemmy.unit.mentions.InboxMentionsScreen
import com.livefast.eattrash.raccoonforlemmy.unit.messages.InboxMessagesScreen
import com.livefast.eattrash.raccoonforlemmy.unit.replies.InboxRepliesScreen
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

object InboxScreen : Tab {
    override val options: TabOptions
        @Composable get() {
            return TabOptions(0u, "")
        }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val model: InboxMviModel = getViewModel<InboxViewModel>()
        val uiState by model.uiState.collectAsState()
        val topAppBarState = rememberTopAppBarState()
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)
        val drawerCoordinator = remember { getDrawerCoordinator() }
        val navigationCoordinator = remember { getNavigationCoordinator() }
        val settingsRepository = remember { getSettingsRepository() }
        val settings by settingsRepository.currentSettings.collectAsState()
        val scope = rememberCoroutineScope()
        val connection = navigationCoordinator.getBottomBarScrollConnection()
        val notificationCenter = remember { getNotificationCenter() }
        var inboxTypeBottomSheetOpened by remember { mutableStateOf(false) }
        val inboxReadAllSuccessMessage = LocalStrings.current.messageReadAllInboxSuccess

        LaunchedEffect(model) {
            model.effects
                .onEach { event ->
                    when (event) {
                        InboxMviModel.Effect.ReadAllInboxSuccess -> {
                            navigationCoordinator.showGlobalMessage(inboxReadAllSuccessMessage)
                        }

                        else -> Unit
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
                                scope.launch {
                                    drawerCoordinator.toggleDrawer()
                                }
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = LocalStrings.current.actionOpenSideMenu,
                            )
                        }
                    },
                    title = {
                        Column(
                            modifier =
                            Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(CornerSize.xl))
                                .clickable {
                                    inboxTypeBottomSheetOpened = true
                                }.padding(horizontal = Spacing.s),
                        ) {
                            Text(
                                text = LocalStrings.current.navigationInbox,
                                style = MaterialTheme.typography.titleMedium,
                            )
                            val text =
                                when (uiState.unreadOnly) {
                                    true -> LocalStrings.current.inboxListingTypeUnread
                                    else -> LocalStrings.current.inboxListingTypeAll
                                }
                            Text(
                                text = text,
                                style = MaterialTheme.typography.titleSmall,
                            )
                        }
                    },
                    actions = {
                        if (uiState.isLogged == true) {
                            IconButton(
                                onClick = {
                                    model.reduce(InboxMviModel.Intent.ReadAll)
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DoneAll,
                                    contentDescription = LocalStrings.current.actionMarkAllAsRead,
                                )
                            }
                        }
                    },
                )
            },
        ) { padding ->
            when (uiState.isLogged) {
                false -> {
                    Column(
                        modifier =
                        Modifier
                            .padding(
                                top = padding.calculateTopPadding(),
                            ).padding(horizontal = Spacing.m),
                    ) {
                        Text(
                            text = LocalStrings.current.inboxNotLoggedMessage,
                        )
                    }
                }

                true -> {
                    Column(
                        modifier =
                        Modifier
                            .padding(
                                top = padding.calculateTopPadding(),
                            ).then(
                                if (connection != null && settings.hideNavigationBarWhileScrolling) {
                                    Modifier.nestedScroll(connection)
                                } else {
                                    Modifier
                                },
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
                                buildString {
                                    append(LocalStrings.current.inboxSectionReplies)
                                    if (uiState.unreadReplies > 0) {
                                        append(" (")
                                        append(uiState.unreadReplies)
                                        append(")")
                                    }
                                },
                                buildString {
                                    append(LocalStrings.current.inboxSectionMentions)
                                    if (uiState.unreadMentions > 0) {
                                        append(" (")
                                        append(uiState.unreadMentions)
                                        append(")")
                                    }
                                },
                                buildString {
                                    append(LocalStrings.current.inboxSectionMessages)
                                    if (uiState.unreadMessages > 0) {
                                        append(" (")
                                        append(uiState.unreadMessages)
                                        append(")")
                                    }
                                },
                            ),
                            currentSection =
                            when (uiState.section) {
                                InboxSection.Mentions -> 1
                                InboxSection.Messages -> 2
                                else -> 0
                            },
                            onSectionSelected = {
                                val section =
                                    when (it) {
                                        1 -> InboxSection.Mentions
                                        2 -> InboxSection.Messages
                                        else -> InboxSection.Replies
                                    }
                                model.reduce(InboxMviModel.Intent.ChangeSection(section))
                            },
                        )
                        val screens =
                            remember {
                                listOf(
                                    InboxRepliesScreen(),
                                    InboxMentionsScreen(),
                                    InboxMessagesScreen(),
                                )
                            }
                        TabNavigator(screens.first()) {
                            CurrentScreen()
                            val navigator = LocalTabNavigator.current
                            LaunchedEffect(model) {
                                model.uiState
                                    .map { it.section }
                                    .onEach { section ->
                                        val index =
                                            when (section) {
                                                InboxSection.Replies -> 0
                                                InboxSection.Mentions -> 1
                                                InboxSection.Messages -> 2
                                            }
                                        navigator.current = screens[index]
                                    }.launchIn(this)
                            }
                        }
                    }
                }

                else -> Unit
            }
        }

        if (inboxTypeBottomSheetOpened) {
            val values =
                listOf(
                    LocalStrings.current.inboxListingTypeUnread,
                    LocalStrings.current.inboxListingTypeAll,
                )
            CustomModalBottomSheet(
                title = LocalStrings.current.inboxListingTypeTitle,
                items =
                values.map { value ->
                    CustomModalBottomSheetItem(label = value)
                },
                onSelect = { index ->
                    inboxTypeBottomSheetOpened = false
                    if (index != null) {
                        notificationCenter.send(
                            NotificationCenterEvent.ChangeInboxType(unreadOnly = index == 0),
                        )
                    }
                },
            )
        }
    }
}
