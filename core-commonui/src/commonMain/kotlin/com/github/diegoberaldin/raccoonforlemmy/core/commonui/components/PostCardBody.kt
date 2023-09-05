package com.github.diegoberaldin.raccoonforlemmy.core.commonui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.diegoberaldin.raccoonforlemmy.core.markdown.compose.Markdown
import com.github.diegoberaldin.raccoonforlemmy.core.markdown.model.markdownColor

@Composable
fun PostCardBody(
    modifier: Modifier = Modifier,
    text: String,
) {
    if (text.isNotEmpty()) {
        Markdown(
            modifier = modifier,
            content = text,
            colors = markdownColor(
                text = MaterialTheme.colorScheme.onSurfaceVariant,
                backgroundCode = MaterialTheme.colorScheme.surfaceVariant,
            )
        )
    }
}
