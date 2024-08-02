package com.livefast.eattrash.raccoonforlemmy.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.livefast.eattrash.raccoonforlemmy.core.l10n.messages.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.navigation.TabNavigationSection
import com.livefast.eattrash.raccoonforlemmy.core.navigation.toInt
import com.livefast.eattrash.raccoonforlemmy.unit.filteredcontents.FilteredContentsScreen
import com.livefast.eattrash.raccoonforlemmy.unit.filteredcontents.FilteredContentsType
import com.livefast.eattrash.raccoonforlemmy.unit.filteredcontents.toInt

object BookmarksTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.Bookmark)
            val title = LocalStrings.current.navigationDrawerTitleBookmarks

            return TabOptions(
                index = TabNavigationSection.Bookmarks.toInt().toUShort(),
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
