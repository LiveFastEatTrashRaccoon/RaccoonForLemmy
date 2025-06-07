package com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.CornerSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.ancillaryTextAlpha

@Composable
fun SettingsSwitchRow(
    title: String,
    value: Boolean,
    onChangeValue: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
) {
    Row(
        modifier =
        modifier
            .clip(
                shape = RoundedCornerShape(CornerSize.xxl),
            ).toggleable(
                value = value,
                role = Role.Checkbox,
                enabled = true,
                onValueChange = {
                    onChangeValue(!value)
                },
            ).padding(horizontal = Spacing.m),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing.xxxs),
        ) {
            val fullColor = MaterialTheme.colorScheme.onBackground
            val ancillaryColor = MaterialTheme.colorScheme.onBackground.copy(alpha = ancillaryTextAlpha)
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
        Switch(
            modifier = Modifier.padding(start = Spacing.xs, top = Spacing.xs),
            checked = value,
            onCheckedChange = null,
        )
    }
}
