package com.livefast.eattrash.raccoonforlemmy.unit.acknowledgements.components

import androidx.compose.foundation.layout.Arrangement
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
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.IconSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.ancillaryTextAlpha
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.CustomImage
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.PlaceholderImage
import com.livefast.eattrash.raccoonforlemmy.unit.acknowledgements.models.AcknowledgementModel

@Composable
fun AcknowledgementItem(item: AcknowledgementModel, modifier: Modifier = Modifier) {
    val title = item.title.orEmpty()
    val fullColor = MaterialTheme.colorScheme.onBackground
    val ancillaryColor = MaterialTheme.colorScheme.onBackground.copy(alpha = ancillaryTextAlpha)
    val iconSize = IconSize.xl

    Row(
        modifier =
        modifier.padding(
            vertical = Spacing.xs,
            horizontal = Spacing.s,
        ),
        horizontalArrangement = Arrangement.spacedBy(Spacing.m),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (!item.avatar.isNullOrEmpty()) {
            item.avatar.also { url ->
                CustomImage(
                    modifier =
                    Modifier
                        .size(iconSize)
                        .clip(RoundedCornerShape(iconSize / 2)),
                    contentDescription = null,
                    url = url,
                    autoload = true,
                )
            }
        } else {
            PlaceholderImage(
                size = iconSize,
                title = title,
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing.xs),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = fullColor,
            )
            if (!item.subtitle.isNullOrEmpty()) {
                Text(
                    text = item.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = ancillaryColor,
                )
            }
        }
    }
}
