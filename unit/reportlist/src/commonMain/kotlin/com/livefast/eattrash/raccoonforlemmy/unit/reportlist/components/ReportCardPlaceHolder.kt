package com.livefast.eattrash.raccoonforlemmy.unit.reportlist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.livefast.eattrash.raccoonforlemmy.core.appearance.data.PostLayout
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.CornerSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.IconSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.utils.compose.shimmerEffect

@Composable
internal fun ReportCardPlaceHolder(postLayout: PostLayout = PostLayout.Card) {
    Column(
        modifier =
        Modifier.then(
            if (postLayout == PostLayout.Card) {
                Modifier
                    .padding(horizontal = Spacing.xs)
                    .shadow(
                        elevation = 5.dp,
                        shape = RoundedCornerShape(CornerSize.l),
                    )
                    .clip(RoundedCornerShape(CornerSize.l))
                    .background(
                        color = MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp),
                    )
                    .padding(vertical = Spacing.s)
            } else {
                Modifier
            },
        ),
        verticalArrangement = Arrangement.spacedBy(Spacing.xs),
    ) {
        Box(
            modifier =
            Modifier
                .height(IconSize.l)
                .fillMaxWidth()
                .clip(RoundedCornerShape(CornerSize.m))
                .shimmerEffect(),
        )
        Box(
            modifier =
            Modifier
                .padding(vertical = Spacing.xxxs)
                .height(80.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(CornerSize.m))
                .shimmerEffect(),
        )
        Box(
            modifier =
            Modifier
                .height(IconSize.l)
                .fillMaxWidth()
                .clip(RoundedCornerShape(CornerSize.m))
                .shimmerEffect(),
        )
    }
}
