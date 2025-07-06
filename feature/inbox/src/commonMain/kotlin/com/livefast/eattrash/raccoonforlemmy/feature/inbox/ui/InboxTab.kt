package com.livefast.eattrash.raccoonforlemmy.feature.inbox.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.livefast.eattrash.raccoonforlemmy.feature.inbox.main.InboxMviModel
import com.livefast.eattrash.raccoonforlemmy.feature.inbox.main.InboxScreen

@Composable
fun InboxTab(model: InboxMviModel, modifier: Modifier = Modifier) {
    InboxScreen(modifier = modifier, model = model)
}
