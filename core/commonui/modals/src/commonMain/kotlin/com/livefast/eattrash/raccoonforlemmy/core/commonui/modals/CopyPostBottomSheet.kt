package com.livefast.eattrash.raccoonforlemmy.core.commonui.modals

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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

class CopyPostBottomSheet(
    private val title: String? = null,
    private val text: String? = null,
) : Screen {
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
                BottomSheetHeader(LocalStrings.current.actionCopyClipboard)
                Column(
                    modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(Spacing.xxs),
                ) {
                    val titleCanBeCopied = !title.isNullOrBlank()
                    val textCanBeCopied = !text.isNullOrBlank()
                    if (titleCanBeCopied) {
                        Row(
                            modifier =
                                Modifier
                                    .clip(RoundedCornerShape(CornerSize.xxl))
                                    .onClick(
                                        onClick = {
                                            val event =
                                                NotificationCenterEvent.CopyText(title.orEmpty())
                                            notificationCenter.send(event)
                                            navigationCoordinator.hideBottomSheet()
                                        },
                                    ).padding(
                                        horizontal = Spacing.s,
                                        vertical = Spacing.s,
                                    ).fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = LocalStrings.current.copyTitle,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                    }
                    if (textCanBeCopied) {
                        Row(
                            modifier =
                                Modifier
                                    .padding(
                                        horizontal = Spacing.s,
                                        vertical = Spacing.s,
                                    ).fillMaxWidth()
                                    .onClick(
                                        onClick = {
                                            val event = NotificationCenterEvent.CopyText(text.orEmpty())
                                            notificationCenter.send(event)
                                            navigationCoordinator.hideBottomSheet()
                                        },
                                    ),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = LocalStrings.current.copyText,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                        if (titleCanBeCopied) {
                            val textToCopy =
                                buildString {
                                    append(title)
                                    append("\n")
                                    append(text)
                                }
                            Row(
                                modifier =
                                    Modifier
                                        .padding(
                                            horizontal = Spacing.s,
                                            vertical = Spacing.s,
                                        ).fillMaxWidth()
                                        .onClick(
                                            onClick = {
                                                val event = NotificationCenterEvent.CopyText(textToCopy)
                                                notificationCenter.send(event)
                                                navigationCoordinator.hideBottomSheet()
                                            },
                                        ),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = LocalStrings.current.copyBoth,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onBackground,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
