package com.livefast.eattrash.raccoonforlemmy.core.utils.texttoolbar

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.TextToolbar

@Composable
expect fun getCustomTextToolbar(
    shareActionLabel: String,
    quoteActionLabel: String? = null,
    cancelActionLabel: String? = null,
    onShare: () -> Unit,
    onQuote: (() -> Unit)? = null,
    onCancel: (() -> Unit)? = null,
): TextToolbar
