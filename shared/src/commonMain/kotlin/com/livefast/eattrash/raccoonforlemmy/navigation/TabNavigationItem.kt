package com.livefast.eattrash.raccoonforlemmy.navigation

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextOverflow
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.IconSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.CustomImage
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.TabNavigationSection
import com.livefast.eattrash.raccoonforlemmy.core.navigation.di.getNavigationCoordinator
import com.livefast.eattrash.raccoonforlemmy.core.navigation.toIcon
import com.livefast.eattrash.raccoonforlemmy.core.navigation.toReadableName

@Composable
internal fun RowScope.TabNavigationItem(
    section: TabNavigationSection,
    withText: Boolean = true,
    customIconUrl: String? = null,
    onClick: (() -> Unit)? = null,
    onLongPress: (() -> Unit)? = null,
) {
    val navigationCoordinator = remember { getNavigationCoordinator() }
    val unread by navigationCoordinator.inboxUnread.collectAsState()
    val currentSection by navigationCoordinator.currentSection.collectAsState()
    val interactionSource = remember { MutableInteractionSource() }

    val pointerInputModifier =
        Modifier.pointerInput(Unit) {
            detectTapGestures(
                onPress = { offset ->
                    val press = PressInteraction.Press(offset)
                    interactionSource.emit(press)
                    tryAwaitRelease()
                    interactionSource.emit(PressInteraction.Release(press))
                },
                onTap = {
                    onClick?.invoke()
                },
                onLongPress = {
                    onLongPress?.invoke()
                },
            )
        }

    NavigationBarItem(
        onClick = {
            onClick?.invoke()
        },
        selected = section == currentSection,
        interactionSource = interactionSource,
        icon = {
            val content = @Composable {
                if (customIconUrl != null) {
                    val iconSize = IconSize.m

                    CustomImage(
                        modifier =
                            Modifier
                                .size(iconSize)
                                .clip(RoundedCornerShape(iconSize / 2))
                                .then(pointerInputModifier),
                        url = customIconUrl,
                        autoload = true,
                    )
                } else {
                    Icon(
                        modifier = pointerInputModifier,
                        imageVector = section.toIcon(),
                        contentDescription = null,
                    )
                }
            }
            val inboxTitle = LocalStrings.current.navigationInbox
            if (section.toReadableName() == inboxTitle && unread > 0) {
                BadgedBox(
                    badge = {
                        Badge(
                            modifier = Modifier.padding(top = Spacing.s),
                        ) {
                            Text(
                                text =
                                    if (unread <= 99) {
                                        unread.toString()
                                    } else {
                                        "99+"
                                    },
                                style = MaterialTheme.typography.labelSmall,
                            )
                        }
                    },
                ) {
                    content()
                }
            } else {
                content()
            }
        },
        label = {
            if (withText) {
                Text(
                    modifier = Modifier,
                    text = section.toReadableName(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        },
    )
}

