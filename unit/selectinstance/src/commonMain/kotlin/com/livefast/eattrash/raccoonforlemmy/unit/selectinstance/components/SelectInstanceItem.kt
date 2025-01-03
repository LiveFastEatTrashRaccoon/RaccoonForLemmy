package com.livefast.eattrash.raccoonforlemmy.unit.selectinstance.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.IconSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.ancillaryTextAlpha
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.CustomDropDown
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.Option
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.OptionId
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.onClick
import com.livefast.eattrash.raccoonforlemmy.core.utils.toLocalDp
import sh.calvin.reorderable.ReorderableCollectionItemScope

@Composable
internal fun SelectInstanceItem(
    modifier: Modifier = Modifier,
    reorderableScope: ReorderableCollectionItemScope,
    instance: String,
    isActive: Boolean = false,
    onClick: (() -> Unit)? = null,
    onDragStarted: (() -> Unit)? = null,
    options: List<Option> = emptyList(),
    onOptionSelected: ((OptionId) -> Unit)? = null,
) {
    var optionsOffset by remember { mutableStateOf(Offset.Zero) }
    var optionsMenuOpen by remember { mutableStateOf(false) }
    val fullColor = MaterialTheme.colorScheme.onBackground
    val ancillaryColor = MaterialTheme.colorScheme.onBackground.copy(alpha = ancillaryTextAlpha)

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .onClick(
                    onClick = {
                        onClick?.invoke()
                    },
                ).padding(
                    top = Spacing.s,
                    bottom = Spacing.s,
                    start = Spacing.m,
                ),
        horizontalArrangement = Arrangement.spacedBy(Spacing.s),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = instance,
            color = fullColor,
        )

        Spacer(modifier = Modifier.weight(1f))

        if (isActive) {
            RadioButton(
                modifier =
                    Modifier.padding(
                        top = Spacing.s,
                        bottom = Spacing.s,
                        end = 10.dp,
                    ),
                selected = true,
                onClick = null,
            )
        }

        if (options.isNotEmpty()) {
            Box {
                IconButton(
                    modifier =
                        Modifier
                            .then(
                                with(reorderableScope) {
                                    Modifier.draggableHandle(
                                        onDragStarted = {
                                            onDragStarted?.invoke()
                                        },
                                    )
                                },
                            ).onGloballyPositioned {
                                optionsOffset = it.positionInParent()
                            },
                    onClick = {
                        optionsMenuOpen = true
                    },
                ) {
                    Icon(
                        modifier =
                            Modifier
                                .size(IconSize.m)
                                .padding(Spacing.xs),
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
