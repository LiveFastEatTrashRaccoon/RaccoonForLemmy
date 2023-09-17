package com.github.diegoberaldin.raccoonforlemmy.feature.profile.content.logged

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.di.getThemeRepository
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.CornerSize
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.PostCardBody
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.PostCardFooter
import com.github.diegoberaldin.raccoonforlemmy.core.commonui.components.PostCardSubtitle
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommentModel

@Composable
fun ProfileCommentCard(
    comment: CommentModel,
    options: List<String> = emptyList(),
    onOptionSelected: ((Int) -> Unit)? = null,
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
                    options = options,
                    onOptionSelected = onOptionSelected,
                )
            }
        }
    }
}
