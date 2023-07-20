package com.github.diegoberaldin.raccoonforlemmy.feature_inbox

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions

object InboxTab : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val title = "Inbox"
            val icon = rememberVectorPainter(Icons.Default.Email)

            return remember {
                TabOptions(
                    index = 1u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        val model = rememberScreenModel { getInboxScreenModel() }
        Column(modifier = Modifier.padding(4.dp)) {
            Text(
                text = "Inbox content"
            )
        }
    }
}