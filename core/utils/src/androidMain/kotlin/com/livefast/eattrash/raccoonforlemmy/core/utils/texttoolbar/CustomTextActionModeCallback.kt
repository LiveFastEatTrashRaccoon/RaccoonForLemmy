package com.livefast.eattrash.raccoonforlemmy.core.utils.texttoolbar

import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.compose.ui.geometry.Rect

private const val ACTION_ID_COPY = 0
private const val ACTION_ID_SEARCH = 1
private const val ACTION_ID_QUOTE = 2
private const val ACTION_ID_CANCEL = 3
private const val GROUP_ID = 0

internal class CustomTextActionModeCallback(
    private val rect: Rect,
    private val shareActionLabel: String,
    private val quoteActionLabel: String?,
    private val cancelActionLabel: String?,
    private val onCopy: () -> Unit,
    private val onShare: () -> Unit,
    private val onQuote: (() -> Unit)?,
    private val onCancel: (() -> Unit)?,
) : ActionMode.Callback2() {
    override fun onCreateActionMode(
        mode: ActionMode?,
        menu: Menu?,
    ): Boolean {
        menu?.apply {
            if (quoteActionLabel != null) {
                add(
                    GROUP_ID,
                    ACTION_ID_QUOTE,
                    0, // position
                    quoteActionLabel,
                ).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            }
            add(
                GROUP_ID,
                ACTION_ID_COPY,
                1, // position
                android.R.string.copy,
            ).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            add(
                GROUP_ID,
                ACTION_ID_SEARCH,
                2, // position
                shareActionLabel,
            ).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            add(
                GROUP_ID,
                ACTION_ID_CANCEL,
                3, // position
                cancelActionLabel,
            ).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        }
        return true
    }

    override fun onPrepareActionMode(
        mode: ActionMode?,
        menu: Menu?,
    ): Boolean = false

    override fun onActionItemClicked(
        mode: ActionMode?,
        item: MenuItem?,
    ): Boolean {
        val res =
            when (item?.itemId) {
                ACTION_ID_COPY -> {
                    onCopy()
                    true
                }

                ACTION_ID_SEARCH -> {
                    onShare()
                    true
                }

                ACTION_ID_QUOTE -> {
                    onQuote?.invoke()
                    true
                }

                ACTION_ID_CANCEL -> {
                    onCancel?.invoke()
                    true
                }

                else -> false
            }
        mode?.finish()
        return res
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        // no-op
    }

    override fun onGetContentRect(
        mode: ActionMode?,
        view: View?,
        outRect: android.graphics.Rect?,
    ) {
        rect.apply {
            outRect?.set(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
        }
    }
}
