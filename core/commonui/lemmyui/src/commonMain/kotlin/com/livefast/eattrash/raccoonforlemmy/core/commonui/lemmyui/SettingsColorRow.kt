package com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.CornerSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.onClick

@Composable
fun SettingsColorRow(
    title: String,
    value: Color,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    onTap: (() -> Unit)? = null,
    onClear: (() -> Unit)? = null,
) {
    Row(
        modifier =
        modifier
            .clip(RoundedCornerShape(CornerSize.xxl))
            .onClick(
                onClick = {
                    onTap?.invoke()
                },
            ).padding(vertical = Spacing.s, horizontal = Spacing.m),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        if (onClear != null && value != Color.Transparent) {
            IconButton(
                onClick = {
                    onClear()
                },
            ) {
                Icon(
                    imageVector = Icons.Default.RemoveCircle,
                    contentDescription = LocalStrings.current.actionClear,
                    tint = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
        Box(
            modifier =
            Modifier
                .padding(start = Spacing.xs)
                .size(36.dp)
                .background(color = value, shape = CircleShape)
                .then(
                    if (value == Color.Transparent) {
                        Modifier.border(
                            color = MaterialTheme.colorScheme.onBackground,
                            width = Dp.Hairline,
                            shape = CircleShape,
                        )
                    } else {
                        Modifier
                    },
                ),
        )
    }
}
