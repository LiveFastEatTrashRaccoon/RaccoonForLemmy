package com.livefast.eattrash.raccoonforlemmy.unit.about.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.CornerSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.onClick

@Composable
internal fun AboutItem(
    text: String,
    modifier: Modifier = Modifier,
    painter: Painter? = null,
    vector: ImageVector? = null,
    textDecoration: TextDecoration = TextDecoration.None,
    value: String = "",
    onClick: (() -> Unit)? = null,
) {
    Row(
        modifier =
        modifier
            .clip(RoundedCornerShape(CornerSize.xxl))
            .onClick(
                onClick = {
                    onClick?.invoke()
                },
            ).padding(
                horizontal = Spacing.xs,
                vertical = Spacing.s,
            ),
        horizontalArrangement = Arrangement.spacedBy(Spacing.s),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val imageModifier = Modifier.size(22.dp)
        if (painter != null) {
            Icon(
                modifier = imageModifier,
                painter = painter,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
            )
        } else if (vector != null) {
            Icon(
                modifier = imageModifier,
                imageVector = vector,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            textDecoration = textDecoration,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Spacer(modifier = Modifier.weight(1f))

        if (value.isNotEmpty()) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}
