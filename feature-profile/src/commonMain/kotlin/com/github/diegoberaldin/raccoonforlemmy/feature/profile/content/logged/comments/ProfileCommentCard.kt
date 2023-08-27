package com.github.diegoberaldin.raccoonforlemmy.feature.profile.content.logged.comments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.CornerSize
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.PostCardBody
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.PostCardFooter
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.PostCardSubtitle
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommentModel

@Composable
fun ProfileCommentCard(
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
            PostCardSubtitle(
                community = comment.community,
            )
            PostCardBody(
                text = comment.text,
            )
            PostCardFooter(
                score = comment.score,
                saved = comment.saved,
                upVoted = comment.myVote > 0,
                downVoted = comment.myVote < 0,
                date = comment.publishDate,
            )
        }
    }
}
