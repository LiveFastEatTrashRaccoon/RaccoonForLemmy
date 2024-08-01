package com.github.diegoberaldin.raccoonforlemmy.core.utils.texttoolbar

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.TextToolbar

@Composable
actual fun getCustomTextToolbar(
    quoteActionLabel: String?,
    shareActionLabel: String,
    onShare: () -> Unit,
    onQuote: (() -> Unit)?,
): TextToolbar =
    CustomTextToolbar(
        view = LocalView.current,
        quoteActionLabel = quoteActionLabel,
        shareActionLabel = shareActionLabel,
        onShare = onShare,
        onQuote = onQuote,
    )
