package com.livefast.eattrash.raccoonforlemmy.core.utils.texttoolbar

import android.view.ActionMode
import android.view.View
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.TextToolbar
import androidx.compose.ui.platform.TextToolbarStatus

internal class CustomTextToolbar(
    private val view: View,
    private val quoteActionLabel: String?,
    private val shareActionLabel: String,
    private val onShare: () -> Unit,
    private val onQuote: (() -> Unit)?,
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
                        quoteActionLabel = quoteActionLabel,
                        shareActionLabel = shareActionLabel,
                        onCopy = {
                            onCopyRequested?.invoke()
                        },
                        onShare = {
                            onCopyRequested?.invoke()
                            onShare()
                        },
                        onQuote = {
                            onCopyRequested?.invoke()
                            onQuote?.invoke()
                        },
                    ),
                    ActionMode.TYPE_FLOATING,
                )
        } else {
            actionMode?.invalidate()
            actionMode = null
        }
    }
}
