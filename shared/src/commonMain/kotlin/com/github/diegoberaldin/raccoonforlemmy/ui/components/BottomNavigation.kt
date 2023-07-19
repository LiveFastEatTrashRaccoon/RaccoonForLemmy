package com.github.diegoberaldin.raccoonforlemmy.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.github.diegoberaldin.raccoonforlemmy.utils.onClick

sealed interface BottomNavigationSection {

    object Home : BottomNavigationSection
    object Inbox : BottomNavigationSection
    object Account : BottomNavigationSection
    object Search : BottomNavigationSection
    object Settings : BottomNavigationSection

    companion object {
        val ALL = listOf(
            Home,
            Inbox,
            Account,
            Search,
            Settings,
        )
    }
}

@Composable
internal fun BottomNavigation(
    modifier: Modifier = Modifier,
    current: BottomNavigationSection = BottomNavigationSection.Home,
    onSectionSelected: ((BottomNavigationSection) -> Unit)? = null,
) {
    BottomAppBar(
        modifier = modifier.height(80.dp),
    ) {
        val iconModifier = Modifier.size(30.dp)
        for (section in BottomNavigationSection.ALL) {
            Column(
                modifier = Modifier.weight(1f)
                    .onClick {
                        onSectionSelected?.invoke(section)
                    }
                    .padding(top = 10.dp)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val color = if (section == current) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outline
                }
                Icon(
                    modifier = iconModifier,
                    imageVector = section.icon,
                    contentDescription = null,
                    tint = color,
                )
                Text(
                    text = section.title,
                    style = MaterialTheme.typography.labelSmall,
                    color = color,
                )
            }
        }
    }
}

private val BottomNavigationSection.icon: ImageVector
    get() = when (this) {
        BottomNavigationSection.Home -> Icons.Default.Home
        BottomNavigationSection.Account -> Icons.Default.Person
        BottomNavigationSection.Inbox -> Icons.Default.Email
        BottomNavigationSection.Search -> Icons.Default.Search
        BottomNavigationSection.Settings -> Icons.Default.Settings
    }

private val BottomNavigationSection.title: String
    get() = when (this) {
        BottomNavigationSection.Home -> "Posts"
        BottomNavigationSection.Account -> "Account"
        BottomNavigationSection.Inbox -> "Inbox"
        BottomNavigationSection.Search -> "Search"
        BottomNavigationSection.Settings -> "Settings"
    }