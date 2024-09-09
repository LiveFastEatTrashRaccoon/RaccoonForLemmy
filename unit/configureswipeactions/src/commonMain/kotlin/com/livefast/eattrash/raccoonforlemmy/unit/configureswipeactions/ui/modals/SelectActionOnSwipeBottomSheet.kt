package com.livefast.eattrash.raccoonforlemmy.unit.configureswipeactions.ui.modals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.BottomSheetHeader
import com.livefast.eattrash.raccoonforlemmy.core.l10n.messages.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.notifications.di.getNotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.ActionOnSwipe
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.ActionOnSwipeDirection
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.ActionOnSwipeTarget
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.toIcon
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.toReadableName
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.onClick

class SelectActionOnSwipeBottomSheet(
    private val values: List<ActionOnSwipe>,
    private val direction: ActionOnSwipeDirection,
    private val target: ActionOnSwipeTarget,
) : Screen {
    @Composable
    override fun Content() {
        val navigationCoordinator = remember { getNavigationCoordinator() }
        val notificationCenter = remember { getNotificationCenter() }

        Column(
            modifier =
                Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(
                        top = Spacing.s,
                        start = Spacing.s,
                        end = Spacing.s,
                        bottom = Spacing.m,
                    ),
            verticalArrangement = Arrangement.spacedBy(Spacing.s),
        ) {
            BottomSheetHeader(LocalStrings.current.selectActionTitle)
            Column(
                modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(Spacing.xxs),
            ) {
                for (value in values) {
                    Row(
                        modifier =
                            Modifier
                                .padding(
                                    horizontal = Spacing.s,
                                    vertical = Spacing.s,
                                ).fillMaxWidth()
                                .onClick(
                                    onClick = {
                                        notificationCenter.send(
                                            NotificationCenterEvent.ActionsOnSwipeSelected(
                                                value = value,
                                                direction = direction,
                                                target = target,
                                            ),
                                        )
                                        navigationCoordinator.hideBottomSheet()
                                    },
                                ),
                    ) {
                        Text(
                            text = value.toReadableName(),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        value.toIcon()?.also { icon ->
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(Spacing.m))
            }
        }
    }
}
