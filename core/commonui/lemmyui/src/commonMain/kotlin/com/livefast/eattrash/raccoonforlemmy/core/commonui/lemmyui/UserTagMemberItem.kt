package com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.IconSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.ancillaryTextAlpha
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.CustomDropDown
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.UserTagMemberModel
import com.livefast.eattrash.raccoonforlemmy.core.utils.toLocalDp

@Composable
fun UserTagMemberItem(
    member: UserTagMemberModel,
    modifier: Modifier = Modifier,
    options: List<Option> = emptyList(),
    onOptionSelected: ((OptionId) -> Unit)? = null,
) {
    val title = member.username
    val fullColor = MaterialTheme.colorScheme.onBackground
    val ancillaryColor = MaterialTheme.colorScheme.onBackground.copy(alpha = ancillaryTextAlpha)
    var optionsOffset by remember { mutableStateOf(Offset.Zero) }
    var optionsMenuOpen by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.padding(Spacing.s),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
    ) {
        Column(
            modifier = Modifier.weight(1f).padding(start = Spacing.xs),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = fullColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        if (options.isNotEmpty()) {
            Box {
                IconButton(
                    modifier =
                        Modifier
                            .size(IconSize.m)
                            .padding(Spacing.xs)
                            .onGloballyPositioned {
                                optionsOffset = it.positionInParent()
                            },
                    onClick = {
                        optionsMenuOpen = true
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = LocalStrings.current.actionOpenOptionMenu,
                        tint = ancillaryColor,
                    )
                }

                CustomDropDown(
                    expanded = optionsMenuOpen,
                    onDismiss = {
                        optionsMenuOpen = false
                    },
                    offset =
                        DpOffset(
                            x = optionsOffset.x.toLocalDp(),
                            y = optionsOffset.y.toLocalDp(),
                        ),
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            text = {
                                Text(option.text)
                            },
                            onClick = {
                                optionsMenuOpen = false
                                onOptionSelected?.invoke(option.id)
                            },
                        )
                    }
                }
            }
        }
    }
}
