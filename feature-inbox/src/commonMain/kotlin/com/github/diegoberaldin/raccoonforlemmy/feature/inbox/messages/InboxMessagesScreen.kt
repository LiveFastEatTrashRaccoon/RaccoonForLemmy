package com.github.diegoberaldin.raccoonforlemmy.feature.inbox.messages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen

class InboxMessagesScreen : Screen {
    @Composable
    override fun Content() {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "\uD83D\uDEA7 Work in progress! \uD83D\uDEA7",
                style = MaterialTheme.typography.titleLarge,
            )
        }
    }
}