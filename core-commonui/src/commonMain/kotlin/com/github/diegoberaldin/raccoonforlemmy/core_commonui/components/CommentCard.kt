package com.github.diegoberaldin.raccoonforlemmy.core_commonui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.github.diegoberaldin.raccoonforlemmy.core_appearance.theme.CornerSize
import com.github.diegoberaldin.raccoonforlemmy.core_appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.core_md.compose.Markdown
import com.github.diegoberaldin.raccoonforlemmy.domain_lemmy.data.CommentModel
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun CommentCard(
    comment: CommentModel,
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
            verticalArrangement = Arrangement.spacedBy(Spacing.s),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.xxs),
            ) {
                val communityName = comment.community?.name.orEmpty()
                val communityIcon = comment.community?.icon.orEmpty()
                val communityHost = comment.community?.host.orEmpty()
                val iconSize = 16.dp
                if (communityName.isNotEmpty()) {
                    if (communityIcon.isNotEmpty()) {
                        val painterResource = asyncPainterResource(data = communityIcon)
                        KamelImage(
                            modifier = Modifier.size(iconSize)
                                .clip(RoundedCornerShape(iconSize / 2)),
                            resource = painterResource,
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds,
                        )
                    }
                    Text(
                        text = buildString {
                            append(communityName)
                            if (communityHost.isNotEmpty()) {
                                append("@$communityHost")
                            }
                        },
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
            val body = comment.text
            if (body.isNotEmpty()) {
                Markdown(content = body)
            }
        }
    }
}
