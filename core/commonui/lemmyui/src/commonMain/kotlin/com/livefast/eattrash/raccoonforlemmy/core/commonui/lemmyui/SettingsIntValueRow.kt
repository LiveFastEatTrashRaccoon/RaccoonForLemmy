package com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.ancillaryTextAlpha
import com.livefast.eattrash.raccoonforlemmy.core.commonui.components.FeedbackButton
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings

@Composable
fun SettingsIntValueRow(
    title: String,
    value: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
) {
    val fullColor = MaterialTheme.colorScheme.onBackground
    val ancillaryColor = MaterialTheme.colorScheme.onBackground.copy(alpha = ancillaryTextAlpha)
    Row(
        modifier = modifier.padding(horizontal = Spacing.m),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier =
            Modifier
                .weight(1f)
                .padding(vertical = Spacing.s),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = fullColor,
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelMedium,
                    color = ancillaryColor,
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FeedbackButton(
                imageVector = Icons.Default.RemoveCircleOutline,
                tintColor = MaterialTheme.colorScheme.secondary,
                contentDescription = LocalStrings.current.actionDecrement,
                onClick = onDecrement,
            )
            Text(
                modifier =
                Modifier
                    .sizeIn(minWidth = 40.dp)
                    .padding(horizontal = Spacing.s),
                textAlign = TextAlign.Center,
                text = value.toString(),
                style = MaterialTheme.typography.bodyMedium,
                color = fullColor,
            )
            FeedbackButton(
                imageVector = Icons.Default.AddCircleOutline,
                tintColor = MaterialTheme.colorScheme.secondary,
                contentDescription = LocalStrings.current.actionIncrement,
                onClick = onIncrement,
            )
        }
    }
}
