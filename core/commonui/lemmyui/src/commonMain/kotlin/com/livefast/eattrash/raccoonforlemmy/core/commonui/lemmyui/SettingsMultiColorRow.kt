package com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.CornerSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.MultiColorPreview
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.onClick

@Composable
fun SettingsMultiColorRow(
    title: String,
    values: List<Color>,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    onTap: (() -> Unit)? = null,
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
        MultiColorPreview(
            modifier =
            Modifier
                .padding(start = Spacing.xs)
                .size(36.dp),
            colors = values,
        )
    }
}
