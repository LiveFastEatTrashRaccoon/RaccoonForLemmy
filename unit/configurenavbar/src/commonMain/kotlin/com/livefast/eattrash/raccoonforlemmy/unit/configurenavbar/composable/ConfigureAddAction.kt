package com.livefast.eattrash.raccoonforlemmy.unit.configurenavbar.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.IconSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.ancillaryTextAlpha
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.onClick

@Composable
internal fun ConfigureAddAction(onAdd: () -> Unit) {
    val ancillaryColor = MaterialTheme.colorScheme.onBackground.copy(alpha = ancillaryTextAlpha)
    Row(
        modifier =
        Modifier
            .padding(
                horizontal = Spacing.s,
                vertical = Spacing.xs,
            ).onClick(onClick = onAdd),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(IconSize.m),
            imageVector = Icons.Outlined.Add,
            contentDescription = null,
            tint = ancillaryColor,
        )
        Spacer(modifier = Modifier.width(Spacing.xs))
        Text(
            text = LocalStrings.current.buttonAdd,
            style = MaterialTheme.typography.labelMedium,
        )
    }
}
