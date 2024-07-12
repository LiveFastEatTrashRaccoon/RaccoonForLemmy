package com.github.diegoberaldin.raccoonforlemmy.unit.configurenavbar.composable

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
import androidx.compose.ui.unit.DpOffset
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.IconSize
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.ancillaryTextAlpha
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.CustomDropDown
import com.github.diegoberaldin.raccoonforlemmy.core.l10n.messages.LocalStrings
import com.github.diegoberaldin.raccoonforlemmy.core.utils.compose.onClick
import com.github.diegoberaldin.raccoonforlemmy.core.utils.toLocalDp
import sh.calvin.reorderable.ReorderableCollectionItemScope

@Composable
internal fun ConfigureNavBarItem(
    modifier: Modifier = Modifier,
    reorderableScope: ReorderableCollectionItemScope,
    title: String,
    onDragStarted: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null,
) {
    var optionsOffset by remember { mutableStateOf(Offset.Zero) }
    var optionsMenuOpen by remember { mutableStateOf(false) }
    val fullColor = MaterialTheme.colorScheme.onBackground
    val ancillaryColor = MaterialTheme.colorScheme.onBackground.copy(alpha = ancillaryTextAlpha)

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(
                    horizontal = Spacing.m,
                    vertical = Spacing.s,
                ),
        horizontalArrangement = Arrangement.spacedBy(Spacing.s),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            color = fullColor,
        )

        Spacer(modifier = Modifier.weight(1f))
        Box {
            Icon(
                modifier =
                    Modifier
                        .size(IconSize.m)
                        .padding(Spacing.xs)
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
                        }.onClick(
                            onClick = {
                                optionsMenuOpen = true
                            },
                        ),
                imageVector = Icons.Default.MoreVert,
                contentDescription = null,
                tint = ancillaryColor,
            )

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
                DropdownMenuItem(
                    text = {
                        Text(LocalStrings.current.commentActionDelete)
                    },
                    onClick = {
                        optionsMenuOpen = false
                        onDelete?.invoke()
                    },
                )
            }
        }
    }
}
