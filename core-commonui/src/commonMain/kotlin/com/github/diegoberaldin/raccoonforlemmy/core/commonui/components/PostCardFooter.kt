package com.github.diegoberaldin.raccoonforlemmy.core.commonui.components

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
import com.github.diegoberaldin.racconforlemmy.core.utils.onClick
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing

@Composable
fun PostCardFooter(
    comments: Int? = null,
    score: Int,
    saved: Boolean = false,
    upVoted: Boolean = false,
    downVoted: Boolean = false,
    onUpVote: ((Boolean) -> Unit)? = null,
    onDownVote: ((Boolean) -> Unit)? = null,
    onSave: ((Boolean) -> Unit)? = null,
    onReply: (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier.padding(horizontal = Spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.xxs),
    ) {
        val buttonModifier = Modifier.size(28.dp).padding(4.dp)
        if (comments != null) {
            Image(
                modifier = buttonModifier.onClick {
                    onReply?.invoke()
                },
                imageVector = Icons.Default.Chat,
                contentDescription = null,
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSurface),
            )
            Text(
                modifier = Modifier.padding(end = Spacing.s),
                text = "$comments",
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Image(
            modifier = buttonModifier.onClick {
                onSave?.invoke(!saved)
            },
            imageVector = if (!saved) {
                Icons.Default.BookmarkBorder
            } else {
                Icons.Default.Bookmark
            },
            contentDescription = null,
            colorFilter = ColorFilter.tint(
                color = if (saved) {
                    MaterialTheme.colorScheme.secondary
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
            ),
        )
        Image(
            modifier = buttonModifier.onClick {
                onUpVote?.invoke(!upVoted)
            },
            imageVector = if (upVoted) {
                Icons.Filled.ThumbUp
            } else {
                Icons.Outlined.ThumbUp
            },
            contentDescription = null,
            colorFilter = ColorFilter.tint(
                color = if (upVoted) {
                    MaterialTheme.colorScheme.secondary
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
            ),
        )
        Text(
            text = "$score",
        )
        Image(
            modifier = buttonModifier.onClick {
                onDownVote?.invoke(!downVoted)
            },
            imageVector = if (downVoted) {
                Icons.Filled.ThumbDown
            } else {
                Icons.Outlined.ThumbDown
            },
            contentDescription = null,
            colorFilter = ColorFilter.tint(
                color = if (downVoted) {
                    MaterialTheme.colorScheme.secondary
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
            ),
        )
    }
}
