package com.github.diegoberaldin.raccoonforlemmy.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.github.diegoberaldin.raccoonforlemmy.core.l10n.messages.LocalStrings
import com.github.diegoberaldin.raccoonforlemmy.unit.filteredcontents.FilteredContentsScreen
import com.github.diegoberaldin.raccoonforlemmy.unit.filteredcontents.FilteredContentsType
import com.github.diegoberaldin.raccoonforlemmy.unit.filteredcontents.toInt

object BookmarksTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.Bookmark)
            val title = LocalStrings.current.navigationDrawerTitleBookmarks

            return TabOptions(
                index = 5u,
                title = title,
                icon = icon,
            )
        }

    @Composable
    override fun Content() {
        Navigator(
            FilteredContentsScreen(
                type = FilteredContentsType.Bookmarks.toInt(),
            ),
        )
    }
}
