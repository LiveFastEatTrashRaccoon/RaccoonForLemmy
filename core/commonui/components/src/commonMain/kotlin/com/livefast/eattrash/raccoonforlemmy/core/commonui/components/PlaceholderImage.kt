package com.livefast.eattrash.raccoonforlemmy.core.commonui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.onClick

@Composable
fun PlaceholderImage(
    size: Dp,
    title: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    Box(
        modifier =
            modifier
                .clip(RoundedCornerShape(size / 2))
                .then(
                    if (onClick != null) {
                        Modifier.onClick(onClick = { onClick() })
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
        val translationAmount = with(LocalDensity.current) { 2.dp.toPx() }
        Text(
            modifier =
                Modifier.graphicsLayer {
                    translationY = -translationAmount
                },
            text = title.firstOrNull()?.toString().orEmpty().uppercase(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}
