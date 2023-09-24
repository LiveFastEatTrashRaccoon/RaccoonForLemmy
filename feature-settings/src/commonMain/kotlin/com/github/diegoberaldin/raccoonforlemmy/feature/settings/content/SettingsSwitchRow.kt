package com.github.diegoberaldin.raccoonforlemmy.feature.settings.content

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
internal fun SettingsSwitchRow(
    title: String,
    value: Boolean,
    onValueChanged: (Boolean) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.weight(1f))
        Switch(
            checked = value,
            onCheckedChange = {
                onValueChanged(it)
            }
        )
    }
}
