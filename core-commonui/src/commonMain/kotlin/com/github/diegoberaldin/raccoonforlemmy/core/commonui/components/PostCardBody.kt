package com.github.diegoberaldin.raccoonforlemmy.core.commonui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.diegoberaldin.raccoonforlemmy.core.markdown.compose.Markdown
import com.github.diegoberaldin.raccoonforlemmy.core.markdown.model.markdownColor
import com.github.diegoberaldin.raccoonforlemmy.core.markdown.model.markdownTypography

@Composable
fun PostCardBody(
    modifier: Modifier = Modifier,
    text: String,
) {
    if (text.isNotEmpty()) {
        Markdown(
            modifier = modifier,
            content = text,
            typography = markdownTypography(
                h1 = MaterialTheme.typography.displayLarge,
                h2 = MaterialTheme.typography.displayMedium,
                h3 = MaterialTheme.typography.displaySmall,
                h4 = MaterialTheme.typography.headlineMedium,
                h5 = MaterialTheme.typography.headlineSmall,
                h6 = MaterialTheme.typography.titleLarge,
                text = MaterialTheme.typography.bodyMedium,
                paragraph = MaterialTheme.typography.bodyMedium,
            ),
            colors = markdownColor(
                text = MaterialTheme.colorScheme.onSurfaceVariant,
                backgroundCode = MaterialTheme.colorScheme.surfaceVariant,
            ),
        )
    }
}
