package com.livefast.eattrash.raccoonforlemmy.feature.inbox.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.livefast.eattrash.raccoonforlemmy.core.l10n.messages.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.TabNavigationSection
import com.livefast.eattrash.raccoonforlemmy.core.navigation.toInt
import com.livefast.eattrash.raccoonforlemmy.feature.inbox.main.InboxScreen

object InboxTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.Inbox)
            val title = LocalStrings.current.navigationInbox
            return TabOptions(
                index = TabNavigationSection.Inbox.toInt().toUShort(),
                title = title,
                icon = icon,
            )
        }

    @Composable
    override fun Content() {
        TabNavigator(InboxScreen)
    }
}
