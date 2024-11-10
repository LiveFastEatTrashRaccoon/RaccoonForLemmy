package com.livefast.eattrash.raccoonforlemmy.core.utils

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

sealed interface VoteAction {
    data object UpVote : VoteAction

    data object DownVote : VoteAction
}

@Composable
fun VoteAction.toIcon() =
    when (this) {
        VoteAction.DownVote -> Icons.Default.ArrowDownward
        VoteAction.UpVote -> Icons.Default.ArrowUpward
    }

@Composable
fun VoteAction.toModifier(): Modifier = Modifier.padding(horizontal = 1.dp)
