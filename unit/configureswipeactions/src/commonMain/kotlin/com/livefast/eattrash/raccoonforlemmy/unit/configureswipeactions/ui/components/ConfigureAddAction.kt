package com.livefast.eattrash.raccoonforlemmy.unit.configureswipeactions.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.CornerSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.IconSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.ancillaryTextAlpha
import com.livefast.eattrash.raccoonforlemmy.core.l10n.messages.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.onClick

@Composable
internal fun ConfigureAddAction(onAdd: () -> Unit) {
    val ancillaryColor = MaterialTheme.colorScheme.onBackground.copy(alpha = ancillaryTextAlpha)
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(CornerSize.xxl))
                .onClick(onClick = onAdd)
                .padding(
                    horizontal = Spacing.s,
                    vertical = Spacing.xs,
                ),
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
            modifier = Modifier.weight(1f),
            text = LocalStrings.current.buttonAdd,
            style = MaterialTheme.typography.labelMedium,
        )
    }
}
