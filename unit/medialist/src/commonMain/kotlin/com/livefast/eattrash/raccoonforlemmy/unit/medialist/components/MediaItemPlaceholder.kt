package com.livefast.eattrash.raccoonforlemmy.unit.medialist.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.CornerSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.shimmerEffect

@Composable
internal fun MediaItemPlaceholder(modifier: Modifier = Modifier) {
    Column(
        modifier =
        modifier.padding(
            vertical = Spacing.xs,
            horizontal = Spacing.s,
        ),
    ) {
        Box(
            modifier =
            Modifier
                .height(60.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(CornerSize.s))
                .shimmerEffect(),
        )
    }
}
