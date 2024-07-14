package com.github.diegoberaldin.raccoonforlemmy.feature.home.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.github.diegoberaldin.raccoonforlemmy.core.l10n.messages.LocalStrings
import com.github.diegoberaldin.raccoonforlemmy.core.navigation.TabNavigationSection
import com.github.diegoberaldin.raccoonforlemmy.core.navigation.toInt
import com.github.diegoberaldin.raccoonforlemmy.unit.postlist.PostListScreen

object HomeTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.AutoMirrored.Default.Article)
            val title = LocalStrings.current.navigationHome

            return TabOptions(
                index = TabNavigationSection.Home.toInt().toUShort(),
                title = title,
                icon = icon,
            )
        }

    @Composable
    override fun Content() {
        Navigator(PostListScreen())
    }
}
