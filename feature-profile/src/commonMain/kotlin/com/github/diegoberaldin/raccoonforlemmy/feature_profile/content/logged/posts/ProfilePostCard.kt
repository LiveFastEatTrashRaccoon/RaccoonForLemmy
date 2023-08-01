package com.github.diegoberaldin.raccoonforlemmy.feature_profile.content.logged.posts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
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
import com.github.diegoberaldin.raccoonforlemmy.domain_lemmy.data.PostModel
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

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
            verticalArrangement = Arrangement.spacedBy(Spacing.s),
        ) {
            Text(
                text = post.title,
                style = MaterialTheme.typography.titleMedium,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.xxs),
            ) {
                val communityName = post.community?.name.orEmpty()
                val communityIcon = post.community?.icon.orEmpty()
                val communityHost = post.community?.host.orEmpty()
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
            val imageUrl = post.thumbnailUrl.orEmpty()
            if (imageUrl.isNotEmpty()) {
                val painterResource = asyncPainterResource(data = imageUrl)
                KamelImage(
                    modifier = Modifier.fillMaxWidth().heightIn(min = 200.dp),
                    resource = painterResource,
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                )
            }
            val body = post.text
            if (body.isNotEmpty()) {
                Markdown(content = body)
            }
        }
    }
}
