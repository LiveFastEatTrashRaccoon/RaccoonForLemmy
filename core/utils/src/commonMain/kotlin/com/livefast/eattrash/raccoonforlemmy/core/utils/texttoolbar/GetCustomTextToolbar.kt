package com.livefast.eattrash.raccoonforlemmy.core.utils.texttoolbar

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.TextToolbar

@Composable
expect fun getCustomTextToolbar(
    quoteActionLabel: String? = null,
    shareActionLabel: String,
    onShare: () -> Unit,
    onQuote: (() -> Unit)? = null,
): TextToolbar
