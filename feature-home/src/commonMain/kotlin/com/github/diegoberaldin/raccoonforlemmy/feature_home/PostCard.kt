package com.github.diegoberaldin.raccoonforlemmy.feature_home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.github.diegoberaldin.raccoonforlemmy.core_appearance.theme.CornerSize
import com.github.diegoberaldin.raccoonforlemmy.core_appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.core_md.compose.Markdown
import com.github.diegoberaldin.raccoonforlemmy.data.PostModel
import com.seiko.imageloader.rememberImagePainter

@Composable
fun PostCard(
    post: PostModel,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(CornerSize.m)
            ).padding(
                vertical = Spacing.lHalf,
                horizontal = Spacing.s,
            )
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.s)
        ) {
            Text(
                text = post.title,
                style = MaterialTheme.typography.titleMedium,
            )

            val communityName = post.community?.name.orEmpty()
            val communityIcon = post.community?.icon.orEmpty()
            val iconSize = 21.dp
            if (communityName.isNotEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Spacing.s),
                ) {
                    if (communityIcon.isNotEmpty()) {
                        val painter = rememberImagePainter(communityIcon)
                        Image(
                            modifier = Modifier.size(iconSize)
                                .clip(RoundedCornerShape(iconSize / 2)),
                            painter = painter,
                            contentDescription = null,
                            contentScale = ContentScale.FillWidth,
                        )
                    }
                    Text(
                        text = communityName,
                        style = MaterialTheme.typography.titleSmall,
                    )
                }
            }

            val imageUrl = post.thumbnailUrl
            if (!imageUrl.isNullOrEmpty()) {
                val painter = rememberImagePainter(imageUrl)
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                )
            }
            val body = post.text
            if (body.isNotEmpty()) {
                Markdown(content = body)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.xxs),
            ) {
                val buttonModifier = Modifier.size(42.dp)
                Image(
                    modifier = buttonModifier,
                    imageVector = Icons.Default.ArrowDropUp,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface)
                )
                Text(
                    text = "${post.score}"
                )
                Image(
                    modifier = buttonModifier,
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface)
                )
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    modifier = buttonModifier.padding(10.dp),
                    imageVector = Icons.Default.Chat,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface)
                )
                Text(
                    text = "${post.comments}"
                )
            }
        }
    }
}