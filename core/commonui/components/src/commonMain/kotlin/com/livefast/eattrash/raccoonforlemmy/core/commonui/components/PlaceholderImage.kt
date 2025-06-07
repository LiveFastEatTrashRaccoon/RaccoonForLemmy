package com.livefast.eattrash.raccoonforlemmy.core.commonui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing

@Composable
fun PlaceholderImage(size: Dp, title: String, modifier: Modifier = Modifier, onClick: (() -> Unit)? = null) {
    Box(
        modifier =
        modifier
            .then(
                if (onClick != null) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                },
            ).padding(Spacing.xxxs)
            .size(size)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(size / 2),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = title.firstOrNull()?.toString().orEmpty().uppercase(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
