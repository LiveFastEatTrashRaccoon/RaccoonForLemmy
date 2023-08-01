package com.github.diegoberaldin.raccoonforlemmy.feature_home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.github.diegoberaldin.racconforlemmy.core_utils.onClick
import com.github.diegoberaldin.raccoonforlemmy.core_appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.domain_lemmy.data.PostModel

@Composable
internal fun PostCardFooter(
    post: PostModel,
    onUpVote: (Boolean) -> Unit,
    onDownVote: (Boolean) -> Unit,
    onSave: (Boolean) -> Unit,
    onReply: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.xxs),
    ) {
        val buttonModifier = Modifier.size(42.dp).padding(8.dp)
        Image(
            modifier = buttonModifier.onClick(onReply),
            imageVector = Icons.Default.Chat,
            contentDescription = null,
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface),
        )
        Text(
            modifier = Modifier.padding(end = Spacing.s),
            text = "${post.comments}",
        )
        Spacer(modifier = Modifier.weight(1f))
        Image(
            modifier = buttonModifier.onClick {
                onSave(!post.saved)
            },
            imageVector = if (!post.saved) {
                Icons.Default.BookmarkBorder
            } else {
                Icons.Default.Bookmark
            },
            contentDescription = null,
            colorFilter = ColorFilter.tint(
                color = if (post.saved) {
                    MaterialTheme.colorScheme.secondary
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
            ),
        )
        val upvoted = post.myVote > 0
        val downvoted = post.myVote < 0
        Image(
            modifier = buttonModifier.onClick {
                onUpVote(!upvoted)
            },
            imageVector = if (upvoted) {
                Icons.Filled.ThumbUp
            } else {
                Icons.Outlined.ThumbUp
            },
            contentDescription = null,
            colorFilter = ColorFilter.tint(
                color = if (upvoted) {
                    MaterialTheme.colorScheme.secondary
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
            ),
        )
        Text(
            text = "${post.score}",
        )
        Image(
            modifier = buttonModifier.onClick {
                onDownVote(!downvoted)
            },
            imageVector = if (downvoted) {
                Icons.Filled.ThumbDown
            } else {
                Icons.Outlined.ThumbDown
            },
            contentDescription = null,
            colorFilter = ColorFilter.tint(
                color = if (downvoted) {
                    MaterialTheme.colorScheme.secondary
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
            ),
        )
    }
}
