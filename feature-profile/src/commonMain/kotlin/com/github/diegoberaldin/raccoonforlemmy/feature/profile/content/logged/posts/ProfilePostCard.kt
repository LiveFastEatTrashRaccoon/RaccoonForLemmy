package com.github.diegoberaldin.raccoonforlemmy.feature.profile.content.logged.posts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.CornerSize
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.PostCardBody
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.PostCardFooter
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.PostCardImage
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.PostCardSubtitle
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.PostModel

@Composable
fun ProfilePostCard(
    post: PostModel,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(CornerSize.m),
            ).padding(
                vertical = Spacing.lHalf,
                horizontal = Spacing.s,
            ),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.xs),
        ) {
            Text(
                text = post.title,
                style = MaterialTheme.typography.titleMedium,
            )
            PostCardSubtitle(
                community = post.community,
            )
            PostCardImage(post)

            Box {
                PostCardBody(
                    modifier = Modifier.heightIn(max = 200.dp).padding(Spacing.xs),
                    text = post.text,
                )
                Box(
                    modifier = Modifier
                        .height(Spacing.s)
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.surface,
                                ),
                            ),
                        ),
                )
            }
            PostCardFooter(
                comments = post.comments,
                score = post.score,
                saved = post.saved,
                upVoted = post.myVote > 0,
                downVoted = post.myVote < 0,
            )
        }
    }
}
