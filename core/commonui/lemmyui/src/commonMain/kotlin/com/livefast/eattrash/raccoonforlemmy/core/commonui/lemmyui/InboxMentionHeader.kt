package com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.IconSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.ancillaryTextAlpha
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PersonMentionModel

@Composable
fun InboxCardHeader(mention: PersonMentionModel, type: InboxCardType, modifier: Modifier = Modifier) {
    val fullColor = MaterialTheme.colorScheme.onBackground
    val ancillaryColor = MaterialTheme.colorScheme.onBackground.copy(alpha = ancillaryTextAlpha)
    val header =
        buildAnnotatedString {
            withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = fullColor)) {
                append(mention.creator.name)
            }
            append(" ")
            withStyle(SpanStyle(color = ancillaryColor)) {
                when (type) {
                    InboxCardType.Mention -> {
                        append(LocalStrings.current.inboxItemMention)
                    }

                    InboxCardType.Reply -> {
                        if (mention.isCommentReply) {
                            append(LocalStrings.current.inboxItemReplyComment)
                        } else {
                            append(LocalStrings.current.inboxItemReplyPost)
                        }
                    }
                }

                append(" ")
                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = fullColor)) {
                    append(mention.post.title)
                }
            }
        }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier =
            Modifier
                .weight(1f)
                .padding(
                    vertical = Spacing.xs,
                    horizontal = Spacing.xs,
                ),
            text = header,
            style = MaterialTheme.typography.bodySmall,
            color = fullColor,
        )
        if (!mention.read) {
            Icon(
                modifier =
                Modifier
                    .padding(end = Spacing.s)
                    .size(IconSize.xs),
                imageVector = Icons.Filled.FiberManualRecord,
                contentDescription = LocalStrings.current.itemUnread,
                tint = ancillaryColor,
            )
        }
    }
}
