package com.github.diegoberaldin.raccoonforlemmy.core.commonui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.diegoberaldin.raccoonforlemmy.core.markdown.compose.Markdown

@Composable
fun PostCardBody(
    modifier: Modifier = Modifier,
    text: String,
) {
    if (text.isNotEmpty()) {
        Markdown(
            modifier = modifier,
            content = text,
        )
    }
}
