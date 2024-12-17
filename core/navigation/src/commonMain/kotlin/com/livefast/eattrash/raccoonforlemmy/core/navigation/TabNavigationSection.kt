package com.livefast.eattrash.raccoonforlemmy.core.navigation

import androidx.compose.runtime.Composable
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings

sealed interface TabNavigationSection {
    data object Home : TabNavigationSection

    data object Explore : TabNavigationSection

    data object Profile : TabNavigationSection

    data object Inbox : TabNavigationSection

    data object Settings : TabNavigationSection

    data object Bookmarks : TabNavigationSection
}

@Composable
fun TabNavigationSection.toReadableName(): String =
    when (this) {
        TabNavigationSection.Bookmarks -> LocalStrings.current.navigationDrawerTitleBookmarks
        TabNavigationSection.Explore -> LocalStrings.current.navigationSearch
        TabNavigationSection.Home -> LocalStrings.current.navigationHome
        TabNavigationSection.Inbox -> LocalStrings.current.navigationInbox
        TabNavigationSection.Profile -> LocalStrings.current.navigationProfile
        TabNavigationSection.Settings -> LocalStrings.current.navigationSettings
    }

fun Int.toTabNavigationSection(): TabNavigationSection? =
    when (this) {
        0 -> TabNavigationSection.Home
        1 -> TabNavigationSection.Explore
        2 -> TabNavigationSection.Inbox
        3 -> TabNavigationSection.Profile
        4 -> TabNavigationSection.Settings
        5 -> TabNavigationSection.Bookmarks
        else -> null
    }

fun TabNavigationSection.toInt(): Int =
    when (this) {
        TabNavigationSection.Home -> 0
        TabNavigationSection.Explore -> 1
        TabNavigationSection.Inbox -> 2
        TabNavigationSection.Profile -> 3
        TabNavigationSection.Settings -> 4
        TabNavigationSection.Bookmarks -> 5
    }

fun List<Int>.toTabNavigationSections(): List<TabNavigationSection> = mapNotNull { it.toTabNavigationSection() }

fun List<TabNavigationSection>.toInts(): List<Int> = map { it.toInt() }
