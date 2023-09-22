package com.github.diegoberaldin.raccoonforlemmy.core.commonui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import com.github.diegoberaldin.racconforlemmy.core.utils.onClick
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.di.getThemeRepository
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.CornerSize
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommentModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.PersonMentionModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.PostModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.UserModel

@Composable
fun InboxMentionCard(
    mention: PersonMentionModel,
    onOpenPost: (PostModel) -> Unit,
    onOpenCreator: (UserModel) -> Unit,
    onOpenCommunity: (CommunityModel) -> Unit,
    onUpVote: ((CommentModel) -> Unit)? = null,
    onDownVote: ((CommentModel) -> Unit)? = null,
) {
    val themeRepository = remember { getThemeRepository() }
    val fontScale by themeRepository.contentFontScale.collectAsState()
    CompositionLocalProvider(
        LocalDensity provides Density(
            density = LocalDensity.current.density,
            fontScale = fontScale,
        ),
    ) {
        Card(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(CornerSize.m),
            ).padding(
                vertical = Spacing.s,
                horizontal = Spacing.s,
            ).onClick {
                onOpenPost(mention.post)
            },
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing.xs),
            ) {
                InboxReplyHeader(
                    mention = mention,
                )
                PostCardBody(
                    text = mention.comment.text,
                )
                InboxReplySubtitle(
                    creator = mention.creator,
                    community = mention.community,
                    date = mention.publishDate,
                    score = mention.score,
                    upVoted = mention.myVote > 0,
                    downVoted = mention.myVote < 0,
                    onOpenCommunity = onOpenCommunity,
                    onOpenCreator = { user ->
                        onOpenCreator(user)
                    },
                    onUpVote = {
                        onUpVote?.invoke(mention.comment)
                    },
                    onDownVote = {
                        onDownVote?.invoke(mention.comment)
                    },
                )
            }
        }
    }
}
