package com.github.diegoberaldin.raccoonforlemmy.core.utils.texttoolbar

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalTextToolbar
import androidx.compose.ui.platform.TextToolbar

@Composable
actual fun getCustomTextToolbar(
    quoteActionLabel: String?,
    shareActionLabel: String,
    onShare: () -> Unit,
    onQuote: (() -> Unit)?,
): TextToolbar = LocalTextToolbar.current
