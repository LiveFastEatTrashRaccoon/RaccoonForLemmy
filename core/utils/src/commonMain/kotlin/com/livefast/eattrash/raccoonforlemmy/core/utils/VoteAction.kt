package com.livefast.eattrash.raccoonforlemmy.core.utils

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.livefast.eattrash.raccoonforlemmy.core.resources.LocalResources

sealed interface VoteAction {
    data object UpVote : VoteAction

    data object DownVote : VoteAction
}

@Composable
fun VoteAction.toIcon() =
    when (this) {
        VoteAction.DownVote -> LocalResources.current.arrowCircleDown
        VoteAction.UpVote -> LocalResources.current.arrowCircleUp
    }

@Composable
fun VoteAction.toModifier(): Modifier = Modifier.padding(horizontal = 1.dp)
