package com.livefast.eattrash.raccoonforlemmy.core.utils.texttoolbar

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.TextToolbar

@Composable
actual fun getCustomTextToolbar(
    shareActionLabel: String,
    quoteActionLabel: String?,
    cancelActionLabel: String?,
    onShare: (() -> Unit)?,
    onQuote: (() -> Unit)?,
    onCancel: (() -> Unit)?,
): TextToolbar =
    CustomTextToolbar(
        view = LocalView.current,
        quoteActionLabel = quoteActionLabel,
        shareActionLabel = shareActionLabel,
        cancelActionLabel = cancelActionLabel,
        onShare = onShare,
        onQuote = onQuote,
        onCancel = onCancel,
    )
