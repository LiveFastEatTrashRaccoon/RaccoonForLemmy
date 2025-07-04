package com.livefast.eattrash.raccoonforlemmy.unit.configureswipeactions.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.unit.DpOffset
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.IconSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.ancillaryTextAlpha
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.CustomDropDown
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.Option
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.OptionId
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.ActionOnSwipe
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.toReadableName
import com.livefast.eattrash.raccoonforlemmy.core.utils.toLocalDp

@Composable
internal fun ConfigureActionItem(
    action: ActionOnSwipe,
    icon: ImageVector,
    iconContentDescription: String?,
    modifier: Modifier = Modifier,
    options: List<Option> = emptyList(),
    onSelectOption: ((OptionId) -> Unit)? = null,
) {
    var optionsExpanded by remember { mutableStateOf(false) }
    var optionsOffset by remember { mutableStateOf(Offset.Zero) }
    val ancillaryColor = MaterialTheme.colorScheme.onBackground.copy(alpha = ancillaryTextAlpha)

    Box(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = Spacing.s, vertical = Spacing.xs),
            horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.size(IconSize.m),
                imageVector = icon,
                contentDescription = iconContentDescription,
            )
            Text(
                text = action.toReadableName(),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.weight(1f))
            if (options.isNotEmpty()) {
                IconButton(
                    modifier =
                    Modifier
                        .size(IconSize.m)
                        .padding(Spacing.xs)
                        .onGloballyPositioned {
                            optionsOffset = it.positionInParent()
                        },
                    onClick = {
                        optionsExpanded = true
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = LocalStrings.current.actionOpenOptionMenu,
                        tint = ancillaryColor,
                    )
                }
            }
        }

        CustomDropDown(
            expanded = optionsExpanded,
            onDismiss = {
                optionsExpanded = false
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
                        optionsExpanded = false
                        onSelectOption?.invoke(option.id)
                    },
                )
            }
        }
    }
}
