package com.github.diegoberaldin.raccoonforlemmy.core.markdown.compose

import io.noties.markwon.Markwon

interface MarkwonProvider {
    val markwon: Markwon
}