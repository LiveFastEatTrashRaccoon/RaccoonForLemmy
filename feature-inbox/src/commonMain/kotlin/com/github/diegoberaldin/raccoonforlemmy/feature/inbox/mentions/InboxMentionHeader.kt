package com.github.diegoberaldin.raccoonforlemmy.feature.inbox.mentions

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.PersonMentionModel
import com.github.diegoberaldin.raccoonforlemmy.resources.MR
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun InboxMentionHeader(mention: PersonMentionModel) {
    val header = buildAnnotatedString {
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append(mention.creator.name)
        }
        append(" ")
        if (mention.isOwnPost) {
            append(stringResource(MR.strings.inbox_item_reply_post))
        } else {
            append(stringResource(MR.strings.inbox_item_reply_comment))
        }
        append(" ")
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append(mention.post.title)
        }
    }
    Text(
        modifier = Modifier.padding(vertical = Spacing.xs),
        text = header,
        style = MaterialTheme.typography.bodySmall,
    )
}
