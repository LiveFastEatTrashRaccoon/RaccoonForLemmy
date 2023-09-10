package com.github.diegoberaldin.raccoonforlemmy.core.commonui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.diegoberaldin.raccoonforlemmy.core.markdown.compose.Markdown
import com.github.diegoberaldin.raccoonforlemmy.core.markdown.model.markdownColor
import com.github.diegoberaldin.raccoonforlemmy.core.markdown.model.markdownTypography
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.PostModel

@Composable
fun PostCardTitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    Markdown(
        modifier = modifier,
        content = text,
        typography = markdownTypography(
            text = MaterialTheme.typography.displaySmall,
        ),
        colors = markdownColor(
            text = MaterialTheme.colorScheme.onSurfaceVariant,
            backgroundCode = MaterialTheme.colorScheme.surfaceVariant,
        ),
    )
}
