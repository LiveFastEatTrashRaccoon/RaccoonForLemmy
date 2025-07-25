package com.livefast.eattrash.raccoonforlemmy.unit.rawcontent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalTextToolbar
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.IconSize
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.utils.VoteAction
import com.livefast.eattrash.raccoonforlemmy.core.utils.di.getShareHelper
import com.livefast.eattrash.raccoonforlemmy.core.utils.texttoolbar.getCustomTextToolbar
import com.livefast.eattrash.raccoonforlemmy.core.utils.toIcon
import com.livefast.eattrash.raccoonforlemmy.core.utils.toModifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RawContentDialog(
    modifier: Modifier = Modifier,
    publishDate: String? = null,
    updateDate: String? = null,
    title: String? = null,
    url: String? = null,
    text: String? = null,
    isLogged: Boolean = true,
    onDismiss: (() -> Unit)? = null,
    onQuote: ((String?) -> Unit)? = null,
    upVotes: Int? = null,
    downVotes: Int? = null,
) {
    val clipboardManager = LocalClipboardManager.current
    val shareHelper = remember { getShareHelper() }
    val onShareLambda: () -> Unit = {
        val query = clipboardManager.getText()?.text.orEmpty()
        shareHelper.share(query)
    }
    val onQuoteLambda: () -> Unit = {
        val query = clipboardManager.getText()?.text.orEmpty()
        onQuote?.invoke(query)
    }
    val quoteActionLabel =
        if (isLogged) {
            LocalStrings.current.actionQuote
        } else {
            null
        }
    val shareActionLabel = LocalStrings.current.postActionShare
    val cancelActionLabel = LocalStrings.current.buttonCancel
    val fullColor = MaterialTheme.colorScheme.onBackground
    val focusManager = LocalFocusManager.current

    BasicAlertDialog(
        modifier = modifier,
        onDismissRequest = { onDismiss?.invoke() },
    ) {
        CompositionLocalProvider(
            LocalTextToolbar provides
                getCustomTextToolbar(
                    quoteActionLabel = quoteActionLabel,
                    shareActionLabel = shareActionLabel,
                    cancelActionLabel = cancelActionLabel,
                    onShare = onShareLambda,
                    onQuote = onQuoteLambda,
                    onCancel = {
                        focusManager.clearFocus(true)
                    },
                ),
        ) {
            Column(
                modifier =
                Modifier
                    .background(color = MaterialTheme.colorScheme.surface)
                    .padding(vertical = Spacing.s),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = LocalStrings.current.dialogTitleRawContent,
                    style = MaterialTheme.typography.titleMedium,
                    color = fullColor,
                )
                Spacer(modifier = Modifier.height(Spacing.s))
                LazyColumn(
                    modifier =
                    Modifier
                        .padding(vertical = Spacing.s, horizontal = Spacing.m)
                        .heightIn(max = 400.dp),
                    verticalArrangement = Arrangement.spacedBy(Spacing.xs),
                ) {
                    title?.takeIf { it.trim().isNotEmpty() }?.also {
                        item {
                            Column {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = LocalStrings.current.dialogRawContentTitle,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = fullColor,
                                )
                                SelectionContainer {
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = it,
                                        style =
                                        MaterialTheme.typography.bodyLarge.copy(
                                            fontFamily = FontFamily.Monospace,
                                        ),
                                        color = fullColor,
                                    )
                                }
                            }
                        }
                    }
                    url?.takeIf { it.trim().isNotEmpty() }?.also {
                        item {
                            Column {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = LocalStrings.current.dialogRawContentUrl,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = fullColor,
                                )
                                SelectionContainer {
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = it,
                                        style =
                                        MaterialTheme.typography.bodyMedium.copy(
                                            fontFamily = FontFamily.Monospace,
                                        ),
                                        color = fullColor,
                                    )
                                }
                            }
                        }
                    }
                    text?.takeIf { it.trim().isNotEmpty() }?.also {
                        item {
                            Column {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = LocalStrings.current.dialogRawContentText,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = fullColor,
                                )
                                SelectionContainer {
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = it,
                                        style =
                                        MaterialTheme.typography.bodyMedium.copy(
                                            fontFamily = FontFamily.Monospace,
                                        ),
                                        color = fullColor,
                                    )
                                }
                            }
                        }
                    }

                    publishDate?.takeIf { it.trim().isNotEmpty() }?.also {
                        item {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(Spacing.xxs),
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Icon(
                                        modifier = Modifier.size(IconSize.m).padding(1.5.dp),
                                        imageVector = Icons.Default.Schedule,
                                        contentDescription = LocalStrings.current.creationDate,
                                        tint = fullColor,
                                    )
                                    Text(
                                        modifier = Modifier.weight(1f).padding(start = Spacing.xxs),
                                        text = it,
                                        style =
                                        MaterialTheme.typography.bodyMedium.copy(
                                            fontFamily = FontFamily.Monospace,
                                        ),
                                        color = fullColor,
                                    )
                                }
                            }
                        }
                    }
                    updateDate?.takeIf { it.trim().isNotEmpty() }?.also {
                        item {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(Spacing.xxs),
                            ) {
                                Icon(
                                    modifier = Modifier.size(IconSize.m).padding(0.25.dp),
                                    imageVector = Icons.Default.Update,
                                    contentDescription = LocalStrings.current.updateDate,
                                    tint = fullColor,
                                )
                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = it,
                                    style =
                                    MaterialTheme.typography.bodyMedium.copy(
                                        fontFamily = FontFamily.Monospace,
                                    ),
                                    color = fullColor,
                                )
                            }
                        }
                    }
                    if (upVotes != null && downVotes != null) {
                        item {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
                            ) {
                                Icon(
                                    modifier =
                                    Modifier
                                        .size(IconSize.m)
                                        .padding(end = 3.5.dp)
                                        .then(VoteAction.UpVote.toModifier()),
                                    imageVector = VoteAction.UpVote.toIcon(),
                                    contentDescription = LocalStrings.current.actionUpvote,
                                    tint = fullColor,
                                )
                                Text(
                                    text = "$upVotes",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = fullColor,
                                )

                                Spacer(modifier = Modifier.weight(1f))

                                Icon(
                                    modifier =
                                    Modifier
                                        .size(IconSize.m)
                                        .padding(end = 3.5.dp)
                                        .then(VoteAction.DownVote.toModifier()),
                                    imageVector = VoteAction.DownVote.toIcon(),
                                    contentDescription = LocalStrings.current.actionDownvote,
                                    tint = fullColor,
                                )
                                Text(
                                    text = "$downVotes",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = fullColor,
                                )

                                Spacer(modifier = Modifier.weight(1f))

                                val totalVotes = upVotes + downVotes
                                val percVote =
                                    if (totalVotes == 0) 0.0 else upVotes.toDouble() / totalVotes
                                val percentage = "${(percVote * 100).toInt()}"
                                Icon(
                                    modifier = Modifier.size(IconSize.m).padding(end = 3.5.dp),
                                    imageVector = Icons.Default.Percent,
                                    contentDescription = LocalStrings.current.settingsVoteFormatPercentage,
                                    tint = fullColor,
                                )
                                Text(
                                    text = percentage,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = fullColor,
                                )
                            }
                        }
                    }
                }
                Button(
                    onClick = {
                        onDismiss?.invoke()
                    },
                ) {
                    Text(text = LocalStrings.current.buttonClose)
                }
            }
        }
    }
}
