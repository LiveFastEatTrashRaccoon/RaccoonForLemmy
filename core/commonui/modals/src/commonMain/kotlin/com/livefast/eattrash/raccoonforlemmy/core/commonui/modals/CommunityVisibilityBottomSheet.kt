package com.livefast.eattrash.raccoonforlemmy.core.commonui.modals

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import cafe.adriel.voyager.core.screen.Screen
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.CornerSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.BottomSheetHeader
import com.livefast.eattrash.raccoonforlemmy.core.l10n.messages.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.notifications.NotificationCenterEvent
import com.livefast.eattrash.raccoonforlemmy.core.notifications.di.getNotificationCenter
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.onClick
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommunityVisibilityType
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toIcon
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.toReadableName

class CommunityVisibilityBottomSheet : Screen {
    @Composable
    override fun Content() {
        val navigationCoordinator = remember { getNavigationCoordinator() }
        val notificationCenter = remember { getNotificationCenter() }

        Surface {
            Column(
                modifier =
                    Modifier
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .padding(
                            top = Spacing.s,
                            start = Spacing.s,
                            end = Spacing.s,
                            bottom = Spacing.m,
                        ),
                verticalArrangement = Arrangement.spacedBy(Spacing.s),
            ) {
                BottomSheetHeader(LocalStrings.current.editCommunityItemVisibility)
                val values =
                    listOf(
                        CommunityVisibilityType.LocalOnly,
                        CommunityVisibilityType.Public,
                    )
                Column(
                    modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(Spacing.xxs),
                ) {
                    for (value in values) {
                        Row(
                            modifier =
                                Modifier
                                    .clip(RoundedCornerShape(CornerSize.xxl))
                                    .onClick(
                                        onClick = {
                                            notificationCenter.send(
                                                NotificationCenterEvent.ChangeCommunityVisibility(
                                                    value = value,
                                                ),
                                            )
                                            navigationCoordinator.hideBottomSheet()
                                        },
                                    ).padding(
                                        horizontal = Spacing.s,
                                        vertical = Spacing.s,
                                    ).fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = value.toReadableName(),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                imageVector = value.toIcon(),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                    }
                }
            }
        }
    }
}
