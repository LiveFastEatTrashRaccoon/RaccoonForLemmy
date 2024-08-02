package com.livefast.eattrash.raccoonforlemmy.unit.manageban.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.CornerSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.IconSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.shimmerEffect

@Composable
internal fun ManageBanItemPlaceholder() {
    Row(
        modifier =
            Modifier.padding(
                vertical = Spacing.xs,
                horizontal = Spacing.s,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
    ) {
        Box(
            modifier =
                Modifier
                    .padding(Spacing.xxxs)
                    .size(IconSize.l)
                    .clip(CircleShape)
                    .shimmerEffect(),
        )
        Box(
            modifier =
                Modifier
                    .padding(start = Spacing.xs)
                    .height(40.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(CornerSize.s))
                    .shimmerEffect(),
        )
    }
}
