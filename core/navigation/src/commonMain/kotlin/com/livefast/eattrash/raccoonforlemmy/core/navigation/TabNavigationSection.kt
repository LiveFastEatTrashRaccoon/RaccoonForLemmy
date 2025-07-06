package com.livefast.eattrash.raccoonforlemmy.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import kotlinx.serialization.Serializable

sealed interface TabNavigationSection {
    @Serializable
    data object Home : TabNavigationSection

    @Serializable
    data object Explore : TabNavigationSection

    @Serializable
    data object Profile : TabNavigationSection

    @Serializable
    data object Inbox : TabNavigationSection

    @Serializable
    data object Settings : TabNavigationSection

    @Serializable
    data object Bookmarks : TabNavigationSection
}

@Composable
fun TabNavigationSection.toReadableName(): String = when (this) {
    TabNavigationSection.Bookmarks -> LocalStrings.current.navigationDrawerTitleBookmarks
    TabNavigationSection.Explore -> LocalStrings.current.navigationSearch
    TabNavigationSection.Home -> LocalStrings.current.navigationHome
    TabNavigationSection.Inbox -> LocalStrings.current.navigationInbox
    TabNavigationSection.Profile -> LocalStrings.current.navigationProfile
    TabNavigationSection.Settings -> LocalStrings.current.navigationSettings
}

@Composable
fun TabNavigationSection.toIcon(): ImageVector = when (this) {
    TabNavigationSection.Bookmarks -> Icons.Default.Bookmarks
    TabNavigationSection.Explore -> Icons.Default.Explore
    TabNavigationSection.Home -> Icons.AutoMirrored.Default.Article
    TabNavigationSection.Inbox -> Icons.Default.Inbox
    TabNavigationSection.Profile -> Icons.Default.AccountCircle
    TabNavigationSection.Settings -> Icons.Default.Settings
}

fun Int.toTabNavigationSection(): TabNavigationSection? = when (this) {
    0 -> TabNavigationSection.Home
    1 -> TabNavigationSection.Explore
    2 -> TabNavigationSection.Inbox
    3 -> TabNavigationSection.Profile
    4 -> TabNavigationSection.Settings
    5 -> TabNavigationSection.Bookmarks
    else -> null
}

fun TabNavigationSection.toInt(): Int = when (this) {
    TabNavigationSection.Home -> 0
    TabNavigationSection.Explore -> 1
    TabNavigationSection.Inbox -> 2
    TabNavigationSection.Profile -> 3
    TabNavigationSection.Settings -> 4
    TabNavigationSection.Bookmarks -> 5
}

fun List<Int>.toTabNavigationSections(): List<TabNavigationSection> = mapNotNull { it.toTabNavigationSection() }

fun List<TabNavigationSection>.toInts(): List<Int> = map { it.toInt() }
