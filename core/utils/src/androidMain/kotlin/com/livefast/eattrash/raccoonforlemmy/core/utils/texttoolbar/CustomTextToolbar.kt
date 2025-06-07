package com.livefast.eattrash.raccoonforlemmy.core.utils.texttoolbar

import android.view.ActionMode
import android.view.View
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.TextToolbar
import androidx.compose.ui.platform.TextToolbarStatus

internal class CustomTextToolbar(
    private val view: View,
    private val shareActionLabel: String,
    private val quoteActionLabel: String?,
    private val cancelActionLabel: String?,
    private val onShare: (() -> Unit)? = null,
    private val onQuote: (() -> Unit)? = null,
    private val onCancel: (() -> Unit)? = null,
) : TextToolbar {
    private var actionMode: ActionMode? = null

    override var status: TextToolbarStatus = TextToolbarStatus.Hidden
        private set

    override fun hide() {
        status = TextToolbarStatus.Hidden
        actionMode?.finish()
        actionMode = null
    }

    override fun showMenu(
        rect: Rect,
        onCopyRequested: (() -> Unit)?,
        onPasteRequested: (() -> Unit)?,
        onCutRequested: (() -> Unit)?,
        onSelectAllRequested: (() -> Unit)?,
    ) {
        if (actionMode == null) {
            status = TextToolbarStatus.Shown
            actionMode =
                view.startActionMode(
                    CustomTextActionModeCallback(
                        rect = rect,
                        shareActionLabel = shareActionLabel,
                        quoteActionLabel = quoteActionLabel,
                        cancelActionLabel = cancelActionLabel,
                        onCopy = {
                            onCopyRequested?.invoke()
                        },
                        onShare = {
                            onCopyRequested?.invoke()
                            onShare?.invoke()
                        },
                        onQuote = {
                            onCopyRequested?.invoke()
                            onQuote?.invoke()
                        },
                        onCancel = onCancel,
                    ),
                    ActionMode.TYPE_FLOATING,
                )
        } else {
            actionMode?.invalidate()
            actionMode = null
        }
    }
}
